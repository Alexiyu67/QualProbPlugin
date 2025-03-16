package fr.systerel.rodinextension.sample.basis;

import org.eventb.core.ICommentedElement;
import org.eventb.core.IExpressionElement;
import org.rodinp.core.IInternalElementType;
import org.rodinp.core.RodinCore;

import fr.systerel.rodinextension.sample.QualProbPlugin;

public interface IBound extends ICommentedElement, IExpressionElement {
	IInternalElementType<IBound> ELEMENT_TYPE = RodinCore
			.getInternalElementType(QualProbPlugin.PLUGIN_ID + ".bound");
}
