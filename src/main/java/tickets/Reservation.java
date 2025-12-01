package tickets;

import train.Stop;

public class Reservation extends Ticket{
    public enum Type {
        WITH_PASS,
        WITHOUT_PASS
    }

    int seatNum;
    int coachNum;
    Type type;
    String passId;


    public Reservation(int id, int trainId, int price, String passengerName, Stop from, Stop to, int coachNum, int seatNum, String passId){
        super(id, trainId, price, passengerName, from, to);
        this.seatNum = seatNum;
        this.coachNum = coachNum;
        this.passId = passId;
        this.type = passId == null || passId.isEmpty() ? Type.WITHOUT_PASS : Type.WITH_PASS; 

    }

    public int getSeat(){return seatNum;}
    public int getCoach(){return coachNum;}
    public String getPassId(){return this.type == Type.WITH_PASS ? passId : null;}

    @Override
    public String toString(){
        return (this.type.equals(Type.WITH_PASS)? "Egyszeru helyjegy" : "Bérlet nélküli helyjegy") + id + " | Vonat: " + trainId + ", Ár: " + price + "Ft, Utas: " + passengerName + ", Kocsi: " + coachNum + ", Hely: " + seatNum + ", Igazolvány ID: " + passId;
    }

    @Override
    public String getTicketType() {
        return "Másodosztályú helyjegy";
    }

    protected String getExtraDetails() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("    <div class='row'>\n");
        sb.append("      <span class='label'>Helyfoglalás</span>\n");
        sb.append("      <span class='value'>Kocsi: ").append(this.coachNum).append(" | Hely: ").append(this.seatNum).append("</span>\n");
        sb.append("    </div>\n");

        if (passId != null && !passId.isEmpty()) {
            sb.append("    <div class='row'>\n");
            sb.append("      <span class='label'>Bérlet</span>\n");
            sb.append("      <span class='value'>").append(passId).append("</span>\n");
            sb.append("    </div>\n");
        }

        return sb.toString();
    }


}
