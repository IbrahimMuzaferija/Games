package agent;

import java.util.LinkedList;
import java.util.List;
import common.Action;
import common.State;

public class AgentState extends State {

    private char agentPiece;
    private char opponentPiece;

    public AgentState(char agentPiece) {
        this.agentPiece = agentPiece;
        opponentPiece = agentPiece == X ? O : X;
    }

    public List<Action> getActions() {
        List<Action> actions = new LinkedList<Action>();
        for (int i = 0; i < DIMENSION; i++) {
            for (int j = 0; j < DIMENSION; j++) {
                if (isValidAction(i, j)) {
                    actions.add(new Action(i, j));
                }
            }
        }
        return actions;
    }

    //We don´t validate the action because the search algorithm and the state are
    //strongly connected and the validation was already done in method getActions.
    //called previously by the search algorithm.
    public AgentState result(Action action) {
        AgentState sucessor = (AgentState) clone();
        sucessor.placePiece(action.getLine(), action.getColumn());
        return sucessor;
    }

    public double utility() {
        if (isWinner(agentPiece)) {
            return Double.POSITIVE_INFINITY;
        }
        if (isWinner(opponentPiece)) {
            return Double.NEGATIVE_INFINITY;
        }
        return 0; //draw
    }

    public double evaluate() {
        return isEndOfGameState()
                ? utility()
                : getWinningPossibilities(agentPiece) - getWinningPossibilities(opponentPiece);
    }

    private int getWinningPossibilities(char player) {
        char other = player == X ? O : X;
        int possibilities = 0;
        int lineSymbols, coluSymbols, pdSymbols = 0, sdSymbols = 0;
        boolean otherInLine, otherInColu, otherInPD = false, otherInSD = false;

        for (int i = 0; i < board.length; i++) {
            otherInLine = otherInColu = false;
            lineSymbols = coluSymbols = 0;
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == other) {
                    otherInLine = true;
                } else {
                    lineSymbols++;
                }
                if (board[j][i] == other) {
                    otherInColu = true;
                } else {
                    coluSymbols++;
                }
                if (i == j && board[i][j] == other) {
                    otherInPD = true;
                } else {
                    pdSymbols++;
                }
                if (i == DIMENSION - j - 1 && board[i][j] == other) {
                    otherInSD = true;
                } else {
                    sdSymbols++;
                }
            }
            if (!otherInLine) {
                possibilities += lineSymbols;
            }
            if (!otherInColu) {
                possibilities += coluSymbols;
            }
        }
        if (!otherInPD) {
            possibilities += pdSymbols;
        }
        if (!otherInSD) {
            possibilities += sdSymbols;
        }

        return possibilities;
    }

    @Override
    public Object clone() {
        AgentState c = new AgentState(agentPiece);

        c.board = new char[board.length][];
        for (int i = 0; i < board.length; i++) {
            c.board[i] = board[i].clone();
        }
        c.nextPiece = nextPiece;
        c.numPieces = numPieces;
        return c;
    }
}
