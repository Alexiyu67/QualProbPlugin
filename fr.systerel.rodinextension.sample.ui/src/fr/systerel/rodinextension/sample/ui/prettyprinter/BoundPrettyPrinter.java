package fr.systerel.rodinextension.sample.ui.prettyprinter;

import static org.eventb.ui.prettyprint.PrettyPrintUtils.getHTMLBeginForCSSClass;
import static org.eventb.ui.prettyprint.PrettyPrintUtils.getHTMLEndForCSSClass;
import static org.eventb.ui.prettyprint.PrettyPrintUtils.wrapString;

import org.eventb.ui.prettyprint.DefaultPrettyPrinter;
import org.eventb.ui.prettyprint.IPrettyPrintStream;
import org.eventb.ui.prettyprint.PrettyPrintAlignments.HorizontalAlignment;
import org.eventb.ui.prettyprint.PrettyPrintAlignments.VerticalAlignement;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.RodinDBException;

import fr.systerel.rodinextension.sample.basis.IBound;

public class BoundPrettyPrinter extends DefaultPrettyPrinter {

	private static final String BOUND_EXPRESSION = "variantExpression";
	private static final String BOUND_EXPRESSION_SEPARATOR_BEGIN = null;
	private static final String BOUND_EXPRESSION_SEPARATOR_END = null;

	@Override
	public void prettyPrint(IInternalElement elt, IInternalElement parent, IPrettyPrintStream ps) {
		if (elt instanceof IBound) {
			IBound bound = (IBound) elt;
			try {
				appendBoundExpression(ps,wrapString(bound.getExpressionString()));
			} catch (RodinDBException e) {
				System.err.println("Cannot get the expression string for the bound element."+ e.getMessage());
			}
		}
	}
	
	private static void appendBoundExpression(IPrettyPrintStream ps, String expression) {
	    ps.appendString(expression, //
	      getHTMLBeginForCSSClass(BOUND_EXPRESSION, //
	                              HorizontalAlignment.LEFT, //
	                              VerticalAlignement.MIDDLE),//
	      getHTMLEndForCSSClass(BOUND_EXPRESSION, //
	                              HorizontalAlignment.LEFT, //
	                              VerticalAlignement.MIDDLE),//
	                              BOUND_EXPRESSION_SEPARATOR_BEGIN, //
	                              BOUND_EXPRESSION_SEPARATOR_END);
	}

}
