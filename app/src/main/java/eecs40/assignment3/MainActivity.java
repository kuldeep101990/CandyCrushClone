package eecs40.assignment3;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private int boardSize_X, boardsize_Y;
    private Piece pieces[][];
    private Board board;
    private Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardSize_X = 7;
        boardsize_Y = 7;
        pieces = new Piece[boardSize_X][boardsize_Y];
        board = new Board(this);
        board.setBoard(pieces, boardSize_X,boardsize_Y);
        setContentView(board);

    }

    @Override
    protected void onPause(){
        super.onPause();
        board.pause();
    }

    @Override
    protected void onResume(){
        super.onResume();
        board.resume();
    }

}
