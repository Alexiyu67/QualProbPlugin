package fr.systerel.rodinextension.sample;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rodinp.core.RodinCore;

public class QualProbPlugin implements BundleActivator {

	public static final String PLUGIN_ID = "fr.systerel.rodinextension.sample";
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	public void start(BundleContext bundleContext) throws Exception {
		QualProbPlugin.context = bundleContext;
		setProbConfig();
	}

	public void stop(BundleContext bundleContext) throws Exception {
		QualProbPlugin.context = null;
	}
	
	public static void setProbConfig() {
		RodinCore.addElementChangedListener(new ConfSettor());
	}

}
