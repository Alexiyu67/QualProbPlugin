package fr.systerel.rodinextension.sample.pog.modules;

import org.eventb.core.ast.Expression;
import org.eventb.core.tool.IStateType;

import fr.systerel.rodinextension.sample.basis.ISCBound;

public class MachineBoundInfo implements IMachineBoundInfo{

		private final Expression boundExpression;
		private final ISCBound bound;
	 	private boolean immutable;

		/**
		 * Constructor
		 */
		public MachineBoundInfo(final Expression expression, final ISCBound bound) {
			this.boundExpression = expression;
			this.bound = bound;
			immutable = false;
		}
		
		/**
		 * Constructor with no bound attached
		 */
		public MachineBoundInfo() {
			this.boundExpression = null;
			this.bound = null;
			immutable = false;
		}

		@Override
		public String toString() {
			return boundExpression == null ? "null" : boundExpression.toString();
		}
	 	
		public Expression getExpression() {
			return boundExpression;
		}

		public ISCBound getBound() {
			return bound;
		}

		public IStateType<?> getStateType() {
			return IMachineBoundInfo.STATE_TYPE;
		}

		public boolean machineHasBound() {
			return boundExpression != null;
		}

		@Override
		public void makeImmutable() {
			immutable = true;
		}

		@Override
		public boolean isImmutable() {
			return immutable;
		}

}
