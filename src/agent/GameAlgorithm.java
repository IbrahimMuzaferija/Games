package agent;

import common.Action;

public abstract class GameAlgorithm {

    protected int depthLimit;
    
    public abstract Action takeDecision(AgentState currentState);
    
    public void setDepthLimit(int depthLimit){
        this.depthLimit = depthLimit;
    }
}
