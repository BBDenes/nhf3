package train;
import java.io.Serializable;

public class Seat implements Serializable {

    private int id;
    private int coachId;
    private Position position;
    private boolean atTable;
    private boolean reserved;
    private boolean isSelected;
    
    public enum Position {
        WINDOW,
        CORRIDOR
    }
    
    public Seat(int id,int coachId){
        this.id = id;
        this.coachId = coachId;
        this.position = null;
        this.atTable = false;
        this.reserved = false;
        this.isSelected = false;
    }
    public int getId(){return id;}
    public int getCoach(){return coachId;}
    public Position getPosition(){return position;}
    public boolean isAtTable(){return atTable;}
    public boolean isSelected(){return isSelected;}
    public void toggleSelect(){isSelected = !isSelected;}
    public boolean isReserved(){return reserved;}
    public void setReserved(boolean reserved){this.reserved = reserved;}
    public void setPosition(Position p){this.position = p;}
    public void setTable(boolean b){this.atTable = b;}
    
    @Override
    public String toString() {
        return "Seat" +
                " id=" + id +
                ", position=" + position +
                ", atTable=" + atTable +
                ", reserved=" + reserved;
    }

}