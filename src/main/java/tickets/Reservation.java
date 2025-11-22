package tickets;

import train.Stop;

public class Reservation extends Ticket{
    public enum Type {
        SECOND_CLASS,
        FIRST_CLASS,
        BYCICLE
    }

    int seatNum;
    int coachNum;
    Type type;
    String passId;


    public Reservation(int id, int trainId, int price, String passengerName, Stop from, Stop to, int seatNum, int coachNum, String passId, Type t){
        super(id, trainId, price, passengerName, from, to);
        this.seatNum = seatNum;
        this.coachNum = coachNum;
        this.type = t;
        this.passId = passId;

    }

    public int getSeat(){return seatNum;}
    public int getCoach(){return coachNum;}

    @Override
    public String toString(){
        return "Jegy: " + id + ", Vonat: " + trainId + ", Ár: " + price + "Ft, Utas: " + passengerName + ", Kocsi: " + coachNum + ", Hely: " + seatNum + ", Igazolvány ID: " + passId;
    }

    @Override
    public String getTicketType() {
        return "2nd class ticket";
    }
}
