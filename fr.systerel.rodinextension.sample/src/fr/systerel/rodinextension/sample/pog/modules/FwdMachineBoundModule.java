package fr.systerel.rodinextension.sample.pog.modules;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eventb.core.IPORoot;
import org.eventb.core.IPOSource;
import org.eventb.core.ISCMachineRoot;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.Formula;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.ITypeEnvironment;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.ProductType;
import org.eventb.core.ast.Type;
import org.eventb.core.pog.IPOGSource;
import org.eventb.core.pog.POGProcessorModule;
import org.eventb.core.pog.state.IMachineHypothesisManager;
import org.eventb.core.pog.state.IPOGStateRepository;
import org.eventb.core.tool.IModuleType;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;

import fr.systerel.rodinextension.sample.basis.ISCBound;

public class FwdMachineBoundModule extends POGProcessorModule {
	
	//final MachineBoundInfo machineBoundInfo = (IMachineBoundInfo) repository.getState(IMachineBoundInfo.STATE_TYPE);

	public FwdMachineBoundModule() {
	}

	@Override
	public void process(IRodinElement element, IPOGStateRepository repository, IProgressMonitor monitor)
			throws CoreException {
		final IMachineBoundInfo machineBoundInfo = 
				  (IMachineBoundInfo) repository.getState(IMachineBoundInfo.STATE_TYPE);
				final ISCBound scBound = machineBoundInfo.getBound();
				final Expression expr = machineBoundInfo.getExpression();
				final FormulaFactory ff = repository.getFormulaFactory();
				final IPOGSource[] sources = new IPOGSource[] { makeSource(IPOSource.DEFAULT_ROLE, scBound.getSource()) };
				final IPORoot target = repository.getTarget();
				final IMachineHypothesisManager machineHypothesisManager = (IMachineHypothesisManager)
				  repository.getState(IMachineHypothesisManager.STATE_TYPE);

				// if the finiteness of bound is not trivial
				// we generate the PO
				if (mustProveFinite(expr, ff)) {
				  final Predicate finPredicate = ff.makeSimplePredicate(Formula.KFINITE, expr, null);
				  createPO(target, "BFN",
				    POGProcessorModule.makeNature("Finiteness of bound"),
				    machineHypothesisManager.getFullHypothesis(),
				    null, makePredicate(finPredicate, scBound.getSource()), sources,
				    null, machineHypothesisManager.machineIsAccurate(), monitor);
				}

	}

	@Override
	public IModuleType<?> getModuleType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private IMachineBoundInfo createMachineBoundInfo(IRodinElement element, IPOGStateRepository repository) throws CoreException{
		final IRodinFile machineFile = (IRodinFile) element;
		final ISCMachineRoot root = (ISCMachineRoot) machineFile.getRoot();
		final ISCBound[] bounds = root.getChildrenOfType(ISCBound.ELEMENT_TYPE);
		if(bounds.length != 1) {
			return new MachineBoundInfo();
		}
		final ISCBound scBound = bounds[0];
		final ITypeEnvironment typeEnv = repository.getTypeEnvironment();
		final Expression expr = scBound.getExpression(typeEnv);
		return new MachineBoundInfo(expr, scBound);
	}
	
	private boolean mustProveFinite(Expression expr, FormulaFactory ff) {
		final Type type = expr.getType();
		if (type.equals(ff.makeIntegerType()))
			return false;
		if (derivedFromBoolean(type, ff))
			return false;
		return true;
	}
	
	private boolean derivedFromBoolean(Type type, FormulaFactory ff) {
		if (type.equals(ff.makeBooleanType()))
			return true;
		final Type baseType = type.getBaseType();
		if (baseType != null)
			return derivedFromBoolean(baseType, ff);
		if (type instanceof ProductType) {
			final ProductType productType = (ProductType) type;
			return derivedFromBoolean(productType.getLeft(), ff) && 
	                      derivedFromBoolean(productType.getRight(), ff);
		}
		return false;
	}

}
