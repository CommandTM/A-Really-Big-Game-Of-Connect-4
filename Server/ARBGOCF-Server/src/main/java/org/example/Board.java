package org.example;

public class Board {
    /**
     * Width of the board
     */
    public int width;
    /**
     * Height of the board
     */
    public int height;
    /**
     * The actual board
     */
    public char[][] board;

    /**
     * Construct a new board of a set height and width
     * @param width Width of the new board
     * @param height Height of the new board
     */
    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        board = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[y][x] = 'o';
            }
        }
    }

    /**
     * Print the board to the console
     */
    public void printBoard() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Check a move to see if it's valid
     * @param column The column to check
     * @return If the move is valid or not
     */
    public boolean checkBoard(int column){
        return board[0][column] == 'o';
    }

    /**
     * plays a move on the board
     * @param column The column to play in
     * @param piece The piece type to play
     * @return If a victory has been achieved
     */
    public boolean playMove(int column, char piece){
        for (int y = 0; y < height+1; y++) {
            if (y == (height) || board[y][column] != 'o') {
                board[y-1][column] = piece;
                return checkWin(column, y-1, piece);
            }
        }
        return false;
    }

    /**
     * Check the board to see if anyone has won
     * @param x The X of the last piece played
     * @param y The Y of the last piece played
     * @param piece The last piece type played
     * @return If a win was found or not
     */
    public boolean checkWin(int x, int y, char piece){
        int vertMatch = 0;
        int horzMatch = 0;
        int rightDiagMatch = 0;
        int leftDiagMatch = 0;
        int xStart = x-3;
        int xEnd = x+3;
        int yStart = y-3;
        int yEnd = y+3;
        if (xStart < 0) xStart = 0;
        if (xEnd > width-1) xEnd = width-1;
        if (yStart < 0) yStart = 0;
        if (yEnd > height-1) yEnd = height-1;
        int intercept = y+x;
        for (int i = yStart; i < yEnd+1; i++){
            for (int k = xStart; k < xEnd+1; k++){
                if (k == x){
                    if (board[i][k] == piece){
                        vertMatch++;
                    } else {
                        vertMatch = 0;
                    }
                    if (vertMatch == 4){
                        return true;
                    }
                }
                if (i == y){
                    if (board[i][k] == piece){
                        horzMatch++;
                    } else {
                        horzMatch = 0;
                    }
                    if (horzMatch == 4){
                        return true;
                    }
                }
                if (i-y == k-x){
                    if (board[i][k] == piece){
                        rightDiagMatch++;
                    } else {
                        rightDiagMatch = 0;
                    }
                    if (rightDiagMatch == 4){
                        return true;
                    }
                }
                if (i == -k+intercept){
                    if (board[i][k] == piece){
                        leftDiagMatch++;
                    } else {
                        leftDiagMatch = 0;
                    }
                    if (leftDiagMatch == 4){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
