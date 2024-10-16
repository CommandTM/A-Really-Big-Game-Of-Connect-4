package org.example;

import java.util.Scanner;

public class InputLayer {
    int turn = 0;
    char[] players;
    Board board;
    public InputLayer(int width, int height, char[] players) {
        board = new Board(width, height);
        board.printBoard();
        this.players = players;
    }

    public char getTurn(){
        return players[turn];
    }

    public void nextTurn(){
        turn += 1;
        if (turn == players.length) turn = 0;
    }

    public boolean checkMove(int column){
        return board.checkBoard(column);
    }

    public boolean play(int column){
        boolean result = board.playMove(column, getTurn());
        nextTurn();
        return result;
    }

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
