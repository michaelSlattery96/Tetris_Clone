package com.clone.tetris;

public class Board {

    private int board[][] = new int[10][20];
    private int shape[][];

    public Board(){

    }

    public void setShapeStart(int startRowX){

        board[startRowX][0] = 1;
    }

    public void setShape(int[][] shape){
        this.shape = shape;
    }
}
