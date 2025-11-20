package train;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import tickets.*;

public class Coach implements Serializable {
    private int id;
    private int capacity;
    private int bicycleCapacity;
    private int wheelchairCapacity;
    private boolean firstClass;
    private boolean buffetCar;

    private List<Ticket> tickets;
    private List<Seat> seats;
    private List<Integer> reservedSeats;

    public Coach(int id, int capacity, int bicycleCapacity, int wheelchairCapacity, boolean firstClass, boolean buffetCar) {
        this.id = id;
        this.capacity = capacity;
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
            seats.add(new Seat(i, atTable, position));
        }
    }
    public int getId() { return id;}
    public int getCapacity() {return capacity;}
    public int getBicycleCapacity() {return bicycleCapacity;}
    public int getWheelchairCapacity() {return wheelchairCapacity;}
    public boolean isFirstClass() {return firstClass;}
    public boolean isBuffetCar() {return buffetCar;}
    
    @Override
    public String toString() {
        return "Coach: " +
                "id=" + id +
                ", capacity=" + capacity +
                ", bicycleCapacity=" + bicycleCapacity +
                ", wheelchairCapacity=" + wheelchairCapacity +
                ", firstClass=" + firstClass +
                ", buffetCar=" + buffetCar +
                '}';
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
        
    }

    public void addTicket(Ticket t) throws RuntimeException{
        if (reservedSeats.size() >= capacity) {
            throw new RuntimeException("Coach is full!");
        }
        if (reservedSeats.contains(t.getSeat())) {
            throw new RuntimeException("Seat already occupied");
        }
        this.tickets.add(t);
    }

    public int generateAvailableSeat() {
        Random rand = new Random();
        int szek;
        do {
            szek = rand.nextInt() + 1;
        } while (reservedSeats.contains(szek));
        return szek;
    }

}
