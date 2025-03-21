package fr.systerel.rodinextension.sample.sc.modules;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eventb.core.EventBAttributes;
import org.eventb.core.IConvergenceElement.Convergence;
import org.eventb.core.IEvent;
import org.eventb.core.IExpressionElement;
import org.eventb.core.IMachineRoot;
import org.eventb.core.IVariant;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.IParseResult;
import org.eventb.core.ast.ITypeEnvironment;
import org.eventb.core.sc.SCCore;
import org.eventb.core.sc.SCProcessorModule;
import org.eventb.core.sc.state.ISCStateRepository;
import org.eventb.core.tool.IModuleType;
import org.rodinp.core.IAttributeType;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.RodinCore;
import org.rodinp.core.RodinDBException;

import fr.systerel.rodinextension.sample.QualProbPlugin;
import fr.systerel.rodinextension.sample.basis.IBound;
import fr.systerel.rodinextension.sample.basis.ISCBound;

import org.eventb.core.ast.Type;

public class MachineBoundModule extends SCProcessorModule {

	public static final IModuleType<MachineBoundModule> MODULE_TYPE = SCCore
			.getModuleType(QualProbPlugin.PLUGIN_ID + ".machineBoundModule");

	public static final IAttributeType.Boolean PROB_ATTRIBUTE = RodinCore
			.getBooleanAttrType(QualProbPlugin.PLUGIN_ID + ".probabilistic");
	
	private FormulaFactory factory;
	
	private static final String BOUND_NAME_PREFIX = "BND";


	@Override
	public IModuleType<?> getModuleType() {
		return MODULE_TYPE;
	}

	@Override
	public void process(IRodinElement element, IInternalElement target, ISCStateRepository repository,
			IProgressMonitor monitor) throws CoreException {
		
		final IRodinFile machineFile = (IRodinFile) element;
		final IMachineRoot machineRoot = (IMachineRoot) machineFile.getRoot();
		final ProbConvDesc pbDesc = new ProbConvDesc(machineRoot);
		factory = machineRoot.getSafeFormulaFactory();		
		final IBound [] bounds = machineRoot.getChildrenOfType(IBound.ELEMENT_TYPE);
		final IVariant [] variants = machineRoot.getVariants();
		
		
	if(!pbDesc.isProbabilistic()) {
		if(bounds.length > 0) {
			createProblemMarker(bounds[bounds.length -1], EventBAttributes.EXPRESSION_ATTRIBUTE, ProbabilisticGraphProblem.NoProbabilisticEventButBoundWarning);
		}
	}
	
	if(pbDesc.isProbabilistic() && bounds.length > 1) {
		createProblemMarker(bounds[bounds.length - 1], EventBAttributes.EXPRESSION_ATTRIBUTE, ProbabilisticGraphProblem.TooManyBoundsError);
		return;
	}
	
	if(pbDesc.isProbabilistic()) {
		ITypeEnvironment typeEnv = repository.getTypeEnvironment();
		final IVariant variant = variants[0];
		
		if(!assertIsASetOrConstant(variant, typeEnv)) {
			return;
		}
		
		if(bounds.length != 1) { // ExistABound, but idk why it should be extracted other than style
			createProblemMarker(machineRoot, EventBAttributes.EXPRESSION_ATTRIBUTE, ProbabilisticGraphProblem.BoundMustBeSpecified);
			return;
		}
		
		if (!filterModules(element, repository, monitor)) {
			return;
		}
		
		if(!assertIsASetOrConstant(bounds[0], typeEnv)) {
			return;
		}
		
		if(getType(bounds[0], typeEnv).equals(getType(variant, typeEnv))== false) {
			createProblemMarker(bounds[0], EventBAttributes.EXPRESSION_ATTRIBUTE, ProbabilisticGraphProblem.VariantBoundTypeError);
			return;
		}
		
		final ISCBound scBound = target.getInternalElement(ISCBound.ELEMENT_TYPE, BOUND_NAME_PREFIX);
		scBound.create(null, monitor);
		scBound.setExpression(getExpression(bounds[0]), null);
	}
		
	}
	
	private boolean assertIsASetOrConstant(IExpressionElement element, ITypeEnvironment typeEnv)
			throws RodinDBException {
		
		final Type type = getType(element, typeEnv);
		if (type == null) {
			return false;
		}
		if (!isASetOrConstant(type)) {
			if (element instanceof IBound) {
				createProblemMarker(element,
						EventBAttributes.EXPRESSION_ATTRIBUTE,
						ProbabilisticGraphProblem.BoundMustBeConstantError);
			}
			if (element instanceof IVariant) {
				createProblemMarker(element,
						EventBAttributes.EXPRESSION_ATTRIBUTE,
						ProbabilisticGraphProblem.VariantMustBeConstantError);
			}
			return false;
		}
		return true;
	}
	
	private Type getType(IExpressionElement element, ITypeEnvironment typeEnv) throws RodinDBException {
		final Expression boundExpression = getExpression(element);
		boundExpression.typeCheck(typeEnv);
		return boundExpression.getType();
	}
	
	private Expression getExpression(IExpressionElement element)
			throws RodinDBException {
		final String formula = element.getExpressionString();
		final IParseResult result = factory.parseExpression(formula, element);
		final Expression boundExpression = result.getParsedExpression();
		return boundExpression;
	}
	
	private boolean isASetOrConstant(Type type) {
		if (type.equals(factory.makeIntegerType())) {
			return true;
		}
		final Type baseType = type.getBaseType();
		if (baseType != null) {
			return type.equals(factory.makePowerSetType(baseType));
		}
		return false;
	}

	private static class ProbConvDesc{
		private boolean convergent;
		private boolean probabilistic;
		
		public ProbConvDesc(IMachineRoot root) {
			convergent = false;
			probabilistic = false;
			try {
				convergent = isConvergentModel(root);
				probabilistic = isProbabilisticModel(root);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private boolean isProbabilisticModel(IMachineRoot root) throws CoreException {
			for(IEvent event : root.getEvents()) {
				if(event.getConvergence() == Convergence.CONVERGENT && event.getAttributeValue(PROB_ATTRIBUTE)) {
					return true;
				}
			}
			return false;
		}
		
		//getters
		public boolean isProbabilistic() {
			return probabilistic;
		}
		
		public boolean isConvergent() {
			return convergent;
		}

		private boolean isConvergentModel(IMachineRoot root) throws CoreException {
			for(IEvent event : root.getEvents()) {
				if(event.getConvergence() == Convergence.CONVERGENT) {
					return true;
				}
			}
			return false;
		}
		
	}
	
	

}
