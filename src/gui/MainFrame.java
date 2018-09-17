package gui;

import game.GameManager;
import game.GameManagerGUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class MainFrame extends JFrame implements GameManagerGUI{

    private GameManager gameManager;
    private JTextField crossesDepth = new JTextField("1000");
    private JTable gameTable = new JTable();
    private JRadioButton humanWithCrosses = new JRadioButton("Human player");
    private JRadioButton xptoWithCrosses = new JRadioButton("XPTO");
    private String[] algorithmsNames = {"Minimax", "Alfa-beta"};
    private JComboBox crossesAlgorithm = new JComboBox(algorithmsNames);
    private JButton newGameButton = new JButton("New Game");
    private JTextField circlesDepth = new JTextField("1000");
    private JRadioButton humanWhithCircles = new JRadioButton("Human player");
    private JRadioButton xptoWithCircles = new JRadioButton("XPTO");
    private JComboBox circlesAlgorithm = new JComboBox(algorithmsNames);

    public MainFrame() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("TicTacToe");

        JPanel contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel configurationPanel = new JPanel();
        configurationPanel.setLayout(new BoxLayout(configurationPanel, BoxLayout.Y_AXIS));

        ButtonGroup crossesGroup = new ButtonGroup();
        crossesGroup.add(humanWithCrosses);
        crossesGroup.add(xptoWithCrosses);
        JPanel radioButtonsPanel = new JPanel(new GridLayout(4, 1));
        radioButtonsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Crosses (plays first)"),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        radioButtonsPanel.add(humanWithCrosses);
        radioButtonsPanel.add(xptoWithCrosses);
        humanWithCrosses.setSelected(true);
        configurationPanel.add(radioButtonsPanel);

        JPanel algorithmPanel = new JPanel(new FlowLayout());
        algorithmPanel.add(new JLabel("XPTO algorithm: "));
        algorithmPanel.add(crossesAlgorithm);
        crossesAlgorithm.setSelectedIndex(0);
        radioButtonsPanel.add(algorithmPanel);

        JPanel depthPanel = new JPanel(new FlowLayout());
        depthPanel.add(new JLabel("Max. search depth: "));
        crossesDepth.setColumns(4);
        depthPanel.add(crossesDepth);
        radioButtonsPanel.add(depthPanel);

        ButtonGroup circlesGroup = new ButtonGroup();
        circlesGroup.add(humanWhithCircles);
        circlesGroup.add(xptoWithCircles);
        JPanel radioButtonsPanel2 = new JPanel(new GridLayout(4, 1));
        radioButtonsPanel2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Circles"),
                BorderFactory.createEmptyBorder(1, 1, 1, 1)));
        radioButtonsPanel2.add(humanWhithCircles);
        radioButtonsPanel2.add(xptoWithCircles);
        humanWhithCircles.setSelected(true);
        configurationPanel.add(radioButtonsPanel2);
        circlesGroup.setSelected(xptoWithCircles.getModel(), true);

        JPanel algorithmPanel2 = new JPanel(new FlowLayout());
        algorithmPanel2.add(new JLabel("XPTO algorithm: "));
        algorithmPanel2.add(circlesAlgorithm);
        circlesAlgorithm.setSelectedIndex(0);
        radioButtonsPanel2.add(algorithmPanel2);

        JPanel depthPanel2 = new JPanel(new FlowLayout());
        depthPanel2.add(new JLabel("Max. search depth: "));
        circlesDepth.setColumns(4);
        depthPanel2.add(circlesDepth);
        radioButtonsPanel2.add(depthPanel2);

        newGameButton.addActionListener(new NewGameButton_actionAdapter(this));
        configurationPanel.add(newGameButton);

        JPanel globalPanel = new JPanel(new FlowLayout());
        globalPanel.add(configurationPanel);
        globalPanel.add(gameTable);
        contentPane.add(globalPanel);
        
        gameManager = new GameManager(this);
        gameTable.addMouseListener(new GameTable_mouseAdapter(this));
        tableConfiguration(gameTable);

        pack();
    }

    private void tableConfiguration(JTable table) {
        gameTable.setModel(new TableModel(gameManager.getCurrentState()));
        table.setDefaultRenderer(Object.class, new CellRenderer());
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(Properties.CELL_WIDTH);
        }
        table.setRowHeight(Properties.CELL_HEIGHT);
        table.setBorder(BorderFactory.createLineBorder(Color.black));
        table.setIntercellSpacing(new Dimension(1, 1));

        table.setShowGrid(true);
    }

    public void jNewGameButton_actionPerformed(ActionEvent e) {
        if (xptoWithCrosses.isSelected()) {
            gameManager.createCrossesXPTO(
                    Integer.parseInt(crossesDepth.getText()),
                    (crossesAlgorithm.getSelectedIndex() == 0) ? GameManager.MINIMAX : GameManager.ALPHA_BETA);
        }

        if (xptoWithCircles.isSelected()) {
            gameManager.createCirclesXPTO(
                    Integer.parseInt(circlesDepth.getText()),
                    (circlesAlgorithm.getSelectedIndex() == 0) ? GameManager.MINIMAX : GameManager.ALPHA_BETA);
        }

        newGameButton.setEnabled(false);        
        gameManager.startGame();
    }

    //Human move
    void gameTable_mouseClicked(MouseEvent e) {
        if (gameManager.isGameOngoing()) {
            try {
                int line = gameTable.rowAtPoint(e.getPoint());
                int column = gameTable.columnAtPoint(e.getPoint());
                gameManager.humanMove(line, column);
            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void showWinner(String winner) {
        if (winner.equals("Draw")) {
            JOptionPane.showMessageDialog(this, "Draw!", "Game End", JOptionPane.INFORMATION_MESSAGE);
        } else if (winner.equals("Crosses") && xptoWithCrosses.isSelected()
                || winner.equals("Circles") && xptoWithCircles.isSelected()) {
            JOptionPane.showMessageDialog(this, winner + " wins!", "Game End", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Congratulations, you win!", "Game End", JOptionPane.INFORMATION_MESSAGE);
        }
        newGameButton.setEnabled(true);        
    }
}

class NewGameButton_actionAdapter implements ActionListener {

    private MainFrame adaptee;

    NewGameButton_actionAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jNewGameButton_actionPerformed(e);
    }
}

class GameTable_mouseAdapter extends java.awt.event.MouseAdapter {

    private MainFrame adaptee;

    GameTable_mouseAdapter(MainFrame adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        adaptee.gameTable_mouseClicked(e);
    }
}
