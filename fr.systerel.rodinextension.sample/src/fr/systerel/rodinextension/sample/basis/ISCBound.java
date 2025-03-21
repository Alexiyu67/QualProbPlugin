package fr.systerel.rodinextension.sample.basis;

import org.eventb.core.ISCExpressionElement;
import org.eventb.core.ITraceableElement;
import org.rodinp.core.IInternalElementType;
import org.rodinp.core.RodinCore;

import fr.systerel.rodinextension.sample.QualProbPlugin;

public interface ISCBound extends ISCExpressionElement, ITraceableElement {
	
	IInternalElementType<ISCBound> ELEMENT_TYPE =
		RodinCore.getInternalElementType(QualProbPlugin.PLUGIN_ID + ".scBound");
}
