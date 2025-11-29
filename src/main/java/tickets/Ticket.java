package tickets;
import java.io.Serializable;

import train.Stop;
import utilities.Time;

public class Ticket implements Serializable{

    protected int id;
    protected int trainId;
    protected int price;

    protected String passengerName;
    protected Stop from;
    protected Stop to;
    protected Time timeOfLeave;
    protected Time timeOfArrival;

    public Ticket(int id, int trainId, int price, String passengerName, Stop from, Stop to) {
        
        this.id = id;
        this.trainId = trainId;
        this.price = price;
        this.passengerName = passengerName;
        this.from = from;
        this.to = to;
        this.timeOfLeave = from.getLeave();
        this.timeOfArrival = to.getArrive();
    }

    public int getSeat(){return id;}
    public int getTrainId(){return trainId;}
    public int getPrice(){return price;}
    public String getPassengerName(){return passengerName;}

    public String getTicketType() {
        return "Menetjegy";
    }

    @Override
    public String toString() {
        return getTicketType() + " | " + timeOfLeave + " | " + from + " -> " + to + " (" + passengerName + ")";
    }

}
