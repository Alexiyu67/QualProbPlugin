package doubleinequalityreasoner;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eventb.core.ast.Expression;
import org.eventb.core.ast.Formula;
import org.eventb.core.ast.FormulaFactory;
import org.eventb.core.ast.Predicate;
import org.eventb.core.ast.RelationalPredicate;
import org.eventb.core.seqprover.IProofMonitor;
import org.eventb.core.seqprover.IProofRule.IAntecedent;
import org.eventb.core.seqprover.IProverSequent;
import org.eventb.core.seqprover.IReasonerInput;
import org.eventb.core.seqprover.IReasonerOutput;
import org.eventb.core.seqprover.IVersionedReasoner;
import org.eventb.core.seqprover.ProverFactory;
import org.eventb.core.seqprover.IHypAction.ISelectionHypAction;
import org.eventb.core.seqprover.reasonerInputs.SinglePredInput;
import org.eventb.core.seqprover.reasonerInputs.SinglePredInputReasoner;

public class DoubleInequalityReasoner extends SinglePredInputReasoner implements IVersionedReasoner{
	
	private static final String REASONER_ID = "org.eventb.doubleInequality.dblIneq";
	private static final int VERSION = 0;

	@Override
	public IReasonerOutput apply(IProverSequent seq, IReasonerInput input, IProofMonitor pm) {
		if(input.hasError()) {
			return ProverFactory.reasonerFailure(this, input, input.getError());
		}
		final SinglePredInput predInput = (SinglePredInput) input;
		final Predicate predicate = predInput.getPredicate();
		if(predicate.getTag() != Formula.LE) {
			return ProverFactory.reasonerFailure(this, input, "invalid input predicate");
		}
		final RelationalPredicate aLEb = (RelationalPredicate) predicate;
		final Expression a = aLEb.getLeft();
		final Expression b = aLEb.getRight();
		final FormulaFactory ff = seq.getFormulaFactory();
		final Predicate bLEa = ff.makeRelationalPredicate(Formula.LE, b, a, null);
		if(!seq.containsHypotheses(Arrays.asList(aLEb, bLEa))) {
			return ProverFactory.reasonerFailure(this, input, "sequent does not contain required hypothesis");
		}
		final Predicate aEqb = ff.makeRelationalPredicate(Formula.EQUAL, a, b, null);
		//added hypothesis a = b
		final Set<Predicate> addedHyps = Collections.singleton(aEqb);
		final ISelectionHypAction hideIneqs = ProverFactory.makeHideHypAction(Arrays.asList(aLEb, bLEa));
		//we create our antecedent to be used in our proof rule
		final IAntecedent antecedent = ProverFactory.makeAntecedent(null, addedHyps, hideIneqs);
		final Set<Predicate> neededHyps = new HashSet<Predicate>();
		neededHyps.addAll(Arrays.asList(aLEb, bLEa));
		
		return ProverFactory.makeProofRule(this, input, null, neededHyps, "Inequality Rewrites", antecedent);
	}

	@Override
	public int getVersion() {
		return VERSION;
	}

	@Override
	public String getReasonerID() {
		return REASONER_ID;
	}

}
