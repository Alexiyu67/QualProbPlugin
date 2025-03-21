package fr.systerel.rodinextension.sample;

import org.rodinp.core.IInternalElement;
import org.rodinp.core.IInternalElementType;
import org.rodinp.core.IRodinElement;

import fr.systerel.rodinextension.sample.basis.ISCBound;

import org.eventb.core.basis.SCExpressionElement;

public class SCBound extends SCExpressionElement implements ISCBound {

	
	public SCBound(String name, IRodinElement parent) {
		super(name, parent);
	}

	
	@Override
	public IInternalElementType<? extends IInternalElement> getElementType() {
		return ELEMENT_TYPE;
	}

}
