package agent;

import common.Action;

public class Agent{

    private char agentPiece;
    private Minimax minimax;
    private AlphaBeta alfabeta;
    private GameAlgorithm algorithm;
    private AgentState currentState;   

    public Agent(char piece) {
        agentPiece = piece;
        minimax = new Minimax();
        alfabeta = new AlphaBeta();
        algorithm = alfabeta;
    }

    public void notifyNewGame() {
        currentState = new AgentState(agentPiece);
    }

    public Action play() {
        return algorithm.takeDecision(currentState);
    }

    public void notifyAction(Action action) {
        currentState.placePiece(action.getLine(), action.getColumn());
    }

    public void useMinimax() {
        algorithm = minimax;
    }

    public void useAlfabeta() {
        algorithm = alfabeta;
    }

    public void setLimitDepth(int limitDepth) {
        algorithm.setDepthLimit(limitDepth);
    }
    
    public AgentState getCurrentState(){
        return currentState;
    }
}
