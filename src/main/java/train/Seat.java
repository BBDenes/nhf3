package train;
import java.io.Serializable;

public class Seat implements Serializable {

    private int id;
    private Position position;
    private boolean atTable;
    private boolean reserved;
    
    public enum Position {
        WINDOW,
        CORRIDOR
    }
    
    public Seat(int id, boolean atTable, Position p){
        this.id = id;
        this.position = p;
        this.atTable = atTable;
        this.reserved = false;
    }
    public int getId(){return id;}
    public Position getPosition(){return position;}
    public boolean isAtTable(){return atTable;}
    public boolean isReserved(){return reserved;}
    public void setReserved(boolean reserved){this.reserved = reserved;}
    
    @Override
    public String toString() {
        return "Seat" +
                " id=" + id +
                ", position=" + position +
                ", atTable=" + atTable +
                ", reserved=" + reserved;
    }

}