package fr.systerel.rodinextension.sample.basis;

import org.eventb.core.basis.EventBElement;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IInternalElementType;
import org.rodinp.core.IRodinElement;

public class Bound extends EventBElement implements IBound{

	public Bound(String name, IRodinElement parent) {
		super(name, parent);
		//Empty Constructor
	}

	@Override
	public IInternalElementType<? extends IInternalElement> getElementType() {
		return IBound.ELEMENT_TYPE;
	}

}
