package eecs40.assignment3;

public class Collision {
    private int collisionV;
    private int holdV;
    private int collisionH[];
    private int holdH[];
    private int points;

    public void setPoints(int points){
        this.points = points;
    }

    public int getPoints(){
        return points;
    }

    public Collision(int ySize){
        collisionH = new int[ySize];
        holdH = new int[ySize];
        collisionV = -1;
        holdV = 0;
        for(int x = 0; x < ySize; x++){
            collisionH[x] = -1;
            holdH[x] = 0;
        }
        points = 0;

    }

    public boolean verticalTest(int x){
        if(x == collisionV && holdV >= 2){
            return false;
        }
        else if (x == collisionV){
            holdV++;
        }
        else{
            collisionV = x;
            holdV = 1;
        }
        return true;
    }

    public boolean horizontalTest(int x, int row){
        if(x == collisionH[row] && holdH[row] >= 2){
            return false;
        }
        else if (x == collisionH[row]){
            holdH[row]++;
        }
        else{
            collisionH[row] = x;
            holdH[row] = 1;
        }
        return true;
    }

    public boolean localCheck (Piece pieces[][], Piece moved1, Piece moved2, int boardSizeX, int boardSizeY){
        int x,y;
        boolean test1, test2;

        GemType gemType;

        x = moved1.getLocationX();
        y = moved1.getLocationY();
        gemType = moved1.getGemtype();

        test1 = blockCheck(pieces, x, y, gemType, boardSizeX, boardSizeY);

        x = moved2.getLocationX();
        y = moved2.getLocationY();
        gemType = moved2.getGemtype();

        test2 = blockCheck(pieces, x, y, gemType, boardSizeX, boardSizeY);

        test1 = test1 || test2;

        return test1;


    }

    public boolean blockCheck(Piece pieces[][], int x, int y, GemType gemType, int boardSizeX, int boardSizeY){
        int check, k;
        boolean test = false;

        //immediate vertical
        if( y != 0 && y != boardSizeY -1 && pieces[x][y-1].checkColor(gemType) && pieces[x][y+1].checkColor(gemType)){
            pieces[x][y].setRemove(true);
            pieces[x][y+1].setRemove(true);
            pieces[x][y-1].setRemove(true);
            test = true;
        }
        //immediate horizontal
        if( x != 0 && x != boardSizeX -1 && pieces[x-1][y].checkColor(gemType) && pieces[x+1][y].checkColor(gemType)){
            pieces[x][y].setRemove(true);
            pieces[x+1][y].setRemove(true);
            pieces[x-1][y].setRemove(true);
            test = true;
        }
        //check up
        check = 0;
        k = y;
        while(y > 1){
            k -=1;
            if(pieces[x][k].checkColor(gemType)){
                check += 1;
            }
            else{
                break;
            }

            if(k == 0){
                break;
            }
        }

        if (check >= 2){
            test = true;
            while(check >= 0){
                pieces[x][y - check].setRemove(true);
                check -= 1;
            }
        }



        //check left
        check = 0;
        k = x;
        while(x > 1){
            k -=1;
            if(pieces[k][y].checkColor(gemType)){
                check += 1;
            }
            else{
                break;
            }

            if(k == 0){
                break;
            }
        }

        if (check >= 2){
            while(check >= 0){
                test = true;
                pieces[x - check][y].setRemove(true);
                check -= 1;
            }
        }

        //check down
        check = 0;
        k = y;
        while(y < boardSizeY - 2){
            k +=1;
            if(pieces[x][k].checkColor(gemType)){
                check += 1;
            }
            else{
                break;
            }

            if(k == boardSizeY - 1){
                break;
            }
        }

        if (check >= 2){
            while(check >= 0){
                test = true;
                pieces[x][y + check].setRemove(true);
                check -= 1;
            }
        }

        //check right
        check = 0;
        k = x;
        while(x < boardSizeX - 2){
            k +=1;
            if(pieces[k][y].checkColor(gemType)){
                check += 1;
            }
            else{
                break;
            }

            if(k == boardSizeX - 1){
                break;
            }
        }

        if (check >= 2){
            while(check >= 0){
                test = true;
                pieces[x + check][y].setRemove(true);
                check -= 1;
            }
        }
        return test;
    }

    public boolean globalCheck(Piece pieces[][], int boardSizeX, int boardSizeY, Movement movement){
        boolean continueCheck = false;

        for (int x = 0; x < boardSizeX; x++) {
            for (int y = 0; y < boardSizeY; y++) {
                if(!pieces[x][y].checkColor(GemType.NONE)) {
                    blockCheck(pieces, x, y, pieces[x][y].getGemtype(), boardSizeX, boardSizeY);
                }
                if(pieces[x][y].getRemove()){
                    continueCheck = true;
                }
            }
        }
        if(!continueCheck){
            return false;
        }

        removePieces(pieces, boardSizeX, boardSizeY);
        movement.determineDrop(pieces, boardSizeX, boardSizeY);
        movement.gravityDrop(pieces, boardSizeX, boardSizeY);
        return true;

    }

    public void removePieces(Piece pieces[][], int boardSizeX, int boardSizeY){

        for (int x = 0; x < boardSizeX; x++) {
            for (int y = 0; y < boardSizeY; y++) {
                if(pieces[x][y].getRemove()){
                    pieces[x][y].setGemtype(GemType.NONE);
                    pieces[x][y].setRemove(false);
                    points += 1;
                }
            }
        }

    }

    public void returnRemove(Piece pieces[][], int boardSizeX, int boardSizeY){
        for (int x = 0; x < boardSizeX; x++) {
            for (int y = 0; y < boardSizeY; y++) {
                pieces[x][y].setRemove(false);
            }
        }
    }

    public boolean finalTest(Piece pieces[][], int boardSizeX, int boardSizeY){

        Piece piece1, piece2;
        boolean test = false;


        for (int x = 0; x < boardSizeX; x++) {
            for (int y = 0; y < boardSizeY; y++) {
                piece1 = pieces[x][y];
                if(x < boardSizeX - 1){
                    piece2 = pieces[x + 1][y];
                    piece1.switchGems(piece2);
                    test = localCheck(pieces, piece1, piece2, boardSizeX, boardSizeY);
                    piece1.switchGems(piece2);
                    if(test){
                        returnRemove(pieces,boardSizeX,boardSizeY);
                        return true;
                    }
                }
                piece1 = pieces[x][y];
                if(y < boardSizeY - 1){
                    piece2 = pieces[x][y + 1];
                    piece1.switchGems(piece2);
                    test = localCheck(pieces, piece1, piece2, boardSizeX, boardSizeY);
                    piece1.switchGems(piece2);
                    if(test){
                        returnRemove(pieces,boardSizeX,boardSizeY);
                        return true;
                    }

                }
            }
        }


        return false;

    }





}
