package eecs40.assignment3;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.Random;

public class Board extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener, Runnable {

    Thread thread;
    SurfaceHolder surfaceHolder;
    boolean isItOK = false;
    boolean remove = false;

    Paint red = new Paint();
    Paint blue = new Paint();
    Paint green = new Paint();
    Paint yellow = new Paint();
    Paint purple = new Paint();
    Paint orange = new Paint();
    Paint white = new Paint();
    Paint outline = new Paint();
    Paint special = new Paint();
    Paint end = new Paint();
    Piece pieces[][];
    int boardSizeX, boardSizeY;
    int minBoardX, minBoardY, maxBoardX, maxBoardY;
    float x, y;
    float testX, testY;
    Piece piece1, piece2;
    Rect ourRect = new Rect();
    Collision collision;
    Movement movement;
    boolean cont;
    int points;



    public Board(Context context) {
        super(context);
        surfaceHolder = getHolder();
        setFocusable(true);
        setWillNotDraw(false);
        blue.setColor(Color.BLUE);
        blue.setStyle(Paint.Style.FILL);
        red.setColor(Color.RED);
        red.setStyle(Paint.Style.FILL);
        green.setColor(Color.GREEN);
        green.setStyle(Paint.Style.FILL);
        yellow.setColor(Color.YELLOW);
        yellow.setStyle(Paint.Style.FILL);
        purple.setColor(Color.parseColor("#551a8b"));
        purple.setStyle(Paint.Style.FILL);
        orange.setColor(Color.parseColor("#ffa550"));
        orange.setStyle(Paint.Style.FILL);
        white.setColor(Color.WHITE);
        white.setStyle(Paint.Style.FILL);
        special.setColor(Color.BLACK);
        special.setStyle(Paint.Style.FILL);
        end.setTextSize(100);
        end.setColor(Color.BLACK);
        outline.setColor(Color.BLACK);
        outline.setStyle(Paint.Style.STROKE);
        outline.setStrokeWidth(20);
        testX = 50;
        testY = 50;
        cont = true;
        points = 0;

    }


    @Override
    public void run() {
        Canvas canvas;

        while (isItOK) {
                if (!surfaceHolder.getSurface().isValid()) {
                    continue;
                }
                canvas = surfaceHolder.lockCanvas();

                ourRect.set(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.drawRect(ourRect, white);

                minBoardX = canvas.getWidth() / 2 - (140 * ((boardSizeX - 1) / 2)) - 70;
                minBoardY = canvas.getHeight() / 2 + 100 - (140 * ((boardSizeY - 1) / 2));
                maxBoardX = canvas.getWidth() / 2 + (140 * ((boardSizeX - 1) / 2)) + 70;
                maxBoardY = canvas.getHeight() / 2 + 240 + (140 * ((boardSizeY - 1) / 2));
                int positionX, positionY;


                positionX = canvas.getWidth() / 2 - (140 * ((boardSizeX - 1) / 2)) - 70;
                positionY = canvas.getHeight() / 2 - (140 * ((boardSizeY - 1) / 2)) + 100;

                for (int x = 0; x < boardSizeX; x++) {
                    for (int y = 0; y < boardSizeY; y++) {
                        ourRect.set(positionX, positionY, positionX + 140, positionY + 140);
                        drawRectangle(canvas, pieces[x][y].getGemtype(), ourRect);
                        positionY += 140;
                    }
                    positionX += 140;
                    positionY -= boardSizeY * 140;
                }
                canvas.drawCircle(testX, testY, 10, special);

                if (!cont) {

                    if (points < 100) {
                        canvas.drawText("No More", 100, 200, end);
                        canvas.drawText("Moves", 100, 300, end);
                    } else {
                        canvas.drawText("Max Points", 75, 200, end);
                    }
                    ourRect.set(600, 100, 1000, 300);
                    canvas.drawText("Restart", 630, 230, end);
                    canvas.drawRect(ourRect, outline);
                }
                else {
                    canvas.drawText("Score: " + points, 100, 200, end);

                }
                surfaceHolder.unlockCanvasAndPost(canvas);
            }



    }
    public void pause(){
        isItOK = false;
        while(true){
            try {
                thread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        thread = null;
    }
    public void resume(){
        isItOK = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        invalidate();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent e){

        if(e.getAction() == MotionEvent.ACTION_DOWN) {
            x = e.getX();
            y = e.getY();
            testX = x;
            testY = y;
            if(cont) {

                if (minBoardX < x && x < maxBoardX && minBoardY < y && y < maxBoardY) {
                    if (piece1 == null) {
                        setPiece(x, y, 1);
                    } else {
                        setPiece(x, y, 2);
                    }
                } else {
                    special.setColor(Color.BLACK);
                }
            }
            else{

                special.setColor(Color.BLACK);
                if(600 < x && x < 1000 && 100 < y && y < 300){
                    points = 0;
                    cont = true;
                    do{
                        scrambleBoard(pieces, boardSizeX, boardSizeY);


                    }while(!collision.finalTest(pieces, boardSizeX, boardSizeY));
                }



            }
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent e){

        return false;
    }

    @Override
    protected void onDraw (Canvas c){

    }



    public void drawRectangle(Canvas c, GemType gemtype, Rect currRect){
        switch(gemtype){
            case RED:
                c.drawRect(currRect, red);
                break;
            case ORANGE:
                c.drawRect(currRect, orange);
                break;
            case YELLOW:
                c.drawRect(currRect, yellow);
                break;
            case GREEN:
                c.drawRect(currRect, green);
                break;
            case BLUE:
                c.drawRect(currRect, blue);
                break;
            case PURPLE:
                c.drawRect(currRect, purple);
                break;
            case NONE:
                c.drawRect(currRect, white);
                break;
        }
    }

    public void setBoard(Piece[][] pieces, int boardSizeX, int boardSizeY){
        Random rand = new Random();
        this.boardSizeX = boardSizeX;
        this.boardSizeY = boardSizeY;
        int k;
        collision = new Collision(boardSizeY);
        movement = new Movement();


            for (int x = 0; x < this.boardSizeX; x++) {
                for (int y = 0; y < this.boardSizeY; y++) {
                    pieces[x][y] = new Piece(x, y, 0, false);

                    do {
                        k = rand.nextInt(6);
                    } while (!collision.verticalTest(k) || !collision.horizontalTest(k, y));

                    pieces[x][y].setGemtype(k);

                }
            }
            while(!collision.finalTest(pieces, boardSizeX, boardSizeY)){
                scrambleBoard(pieces, boardSizeX, boardSizeY);
            }
        this.pieces = pieces;
    }

    public void scrambleBoard(Piece pieces[][], int boardSizeX, int boardSizeY){
        Random rand = new Random();
        int k;
        for (int x = 0; x < boardSizeX; x++) {
            for (int y = 0; y < boardSizeY; y++) {

                do {
                    k = rand.nextInt(6);
                } while (!collision.verticalTest(k) || !collision.horizontalTest(k, y));

                pieces[x][y].setGemtype(k);

            }
        }
    }

    public void setPiece(float x, float y, int choice){
        int xP = 0;
        int yP = 0;
        float holdX1 = minBoardX;
        float holdX2 = holdX1 + 140;
        float holdY1 = minBoardY;
        float holdY2 = holdY1 + 140;
        boolean test;

        while(xP < boardSizeX){
            if(holdX1 < x && x < holdX2){
                break;
            }
            xP += 1;
            holdX1 += 140;
            holdX2 += 140;


        }
        while(yP < boardSizeY){
            if(holdY1 < y && y < holdY2){
                break;
            }
            yP += 1;
            holdY1 += 140;
            holdY2 += 140;

        }
        if (xP >= boardSizeX || yP >= boardSizeY){
            special.setColor(Color.BLACK);
            return;
        }

        special.setColor(Color.WHITE);
        if(choice == 1){
            piece1 = pieces[xP][yP];
        }
        else{
            piece2 = pieces[xP][yP];
            if(piece1.checkAdjacenct(piece2)) {
                piece1.switchGems(piece2);

                test = collision.localCheck(pieces, piece1, piece2, boardSizeX, boardSizeY);

                if (!test){
                    piece1.switchGems(piece2);
                    piece1 = null;
                    piece2 = null;
                    return;
                }





                collision.setPoints(0);

                remove = true;

                points += collision.getPoints();
                collision.setPoints(0);

                /*animatedRemove(pieces, boardSizeX, boardSizeY);*/
                collision.removePieces(pieces, boardSizeX, boardSizeY);
                movement.determineDrop(pieces, boardSizeX, boardSizeY);
                movement.gravityDrop(pieces, boardSizeX, boardSizeY);
                boolean continueCheck;
                do {
                    continueCheck = false;


                    for (int j = 0; j < boardSizeX; j++) {
                        for (int k = 0; k < boardSizeY; k++) {
                            if(!pieces[j][k].checkColor(GemType.NONE)) {
                                collision.blockCheck(pieces, j, k, pieces[j][k].getGemtype(), boardSizeX, boardSizeY);
                            }
                            if(pieces[j][k].getRemove()){
                                continueCheck = true;
                            }
                        }
                    }


                    remove = true;

                    points += collision.getPoints();
                    collision.setPoints(0);

                    /*animatedRemove(pieces, boardSizeX, boardSizeY);*/
                    collision.removePieces(pieces, boardSizeX, boardSizeY);
                    movement.determineDrop(pieces, boardSizeX, boardSizeY);
                    movement.gravityDrop(pieces, boardSizeX, boardSizeY);


                }while(continueCheck);




            }

            points += collision.getPoints();
            collision.setPoints(0);
            piece1 = null;
            piece2 = null;

            cont = collision.finalTest(pieces, boardSizeX, boardSizeY);

            if(points >= 100){
                cont = false;
            }
        }


    }


    /*public void animatedRemove(Piece pieces[][], int boardSizeX, int boardSizeY){
        int positionX, positionY;
        int shrinkX, shrinkY;
        Canvas canvas;
        shrinkX = 14;
        shrinkY = 14;
        while (isItOK){
            if(!surfaceHolder.getSurface().isValid()){
                continue;
            }


            canvas = surfaceHolder.lockCanvas();

            positionX = canvas.getWidth()/2 - (140*((boardSizeX-1)/2)) - 70;
            positionY = canvas.getHeight()/2 - (140*((boardSizeY-1)/2)) + 100;

            if(positionX + shrinkX == positionX + 140 - shrinkX && positionY + shrinkY == positionY + 140 - shrinkY){
                surfaceHolder.unlockCanvasAndPost(canvas);
                break;
            }

            ourRect.set(0,0, canvas.getWidth(), canvas.getHeight());
            canvas.drawRect(ourRect, white);


            for (int x = 0; x < boardSizeX; x++){
                for (int y = 0; y < boardSizeY; y++) {
                    if(!pieces[x][y].getRemove()){
                        ourRect.set(positionX, positionY, positionX + 140, positionY + 140);
                        drawRectangle(canvas, pieces[x][y].getGemtype(), ourRect);
                        positionY += 140;
                    }
                    else{
                        ourRect.set(positionX + shrinkX, positionY + shrinkY, positionX + 140 - shrinkX, positionY + 140 - shrinkY);
                        drawRectangle(canvas, pieces[x][y].getGemtype(), ourRect);
                        positionY += 140;
                    }
                }
                positionX += 140;
                positionY -= boardSizeY*140;
            }

            canvas.drawCircle(testX, testY, 10, special);
            shrinkX += 14;
            shrinkY += 14;
            canvas.drawText("Score: " + points, 100, 200, end);

            surfaceHolder.unlockCanvasAndPost(canvas);
        }
        remove = false;
    }*/





}
