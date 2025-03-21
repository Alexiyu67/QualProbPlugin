package fr.systerel.rodinextension.sample;

import org.eventb.core.IMachineRoot;
import org.rodinp.core.ElementChangedEvent;
import org.rodinp.core.IElementChangedListener;
import org.rodinp.core.IElementType;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinDB;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinElementDelta;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.IRodinProblem;
import org.rodinp.core.IRodinProject;
import org.rodinp.core.RodinDBException;

public class ConfSettor implements IElementChangedListener {
	
	private static final String QUAL_PROB_CONFIG = QualProbPlugin.PLUGIN_ID + ".qpConfig";

	@Override
	public void elementChanged(ElementChangedEvent event) {
		final IRodinElementDelta d = event.getDelta();
		try {
			processDelta(d);
		} catch (RodinDBException e) {
			e.printStackTrace();
		}

	}

	private void processDelta(final IRodinElementDelta d) throws RodinDBException {
		final IRodinElement e = d.getElement();
		
		final IElementType<? extends IRodinElement> elementType = e.getElementType();
		
		if(elementType.equals(IRodinDB.ELEMENT_TYPE) || elementType.equals(IRodinProject.ELEMENT_TYPE)) {
			for(final IRodinElementDelta de: d.getAffectedChildren()) {
				processDelta(de);
			}
		}else if(elementType.equals(IRodinFile.ELEMENT_TYPE)) {
			final IInternalElement root = ((IRodinFile) e).getRoot();
			
			if(root.getElementType().equals(IMachineRoot.ELEMENT_TYPE)) {
				final IMachineRoot mch = (IMachineRoot) root;
				final String conf = mch.getConfiguration();	
				
				if(!conf.contains(QUAL_PROB_CONFIG)) {
					mch.setConfiguration(conf + ";" + QUAL_PROB_CONFIG, null);
				}
			}
		}
	}

}
