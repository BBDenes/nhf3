package train;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tickets.*;

public class Coach{
    private int id;
    private int capacity;
    private int bicycleCapacity;
    private int wheelchairCapacity;
    private int available;
    private boolean firstClass;
    private boolean buffetCar;

    private List<Ticket> tickets;
    private List<Seat> seats;
    private List<Integer> reservedSeats;

    public Coach(int id, int capacity, int bicycleCapacity, int wheelchairCapacity, boolean firstClass, boolean buffetCar) {
        this.id = id;
        this.capacity = capacity;
        this.available = capacity;
        this.bicycleCapacity = bicycleCapacity;
        this.wheelchairCapacity = wheelchairCapacity;
        this.firstClass = firstClass;
        this.buffetCar = buffetCar;
        this.seats = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.reservedSeats = new ArrayList<>();

        for (int i = 1; i <= capacity; i++) {
            Seat.Position position = (i % 2 == 0) ? Seat.Position.CORRIDOR : Seat.Position.WINDOW; // Páros=folyosó, Páratlan=ablak
            boolean atTable = (i <= 4 || i > capacity-4);
            Seat newSeat = new Seat(i,this.id);
            newSeat.setPosition(position);
            newSeat.setTable(atTable);
            seats.add(newSeat);
        }
    }

    public Coach(String formattedInput) throws NumberFormatException{
        String[] in = formattedInput.split(";");
        if(in.length != 6){ throw new NumberFormatException("Nem megfelelő számú bemenet");}
        int sid = Integer.parseInt(in[0]);
        int cap = Integer.parseInt(in[1]);
        int bycicle = Integer.parseInt(in[2]);
        int wheelchair = Integer.parseInt(in[3]);
        boolean fc = (Integer.parseInt(in[4]) == 1);
        boolean buffet = (Integer.parseInt(in[5]) == 1);


        this.id = sid;
        this.capacity = cap;
        this.available = cap;
        this.bicycleCapacity = bycicle;
        this.wheelchairCapacity = wheelchair;
        this.firstClass = fc;
        this.buffetCar = buffet;
        this.seats = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.reservedSeats = new ArrayList<>();

        for (int i = 1; i <= capacity; i++) {
            Seat.Position position = (i % 2 == 0) ? Seat.Position.CORRIDOR : Seat.Position.WINDOW; // Páros=folyosó, Páratlan=ablak
            boolean atTable = (i <= 4 || i > capacity-4);
            Seat newSeat = new Seat(i,this.id);
            newSeat.setPosition(position);
            newSeat.setTable(atTable);
            seats.add(newSeat);
        }
        
            
        
    }
    public int getId() { return id;}
    public int getCapacity() {return capacity;}
    public List<Seat> getSeats(){return seats;}
    public int getAvailable() {return available;}
    public List<Integer> getReservedSeatIds() {return reservedSeats;}
    public int getBicycleCapacity() {return bicycleCapacity;}
    public int getWheelchairCapacity() {return wheelchairCapacity;}
    public boolean isFirstClass() {return firstClass;}
    public boolean isBuffetCar() {return buffetCar;}
    
    @Override
    public String toString() {
        return "Coach: " +
                "id: " + id +
                ", férőhely" + capacity +
                ", kerékpárhelyek: " + bicycleCapacity +
                ", akadálymentes helyek: " + wheelchairCapacity +
                ", 1.osztály: " + firstClass +
                ", Büfékocsi: " + buffetCar;
    }

    public void addTicket(Reservation t) throws RuntimeException{
        if (reservedSeats.size() >= capacity) {
            throw new RuntimeException("Coach is full!");
        }
        if (reservedSeats.contains(t.getSeat())){
            throw new RuntimeException("Seat already occupied");
        }
        this.tickets.add(t);
        this.reservedSeats.add(seats.get(0).getId());
        refreshAvailableSeats();
        
    }

    public void addTicket(Ticket t) throws RuntimeException{
        this.tickets.add(t);
        this.reservedSeats.add(((Reservation)t).getSeat());
        refreshAvailableSeats();
    }

    private void refreshAvailableSeats() {
        this.available = capacity - reservedSeats.size();
        for(Seat s : seats){
            if(reservedSeats.contains(s.getId())){
                s.setReserved(true);
            } else {
                s.setReserved(false);
            }
        }
    }

    public int generateAvailableSeat() {
        Random rand = new Random();
        int szek;
        do {
            szek = rand.nextInt(1, this.seats.size());
        } while (reservedSeats.contains(szek));
        return szek;
    }

    public void resetReservations(){
        this.available = this.capacity;
        this.tickets = new ArrayList<>();
        this.reservedSeats = new ArrayList<>();
        for (Seat s : this.seats) {
            s.setReserved(false);
        }
    }

}
