package fr.systerel.rodinextension.sample.pog.modules;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eventb.core.pog.POGCore;
import org.eventb.core.pog.POGFilterModule;
import org.eventb.core.pog.state.IPOGStateRepository;
import org.eventb.core.tool.IModuleType;

import fr.systerel.rodinextension.sample.QualProbPlugin;

public class FinPORejectingFilterModule extends POGFilterModule {

	private static final IModuleType<FinPORejectingFilterModule> MODULE_TYPE =
         POGCore.getModuleType(QualProbPlugin.PLUGIN_ID + ".finPORejectingModule");
	private IMachineBoundInfo boundInfo;

	@Override
	public IModuleType<?> getModuleType() {
		return MODULE_TYPE;
	}

	@Override
	public boolean accept(String poName, IProgressMonitor monitor) 
         throws CoreException {
		if (! boundInfo.machineHasBound()) {
			return true;
		}
		final boolean rejectedFIN = poName.equals("FIN");
		if (QualProbPlugin.DEBUG) {
			System.out.println("PO " + poName + " is "+ (rejectedFIN ? "" : "not ") + "filtered out.");
		}
		return !rejectedFIN;
	}

	@Override
	public void initModule(IPOGStateRepository repository, IProgressMonitor monitor) 
         throws CoreException {
		boundInfo = (IMachineBoundInfo) repository.getState(IMachineBoundInfo.STATE_TYPE);
	}

	@Override
	public void endModule(IPOGStateRepository repository, IProgressMonitor monitor) 
         throws CoreException {
		boundInfo = null;
	}

}