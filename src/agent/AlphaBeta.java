package agent;

import common.Action;

public class AlphaBeta extends GameAlgorithm {
    
    //ALFABETA-DECISION(state) returns an action
    //    max = -∞
    //    For each a from ACTIONS(state) do
    //        v = MINVALUE(RESULT(state, a), max, +∞, 1)
    //        If (v > max) max = v; action = a
    //    returns action
    
    //MAXVALUE(state, alpha, beta, depth) returns the minimax value of state
    //    If TERMINAL-TEST(state, depth) returns EVALUATION_FUNCTION(state)
    //    v = -∞
    //    For each a from ACTIONS(state) do
    //        v = max(v, MINVALUE(RESULT(state, a), alpha, beta, depth + 1))
    //        If v >= beta then returns v
    //        If v > alpha then alpha=v
    //    returns v
    
    //VALOR-MIN(state, alpha, beta, depth) returns the minimax value of state
    //    If TERMINAL-TEST(state, depth) returns EVALUATION_FUNCTION(state)
    //    v = +∞
    //    For each a from ACTIONS(state) do
    //        v = min(v, MAXVALUE(RESULT(state, a), alpha, beta, depth + 1))
    //        If v <= alpha then returns v
    //        If v < beta then beta=v
    //    returns v
    
    public Action takeDecision(AgentState currentState) {
        double actionValue, max = Double.NEGATIVE_INFINITY;
        Action nextAction = null;
        for (Action a : currentState.getActions()) {
            actionValue = minValue(currentState.result(a), max, Double.POSITIVE_INFINITY, 1);
            if (nextAction == null || actionValue > max) {
                max = actionValue;
                nextAction = a;
            }            
        }        
        return nextAction;        
    }

    private double maxValue(AgentState state, double alpha, double beta, int depth) {
        if (depth == depthLimit || state.isEndOfGameState()) {
            return state.evaluate();
        }
        double value = Double.NEGATIVE_INFINITY;
        for (Action a : state.getActions()) {
            value = Math.max(value, minValue(state.result(a), alpha, beta, depth + 1));
            
            if (value >= beta) {
                return value;
            }
            if (value > alpha) {
                alpha=value;
            }
        }
        return value;

    }
    private double minValue(AgentState state, double alpha, double beta, int depth) {
        if (depth == depthLimit || state.isEndOfGameState()) {
            return state.evaluate();
        }

        double value = Double.POSITIVE_INFINITY;
        for (Action a : state.getActions()) {
            value = Math.min(value, maxValue(state.result(a), alpha, beta, depth + 1));

            if (value <= alpha) {
                return value;
            }
            if (value < beta) {
                beta=value;
            }
        }
        return value;
    }  
}
