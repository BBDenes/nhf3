package tickets;

public abstract class Ticket {
    protected int id;
    protected int price;
    protected String passengerName;
    public int getSeat(){return id;}
}
