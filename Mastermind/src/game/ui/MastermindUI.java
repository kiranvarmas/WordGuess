package game.ui;

import game.Mastermind;

import javax.swing.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MastermindUI extends Canvas {

    private static final int ROWS = 20;
    private int cellWidth, cellHeight, boardCellWidth;
    int userChoice = 0;
    int trial = 1;
    public Mastermind mastermind = new Mastermind();
    private List<Color> colors = mastermind.getAvailableColors();
    JButton submitButton = new JButton("Submit");
    JButton resetButton = new JButton("Reset");
    public Map<Mastermind.Response, Long> results = new HashMap<>();
    ArrayList<Color> userColors = new ArrayList<Color>();
    JFrame frame;


    public void paint(Graphics g) {

        cellWidth = 30;
        cellHeight = 20;
        boardCellWidth = 20;

        for (int i = 0; i < ROWS + 1; i++)
            g.drawLine(cellWidth, cellHeight * (i + 1), 7 * cellWidth, cellHeight * (i + 1));

        for (int i = 0; i < 7; i++)
            g.drawLine(cellWidth * (i + 1), cellHeight, cellWidth * (i + 1), (ROWS + 1) * cellHeight);

        for (int i = 0; i < ROWS + 1; i++)
            g.drawLine(cellWidth * 8, cellHeight * (i + 1), cellWidth * 12, (i + 1) * cellHeight);

        for (int i = 0; i < 7; i++)
            g.drawLine(cellWidth * 8 + boardCellWidth * i, cellHeight, cellWidth * 8 + boardCellWidth * i, cellHeight * (ROWS + 1));

        for (int i = 0; i < 10; i++) {
            g.setColor(colors.get(i));
            g.fillOval(i * cellWidth + cellWidth, cellHeight * (ROWS + 1) + cellHeight / 2, 30, 30);
        }

    }

    public MastermindUI() {
        frame = new JFrame("Mastermind Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.white);
        frame.getContentPane().add(this);
        frame.setSize(390, 580);
        frame.setResizable(false);
        resetButton.setBounds(40, 460, 100, 50);
        frame.getContentPane().add(submitButton, BorderLayout.SOUTH);
        frame.getContentPane().add(resetButton, BorderLayout.NORTH);
        frame.setVisible(true);
        MastermindMouseClicked mouseHandler = new MastermindMouseClicked();
        addMouseListener(mouseHandler);
        submitButton.setEnabled(false);
        submitButton.addActionListener(new submitButtonActionListener());
        resetButton.addActionListener(new resetButtonActionListener());
    }


    private class submitButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            results = mastermind.guess(userColors);
            if (mastermind.getGameStatus() == Mastermind.GameStatus.PLAYING) {
                userChoice = 0;
                resetResults();
                trial++;

            } else if (mastermind.getGameStatus() == Mastermind.GameStatus.WON) {

                JOptionPane.showMessageDialog(frame, "You Won!");

            } else if (mastermind.getGameStatus() == Mastermind.GameStatus.LOST) {

                JOptionPane.showMessageDialog(frame, "You Lost!");

            }
        }
    }

    private void resetResults() {

        Graphics graphics = getGraphics();
        fillBoard(graphics, results);
        userColors.clear();
        results.put(Mastermind.Response.POSITIONAL_MATCH, 0L);
        results.put(Mastermind.Response.MATCH, 0L);
        results.put(Mastermind.Response.NO_MATCH, 0L);
        submitButton.setEnabled(false);

    }

    private void fillBoard(Graphics graphics, Map<Mastermind.Response, Long> results) {

        long positional_match = results.get(Mastermind.Response.POSITIONAL_MATCH);
        long match = results.get(Mastermind.Response.MATCH);
        System.out.println("White" + results.get(Mastermind.Response.NO_MATCH));
        System.out.println("Silver" + results.get(Mastermind.Response.MATCH));
        System.out.println("Black" + results.get(Mastermind.Response.POSITIONAL_MATCH));

        for (int i = 1; i <= positional_match; i++) {

            graphics.setColor(Color.BLACK);
            graphics.fillOval((cellWidth * 8 + (20 * (i - 1))), cellHeight * trial, boardCellWidth, cellHeight);

        }

        for (int j = (int) positional_match + 1; j <= (match + positional_match); j++) {

            graphics.setColor(Color.LIGHT_GRAY);
            graphics.fillOval(cellWidth * 8 + (20 * (j - 1)), (cellHeight * trial), boardCellWidth, cellHeight);

        }
    }

    public static void main(String[] args) {
        new MastermindUI();
    }

    class MastermindMouseClicked extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            if (y < 462 && y > 430) {
                x = (x - cellWidth) / cellWidth;
                Graphics g = getGraphics();
                g.setColor(colors.get(x));
                if (userChoice < 6) {
                    g.fillOval(cellWidth * (userChoice + 1), (trial) * cellHeight, cellWidth, cellHeight);
                    userColors.add(colors.get(x));
                    userChoice++;
                    if (userChoice == 6) {
                        submitButton.setEnabled(true);
                    }
                }
            }
        }
    }

    private class resetButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            frame.dispose();
            new MastermindUI();

        }
    }
}


