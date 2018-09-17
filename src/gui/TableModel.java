package gui;

import game.GameState;
import game.StateListener;
import game.StateEvent;
import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel implements StateListener{

    private GameState gamesState;

    public TableModel(GameState gameState) {
        if(gameState == null)
            throw new NullPointerException("State cannot be null");
        this.gamesState = gameState;
        this.gamesState.addGameListener(this);
    }

    public int getColumnCount() {
        return gamesState.getDimension();
    }

    public int getRowCount() {
        return gamesState.getDimension();
    }

    public Object getValueAt(int row, int col) {
        return new Character(gamesState.getPieceValue(row, col));
    }

    public void stateChanged(StateEvent pe){
        fireTableDataChanged();
    }

    public void setState(GameState gameState){
        if(gameState == null)
          throw new NullPointerException("State cannot be null");
        this.gamesState.removeGameListener(this);
        this.gamesState = gameState;
        this.gamesState.addGameListener(this);
        fireTableDataChanged();
    }
}
