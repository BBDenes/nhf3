package utilities;

import java.util.ArrayList;
import java.util.List;
import tickets.Ticket;

public class PurchaseController {

    //private final int seatPrice = 650; 
    //private final int seatAndTicketPrice = 1000 + seatPrice; 
    //private final int simpleTicketPrice = 1000;
    private final int maxSeats = 1000; //egy kocsiban max hány ülés lehet

    private String from, to;
    private int numOfPassengers;
    private List<String> names, passIds;
    private int trainId;
    private boolean isFirstClass;
    private List<Boolean> accessibleMask, bycicleMask;
    private List<Ticket> ticketsToBuy;
    private List<Integer> seatsToReserve;

    public int getGlobalSeatId(int coachId, int seatId){
        return coachId*maxSeats + seatId;
    }

    public int[] getSeatFromGlobalId(int gId){
        int[] result = {Math.floorDiv(gId, 1000), gId%1000};
        return result;
    }
    public int getPassengerCount(){return this.numOfPassengers;}
    public List<String> getStops(){return List.of(this.from, this.to);}
    public List<String> getPassengerNames(){return this.names;}
    public List<String> getPassIds(){return this.passIds;}
    public List<Boolean> getAccessibleMask(){return this.accessibleMask;}
    public List<Boolean> getBycicleMask(){return this.bycicleMask;}
    public boolean isFirstClass(){return this.isFirstClass;}
    public int getTrainId(){return this.trainId;}

    public void addTicketToBuy(Ticket t){
        if(this.ticketsToBuy == null){
            this.ticketsToBuy = new ArrayList<>();
        }
        this.ticketsToBuy.add(t);
    }
    
    public List<Ticket> getTicketsToBuy() {
        if(this.ticketsToBuy == null){
            this.ticketsToBuy = new ArrayList<>();
        }
        return this.ticketsToBuy;
    }
    public void setStops(String from, String to){
        this.from = from;
        this.to = to;
    }

    public void setPassengerNum(int i){
        this.numOfPassengers = i;
    }

    public void setPassengers(List<String> names, List<String> passIds){
        this.names = names;
        this.passIds = passIds;
    }

    public void setTrain(int trainId){
        this.trainId = trainId;
    }

    public void setFirstClass(boolean isFirstClass){
        this.isFirstClass = isFirstClass;
    }
    public void setMasks(List<Boolean> accessibleMask, List<Boolean> bycicleMask){
        this.accessibleMask = accessibleMask;
        this.bycicleMask = bycicleMask;
    }   

    public void setSeatsToReserve(List<Integer> seats){
        this.seatsToReserve = seats;
    }
    
    public List<Integer> getSeatsToReserve() {
        if(seatsToReserve == null) return new ArrayList<>();
        return seatsToReserve;
    }

    
}
