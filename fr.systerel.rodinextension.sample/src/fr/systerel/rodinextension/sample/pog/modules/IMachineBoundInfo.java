package fr.systerel.rodinextension.sample.pog.modules;

import org.eventb.core.ast.Expression;
import org.eventb.core.pog.POGCore;
import org.eventb.core.pog.state.IPOGState;
import org.eventb.core.tool.IStateType;

import fr.systerel.rodinextension.sample.QualProbPlugin;
import fr.systerel.rodinextension.sample.basis.ISCBound;

public interface IMachineBoundInfo extends IPOGState {
	final static IStateType<IMachineBoundInfo> STATE_TYPE = 
	         POGCore.getToolStateType(QualProbPlugin.PLUGIN_ID + ".machineBoundInfo");
		
		/**
		 * Returns the parsed and type-checked bound expression, or null 
		 * if the machine does not have a bound.
		 * 
		 * @return the parsed and type-checked bound expression, or null 
		 * 		if the machine does not have a bound
		 */
		Expression getExpression();
		
		/**
		 * Returns a handle to the bound, or null if the machine does not have a bound.
		 * 
		 * @return a handle to the bound, or null if the machine does not have a bound
		 */
		ISCBound getBound();
		
		/**
		 * Returns whether the machine has a bound.
		 * 
		 * @return whether the machine has a bound
		 */
		boolean machineHasBound();

}
