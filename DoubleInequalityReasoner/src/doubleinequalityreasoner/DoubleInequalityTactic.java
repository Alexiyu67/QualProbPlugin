package doubleinequalityreasoner;

import org.eventb.core.ast.Expression;
import org.eventb.core.ast.Formula;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.RelationalPredicate;
import org.eventb.core.seqprover.IProofMonitor;
import org.eventb.core.seqprover.IProofTreeNode;
import org.eventb.core.seqprover.IProverSequent;
import org.eventb.core.seqprover.ITactic;
import org.eventb.core.seqprover.reasonerInputs.SinglePredInput;
import org.eventb.core.seqprover.tactics.BasicTactics;

public class DoubleInequalityTactic implements ITactic {

	public DoubleInequalityTactic() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object apply(IProofTreeNode ptNode, IProofMonitor pm) {
		final IProverSequent sequent = ptNode.getSequent();
		final FormulaFactory ff = sequent.getFormulaFactory();
		for(Predicate hyp: sequent.selectedHypIterable()) {
			if(hyp.getTag() == Formula.LE) {
				final RelationalPredicate aLEb = (RelationalPredicate) hyp;
				final Expression a = aLEb.getLeft();
				final Expression b = aLEb.getRight();
				final RelationalPredicate bLEa = ff.makeRelationalPredicate(Formula.LE, b, a, null);
				if(sequent.containsHypothesis(bLEa)) {
					return callDblIneqReasoner(ptNode, aLEb, pm);
				}
			}
		}
		return "no double inequality hypotheses found";
	}
	
	private Object callDblIneqReasoner(IProofTreeNode ptNode, final RelationalPredicate inputPred, IProofMonitor pm) {
		final ITactic dblIneqTac = BasicTactics.reasonerTac(new DoubleInequalityReasoner(), new SinglePredInput(inputPred));
		return dblIneqTac.apply(ptNode, pm);
	}


}
