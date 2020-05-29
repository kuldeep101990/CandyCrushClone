package eecs40.assignment3;

import android.graphics.Canvas;

import java.util.Random;

public class Movement {


    public void randomizeDrop(Piece pieces[][], int boardSizeX, int boardSizeY){
        Random rand = new Random();
        int k;
        for (int x = 0; x < boardSizeX; x++) {
            for (int y = 0; y < boardSizeY; y++) {
                if(pieces[x][y].checkColor(GemType.NONE)){
                    k = rand.nextInt(6);
                    pieces[x][y].setGemtype(k);
                }
            }
        }
    }

    public void determineDrop(Piece pieces[][], int boardSizeX, int boardSizeY){
        int dropAmount;

        for (int x = 0; x < boardSizeX; x++) {
            dropAmount = 0;
            for (int y = boardSizeY - 1; y > -1; y--) {
                if(pieces[x][y].checkColor(GemType.NONE)){
                    dropAmount += 1;
                }
                else{
                    pieces[x][y].setDrop(dropAmount);
                }
            }
        }
    }

    public void gravityDrop(Piece pieces[][], int boardSizeX, int boardSizeY){
        int dropAmount;
        for (int x = 0; x < boardSizeX; x++) {
            for (int y = boardSizeY - 1; y > -1; y--) {
                if(pieces[x][y].getDrop() == 0 || pieces[x][y].checkColor(GemType.NONE)){
                    continue;
                }
                else{
                    pieces[x][y + pieces[x][y].getDrop()].setGemtype(pieces[x][y].getGemtype());
                    pieces[x][y].setGemtype(GemType.NONE);
                    pieces[x][y].setDrop(0);
                }
            }
        }
        randomizeDrop(pieces, boardSizeX, boardSizeY);
    }


}
