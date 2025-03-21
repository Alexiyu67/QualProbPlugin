package fr.systerel.rodinextension.sample.sc.modules;

import java.security.cert.Extension;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eventb.core.EventBAttributes;
import org.eventb.core.IMachineRoot;
import org.eventb.core.ast.Expression;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.FreeIdentifier;
import org.eventb.core.ast.IParseResult;
import org.eventb.core.ast.ITypeEnvironment;
import org.eventb.core.sc.SCCore;
import org.eventb.core.sc.SCFilterModule;
import org.eventb.core.sc.state.ISCStateRepository;
import org.eventb.core.tool.IModuleType;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;
import org.eventb.core.ast.extension.IFormulaExtension;

import fr.systerel.rodinextension.sample.QualProbPlugin;
import fr.systerel.rodinextension.sample.basis.IBound;

public class MachineBoundFreeIdentsModule extends SCFilterModule{
	
	public static final IModuleType<MachineBoundModule> MODULE_TYPE = SCCore.getModuleType(QualProbPlugin.PLUGIN_ID + ".machineBoundFreeIdentsModule");

	private static final Set<IFormulaExtension> extensions = Collections.emptySet();
	private static final FormulaFactory factory = FormulaFactory.getInstance(extensions);
	
	@Override
	public boolean accept(IRodinElement element, ISCStateRepository repository, IProgressMonitor monitor)
			throws CoreException {
		final IRodinFile file = (IRodinFile) element;
		final IMachineRoot root = (IMachineRoot) file.getRoot();
		
		final IBound[] bounds = root.getChildrenOfType(IBound.ELEMENT_TYPE);
		
		//filter wird nach erstem check in MachienBoudnModule aufgerufen, also gibt es nur einen bound
		final IBound bound = bounds[0];
		final IParseResult parseResult = factory.parseExpression(bound.getExpressionString(), bound);
		
		if(parseResult.hasProblem()) {
			createProblemMarker(bound, EventBAttributes.EXPRESSION_ATTRIBUTE, ProbabilisticGraphProblem.InvalidBoundExpressionError, bound);
			return false;
		}
		
		//No problems, expression parsed
		
		final ITypeEnvironment typeEnv = repository.getTypeEnvironment();
		final Expression parsedExpression = parseResult.getParsedExpression();
		
		final FreeIdentifier[] freeIdentifiers = parsedExpression.getFreeIdentifiers();
		
		boolean ok = true;
		
		//All Free Identifiers must be usable as specified in exercise description
		for(FreeIdentifier ident : freeIdentifiers) {
			if(!typeEnv.contains(ident.getName())) {
				createProblemMarker(bound, EventBAttributes.EXPRESSION_ATTRIBUTE, ProbabilisticGraphProblem.BoundFreeIdentifierError, ident);
				ok = false;
			}
		}
		return ok;
	}

	@Override
	public IModuleType<?> getModuleType() {
		return MODULE_TYPE;
	}
	
}