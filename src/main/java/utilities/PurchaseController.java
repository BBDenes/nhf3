package utilities;

import java.util.ArrayList;
import java.util.List;
import tickets.Ticket;

/**
 * A vásárlást vezérlő osztály.
 * Ez az ostály tárolja el az egyes menükből beérkező adatokat
 * és kezeli a jegyvásárlás folyamatát.
 * 
 */

public class PurchaseController {

    
    private final int maxSeats = 1000; //egy kocsiban max hány ülés lehet

    private String from, to;
    private int numOfPassengers;
    private List<String> names, passIds;
    private int trainId;
    private boolean isFirstClass;
    private List<Boolean> accessibleMask, bycicleMask;
    private List<Ticket> ticketsToBuy;
    private List<Integer> seatsToReserve;


    /**
     * Létrehoz egy globális ülésazonosítót a kocsi és szék számból a következőképpen:
     * globális id = coachId * egy kocsiban lévő max ülés száma + seatId
     * @param coachId : A koocsi azonosítója
     * @param seatId : Az ülés azonosítója
     * @return : A globális id
     */
    public int getGlobalSeatId(int coachId, int seatId){
        return coachId*maxSeats + seatId;
    }

    /**
     * Az előző függvény inverze, vissaadja a globális id-ből a kocsi és ülés számát.
     * @param gId : A globális id
     * @return : Tömb, első elem a kocsi, második a szék azonosítója
     */

    public int[] getSeatFromGlobalId(int gId){
        int[] result = {Math.floorDiv(gId, 1000), gId%1000};
        return result;
    }

    //getterek, setterek...
    public int getPassengerCount(){return this.numOfPassengers;}
    public List<String> getStops(){return List.of(this.from, this.to);}
    public List<String> getPassengerNames(){return this.names;}
    public List<String> getPassIds(){return this.passIds;}
    public List<Boolean> getAccessibleMask(){return this.accessibleMask;}
    public List<Boolean> getBycicleMask(){return this.bycicleMask;}
    public boolean isFirstClass(){return this.isFirstClass;}
    public int getTrainId(){return this.trainId;}

    /**
     * Hozzáad egy jegyet a megvásárolandó jegyek listájához
     * @param t : A hozzáadandó jegy
     */
    public void addTicketToBuy(Ticket t){
        if(this.ticketsToBuy == null){
            this.ticketsToBuy = new ArrayList<>();
        }
        this.ticketsToBuy.add(t);
    }
    
    /**
     * getter a vásárolandó jegyekhez, ha még nincs inicializálva, üres listát ad
     * @return : A jegyek listája
     */
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
