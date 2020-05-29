package eecs40.assignment3;




public class Piece {

    private int locationX, locationY, drop;
    private GemType gemtype;
    private boolean remove;
    private boolean moved;

    public Piece(int locationX, int locationY, int drop, boolean remove){
        this.remove = remove;
        this.drop = drop;
        this.locationX = locationX;
        this.locationY = locationY;
    }

    public boolean getRemove(){
        return remove;
    }

    public int getLocationX() {
        return locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public GemType getGemtype() {
        return gemtype;
    }

    public int getDrop(){
        return drop;
    }

    public void setRemove(boolean check) {
        this.remove = check;
    }

    public void setDrop(int drop){
        this.drop = drop;
    }

    public void setGemtype(GemType gemtype) {
        this.gemtype = gemtype;
    }

    public void setGemtype(int x) {
        switch(x){
            case 0:
                gemtype = GemType.RED;
                break;
            case 1:
                gemtype = GemType.ORANGE;
                break;
            case 2:
                gemtype = GemType.YELLOW;
                break;
            case 3:
                gemtype = GemType.GREEN;
                break;
            case 4:
                gemtype = GemType.BLUE;
                break;
            case 5:
                gemtype = GemType.PURPLE;
                break;
        }

    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }



    public boolean checkAdjacenct(Piece gem){
        int locationX = gem.getLocationX();
        int locationY = gem.getLocationY();
        if(((Math.abs(locationX - this.locationX) == 1) && locationY == this.locationY) ^ ((Math.abs(locationY - this.locationY) == 1) && locationX == this.locationX)) {
            return true;
        }
        else{
            return false;
        }
    }

    public boolean checkColor(GemType gemType){
        if(this.gemtype == gemType){
            return true;
        }
        else{
            return false;
        }

    }

    public void switchGems(Piece gem){
        GemType holdGemtype = gem.getGemtype();
        gem.setGemtype(gemtype);
        gemtype = holdGemtype;
    }
}
