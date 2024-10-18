package org.example;

import java.util.Scanner;

/**
 * Acts as an input translator between the packet receiver and Connect 4 game
 */
public class InputLayer {
    /**
     * Current player turn index
     */
    int turn = 0;
    /**
     * The list of players and order of play
     */
    char[] players;
    /**
     * The board is stored here
     */
    Board board;

    /**
     * Construct a new instance of Connect 4
     * @param width The width of the board
     * @param height The height of the board
     * @param players The list of players and what piece they are
     */
    public InputLayer(int width, int height, char[] players) {
        board = new Board(width, height);
        //board.printBoard();
        this.players = players;
    }

    /**
     * Get the current player
     * @return The letter of the current player
     */
    public char getTurn(){
        return players[turn];
    }

    /**
     * Get the player that just played
     * @return The letter of the previous player
     */
    public char getPreviousTurn(){
        int previousTurn = turn-1;
        if (previousTurn < 0){
            previousTurn = players.length - 1;
        }
        return players[previousTurn];
    }

    /**
     * Move the Connect 4 game forward a move
     */
    public void nextTurn(){
        turn += 1;
        if (turn == players.length) turn = 0;
        //board.printBoard();
    }

    /**
     * Check a move to see if it's valid
     * @param column The column to check
     * @return If the move is valid or not
     */
    public boolean checkMove(int column){
        return board.checkBoard(column);
    }

    /**
     * Play a move on the Connect 4 board
     * @param column The column to play in
     * @return If a victory has been achieved
     */
    public boolean play(int column){
        boolean result = board.playMove(column, getTurn());
        nextTurn();
        return result;
    }

    /**
     * Used for running this Connect 4 in a non-server environment.
     * Meant entirely for debugging the Connect 4 logic
     * @param args Not used
     */
    public static void main(String[] args) {
        Board board = new Board(10, 10);
        Scanner console = new Scanner(System.in);
        char[] players = new char[]{'R', 'Y'};
        int turn = 0;
        while (true){
            board.printBoard();
            System.out.println("It's " + players[turn] + "'s Turn, Choose Column To Play (1-" + board.width + ")");
            int column = console.nextInt();
            if (column < 1 || column > board.width){
                System.out.println("Column out of bounds");
                continue;
            }
            column-=1;
            if (!board.checkBoard(column)){
                System.out.println("Column is full");
                continue;
            }
            if (board.playMove(column, players[turn])){
                System.out.println(players[turn] + " WINS!");
                System.exit(0);
            }
            turn += 1;
            if (turn == players.length) turn = 0;
        }
    }
}
