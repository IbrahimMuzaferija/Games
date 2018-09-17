package agent;

import common.Action;

public class Minimax extends GameAlgorithm {

    //MINIMAX-DECISION(state) returns an action
    //	max = -∞
    //	For each a from ACTIONS(state) do
    //      v = MIN-VALUE(RESULT(state, a), 1)
    //      If (v > max) max = v; action = a
    //	returns action
    
    //MAX-VALUE(state, depth) returns the minimax value of state
    // 	If TERMINAL-TEST(state, depth) returns EVALUATION-FUNCION(state)
    //	v = -∞
    //	For each a from ACTIONS(state) do
    //      v = max(v, MIN-VALUE(RESULT(state, a), depth + 1))
    //	returns v
    
    //MIN-VALUE(state, depth) returns the minimax value of state
    // 	If TERMINAL-TEST(state, depth) returns EVALUATION-FUNCION(state)
    //	v = +∞
    //	For each a from ACTIONS(state) do
    //      v = min(v, MAX-VALUE(RESULT(state, a), depth + 1))
    //	returns v    
    
    public Action takeDecision(AgentState currentState) {
        double actionValue, max = Double.NEGATIVE_INFINITY;
        Action nextAction = null;        
        for (Action a : currentState.getActions()) {
            actionValue = minValue(currentState.result(a), 1);
            if (nextAction == null || actionValue > max) {
                max = actionValue;
                nextAction = a;
            }            
        }        
        return nextAction;
    }

    private double maxValue(AgentState state, int depth) {
        if (depth == depthLimit || state.isEndOfGameState()) {
            return state.evaluate();
        }

        double max = Double.NEGATIVE_INFINITY;

        for (Action a : state.getActions()) {
            max = Math.max(max, minValue(state.result(a), depth + 1));
        }
        return max;
    }

    private double minValue(AgentState state, int depth) {
        if (depth == depthLimit || state.isEndOfGameState()) {
            return state.evaluate();
        }

        double min = Double.POSITIVE_INFINITY;

        for (Action a : state.getActions()) {
            min = Math.min(min, maxValue(state.result(a), depth + 1));
        }
        return min;
    }
}
