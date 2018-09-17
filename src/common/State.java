package common;

public class State {

    public static final int DIMENSION = 3;
    public static final char EMPTY = ' ';
    public static final char X = 'X';
    public static final char O = 'O';
    protected char[][] board;
    protected int numPieces;
    protected char nextPiece;

    public State() {
        board = new char[DIMENSION][DIMENSION];
        initialize();
    }

    public void restart() {
        initialize();
    }

    public boolean placePiece(int line, int column) {
        if (!isValidAction(line, column)) {
            return false;
        }
        board[line][column] = nextPiece;
        nextPiece = (nextPiece == X) ? O : X;        
        numPieces++;
        return true;
    }

    public boolean isWinner(char piece) {
        int piecesInLine, piecesInColu, piecesInPD = 0, piecesInSD = 0;
        for (int i = 0; i < board.length; i++) {
            piecesInLine =  piecesInColu = 0;
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] == piece) piecesInLine++;
                if(board[j][i] == piece) piecesInColu++;
                if(i == j && board[j][i] == piece) piecesInPD++;
                if(i == DIMENSION - j -1 && board[i][j] == piece) piecesInSD++; 
            }
            if(piecesInLine == DIMENSION) return true;
            if(piecesInColu == DIMENSION) return true;
        }
        return piecesInPD == DIMENSION || piecesInSD == DIMENSION;        
    }

    public boolean isValidAction(int line, int column) {
        return isValidPosition(line, column) && board[line][column] == EMPTY;
    }

    public boolean isEndOfGameState() {
        return numPieces == board.length * board.length || isWinner(X) || isWinner(O);
    }

    public boolean isValidPosition(int line, int column) {
        return line >= 0 && column >= 0 && line < board.length && column < board.length;
    }

    private void initialize() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                this.board[i][j] = EMPTY;
            }
        }
        nextPiece = X;
        numPieces = 0;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                buffer.append(board[i][j] != EMPTY ? board[i][j] : '.');
                buffer.append(' ');
            }
            buffer.append('\n');            
        }
        return buffer.toString();
    }    
}
