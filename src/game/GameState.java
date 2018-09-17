package game;

import java.util.ArrayList;
import common.State;

public class GameState extends State{

    @Override
    public void restart() {
        super.restart();
        fireGameChanged(null);
    }

    @Override
    public boolean placePiece(int line, int column) {
        if (!super.placePiece(line, column)) {
            return false;
        }
        fireGameChanged(null);
        return true;
    }

    public int getDimension() {
        return board.length;
    }

    public char getPieceValue(int line, int column) {
        if (!isValidPosition(line, column)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return board[line][column];
    }
    
    public String getWinner() {
        if (isWinner(X)) {
            return "Crosses";
        }
        if (isWinner(O)) {
            return "Circles";
        }
        return "Draw";
    }
    
    //Listeners
    private transient ArrayList<StateListener> listeners = new ArrayList<StateListener>(3);

    public synchronized void removeGameListener(StateListener l) {
        if (listeners != null && listeners.contains(l)) {
            listeners.remove(l);
        }
    }

    public synchronized void addGameListener(StateListener l) {
        if (!listeners.contains(l)) {
            listeners.add(l);
        }
    }

    public void fireGameChanged(StateEvent pe) {
        for (StateListener listener : listeners) {
            listener.stateChanged(null);
        }
    }
}
