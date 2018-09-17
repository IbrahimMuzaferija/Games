package game;

import agent.Agent;
import java.util.concurrent.Semaphore;
import javax.swing.SwingWorker;
import common.Action;

public class GameManager implements StateListener {

    public static final int MINIMAX = 1;
    public static final int ALPHA_BETA = 2;
    private GameState currentState = new GameState();
    private Agent crossesXPTO;
    private Agent circlesXPTO;
    private boolean xptoIsPlaying;
    private boolean gameOngoing = false;
    private Semaphore upperPlayerSemaphore;
    private Semaphore downPlayerSemaphore;
    private GameManagerGUI gui;

    public GameManager(GameManagerGUI gui) {
        this.gui = gui;
        currentState.addGameListener(this);
    }

    public void createCrossesXPTO(int depthLimit, int algorithm) {
        crossesXPTO = new Agent('X');
        configureAgent(crossesXPTO, depthLimit, algorithm);
    }

    public void createCirclesXPTO(int depthLimit, int algorithm) {
        circlesXPTO = new Agent('O');
        configureAgent(circlesXPTO, depthLimit, algorithm);
    }

    public void startGame() {
        upperPlayerSemaphore = new Semaphore(1); // first player to play
        downPlayerSemaphore = new Semaphore(0); // second player to play
        currentState.restart();

        if (crossesXPTO != null) {
            crossesXPTO.notifyNewGame();
            crossesPlayerMoves();
        }

        if (circlesXPTO != null) {
            circlesXPTO.notifyNewGame();
            circlesPlayerMoves();
        }

        gameOngoing = true;
    }

    private void crossesPlayerMoves() {
        SwingWorker worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    while (!currentState.isEndOfGameState()) {
                        upperPlayerSemaphore.acquire();
                        xptoIsPlaying = true;

                        if (crossesXPTO != null) {
                            Action action = crossesXPTO.play();
                            if (currentState.placePiece(action.getLine(), action.getColumn())) {
                                //We must do the following test because the game may end when we place a piece.
                                //When the game ends, xptos are nulled (see stateChanged).
                                if (crossesXPTO != null) {
                                    crossesXPTO.notifyAction(action);
                                }
                                if (circlesXPTO != null) {
                                    circlesXPTO.notifyAction(action);
                                }
                            }
                        }
                        xptoIsPlaying = false;
                        downPlayerSemaphore.release();
                    }
                    gameOngoing = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        worker.execute();
    }

    private void circlesPlayerMoves() {
        SwingWorker worker = new SwingWorker<Void, Void>() {
            public Void doInBackground() {
                try {
                    while (!currentState.isEndOfGameState()) {
                        downPlayerSemaphore.acquire();
                        xptoIsPlaying = true;

                        if (circlesXPTO != null) {
                            Action action = circlesXPTO.play();
                            if (currentState.placePiece(action.getLine(), action.getColumn())) {
                                //We must do the following test because the game may end when we place a piece.
                                //When the game ends, xptos are nulled (see stateChanged).                                
                                if (circlesXPTO != null) {
                                    circlesXPTO.notifyAction(action);
                                }
                                if (crossesXPTO != null) {
                                    crossesXPTO.notifyAction(action);
                                }
                            }
                        }
                        xptoIsPlaying = false;
                        upperPlayerSemaphore.release();
                    }
                    gameOngoing = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        worker.execute();
    }

    public void humanMove(int line, int column) {
        if (!xptoIsPlaying && !currentState.isEndOfGameState() && currentState.isValidAction(line, column)) {
            currentState.placePiece(line, column);
            if (crossesXPTO != null) {
                crossesXPTO.notifyAction(new Action(line, column));
                upperPlayerSemaphore.release();
            } else if (circlesXPTO != null) {
                circlesXPTO.notifyAction(new Action(line, column));
                downPlayerSemaphore.release();
            }
        }
    }

    public void stateChanged(StateEvent pe) {
        if (currentState.isEndOfGameState()) {
            gui.showWinner(currentState.getWinner());
            crossesXPTO = circlesXPTO = null;
            gameOngoing = false;
        }
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public boolean isGameOngoing() {
        return gameOngoing;
    }

    private void configureAgent(Agent agent, int depthLimit, int algorithm) {
        if (algorithm == MINIMAX) {
            agent.useMinimax();
        } else {
            agent.useAlfabeta();
        }
        agent.setLimitDepth(depthLimit);
    }
}