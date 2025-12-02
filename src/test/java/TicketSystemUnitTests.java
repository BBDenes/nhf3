import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tickets.Reservation;
import tickets.Ticket;
import train.*;
import utilities.*;

public class TicketSystemUnitTests {

    private TrainHandler trainHandler;
    private PurchaseController purchase;
    private Train testTrain;
    private List<Stop> stops;

   @BeforeEach
    void setUp() {
        trainHandler = new TrainHandler();
        purchase = new PurchaseController();
        
        stops = new ArrayList<>();
        stops.add(new Stop("Budapest", new Time(8, 0), new Time(8, 10)));
        stops.add(new Stop("Szolnok", new Time(9, 0), new Time(9, 5)));
        stops.add(new Stop("Debrecen", new Time(10, 0), new Time(10, 10)));

        List<Coach> coaches = new ArrayList<>();
        // 1. o, 10 hely, 5 bicikli, 0 kerekesszék
        coaches.add(new Coach(1, 10, 5, 0, true, false)); 
        // 2. o, 20 hely, 0 bicikli, 2 kerekesszék
        coaches.add(new Coach(2, 20, 0, 2, false, false));

        testTrain = new Train(101, "Teszt IC", "IC", coaches, stops);

       

        trainHandler.getTrains().add(testTrain);
    }

    @Test
    // valid út: Budapest -> Debrecen
    void testSearchTrains_ValidRoute() {
        List<Train> results = trainHandler.searchTrains("Budapest", "Debrecen");
        assertEquals(1, results.size());
        assertEquals("Teszt IC", results.get(0).getName());
    }

    @Test
    //nem jó, mert fordított az irány
    void testSearchTrains_InvalidRoute_WrongDirection() {
        List<Train> results = trainHandler.searchTrains("Debrecen", "Budapest");
        assertTrue(results.isEmpty());

    }

    @Test
    //nincs ilyen állomás
    void testSearchTrains_InvalidStation() {
        List<Train> results = trainHandler.searchTrains("Budapest", "Mars");
        assertTrue(results.isEmpty());
    }

    @Test
    void testReserveAutomatic_2ndClass_NoPass() throws Exception {
        PurchaseController p = new PurchaseController();
        p.setTrain(101);
        p.setFirstClass(false);
        p.setStops("Budapest", "Szolnok");
        p.setPassengers(List.of("Utas1"), List.of("")); // Nincs bérlet

        trainHandler.reserveAutomaticSeats(p);

        List<Ticket> tickets = p.getTicketsToBuy();
        assertEquals(1, tickets.size());
        
        Ticket t = tickets.get(0);
        assertTrue(t instanceof Reservation);
        // Ár: 650 (helyjegy) + 1000 (menetjegy) = 1650
        assertEquals(1650, t.getPrice());
    }

    @Test
    void testReserveAutomatic_2ndClass_WithPass() throws Exception {
        PurchaseController p = new PurchaseController();
        p.setTrain(101);
        p.setFirstClass(false);
        p.setStops("Budapest", "Szolnok");
        p.setPassengers(List.of("Utas1"), List.of("BERLET-123")); // VAN bérlet

        trainHandler.reserveAutomaticSeats(p);

        Ticket t = p.getTicketsToBuy().get(0);
        assertEquals(650, t.getPrice());
    }

    @Test
    void testReserveAutomatic_1stClass_NoPass() throws Exception {
        PurchaseController p = new PurchaseController();
        p.setTrain(101);
        p.setFirstClass(true);
        p.setStops("Budapest", "Szolnok");
        p.setPassengers(List.of("Utas1"), List.of(""));

        trainHandler.reserveAutomaticSeats(p);

        Ticket t = p.getTicketsToBuy().get(0);
        assertEquals(3300, t.getPrice());
    }

    @Test
    void testGetRandom_Success() throws Exception {
        int[] seatInfo = trainHandler.getRandom(101, false);
        
        assertEquals(2, seatInfo[0]); 
        assertTrue(seatInfo[1] >= 1 && seatInfo[1] <= 20);
    }

    @Test
    void testGetRandom_FullTrain_ThrowsException() {
        Coach c1 = testTrain.getCoaches().get(0);
        for(int i=1; i<=10; i++) c1.addTicket(new Reservation(i, testTrain.getId(), 0, " ", new Stop("T1;1:1;1:1"), new Stop("T2;2:2;2:2"), c1.getId(), i, null));

        Exception exception = assertThrows(Exception.class, () -> {
            trainHandler.getRandom(101, true);
        });

        assertEquals("Nincs szabad hely a kért osztályon ezen a vonaton!", exception.getMessage());
    }

    @Test
    void testAttributeSearch_Bicycle() throws Exception {


        PurchaseController p = new PurchaseController();
        p.setTrain(101);
        p.setStops(testTrain.getStops().get(0).getName(), testTrain.getStops().get(1).getName());
        p.setFirstClass(true); 
        p.setPassengers(List.of("Bringás"), List.of(""));
        p.setMasks(List.of(false), List.of(true));

        trainHandler.reserveAutomaticSeats(p);
        
        Ticket t = p.getTicketsToBuy().get(0);
        if (t instanceof Reservation) {
            assertEquals(1, ((Reservation)t).getCoach());
        }
    }

    @Test
    void testAttributeSearch_Wheelchair_Fail() {
        PurchaseController p = new PurchaseController();
        p.setTrain(101);
        p.setFirstClass(true); 
        p.setPassengers(List.of("Kerekesszékes"), List.of(""));
        p.setMasks(List.of(true), List.of(false));

        Exception exception = assertThrows(Exception.class, () -> {
            trainHandler.reserveAutomaticSeats(p);
        });
        
        assertTrue(exception.getMessage().contains("Nincs ilyen kocsiii")); // Vagy amit javítottál
    }

    @Test
    void testSzemelyVonat_WithPass_ShouldNotBuyTicket() throws Exception {
        // Hozzunk létre egy személyvonatot
        Train szemely = new Train(202, "Személy", "Személy", new ArrayList<>(), new ArrayList<>());
        // Adjunk hozzá egy megállót, hogy működjön a keresés
        szemely.getStops().add(new Stop("A", new Time(8,0), new Time(8,0)));
        szemely.getStops().add(new Stop("B", new Time(9,0), new Time(9,0)));
        trainHandler.getTrains().add(szemely);

        PurchaseController p = new PurchaseController();
        p.setTrain(202);
        p.setFirstClass(false);
        p.setStops("A", "B");
        p.setPassengers(List.of("Bérletes"), List.of("PASS-123"));

        
    }

    @Test
    void testGetGlobalSeatId() {
        // Ha a kocsi ID=5 és a szék=42, a képlet: 5 * 1000 + 42 = 5042
        int globalId = purchase.getGlobalSeatId(5, 42);
        assertEquals(5042, globalId, "A globális ID számítása hibás");
    }

    @Test
    void testGetSeatFromGlobalId() {
        int globalId = 5042;
        int[] result = purchase.getSeatFromGlobalId(globalId);
        
        assertEquals(5, result[0], "A kocsi ID visszafejtése hibás");
        assertEquals(42, result[1], "A szék ID visszafejtése hibás");
    }

    @Test
    void testConstructorFromString_ValidInput() {
        String input = "10;50;5;2;1;0"; 
        Coach c = new Coach(input);

        assertEquals(10, c.getId());
        assertEquals(50, c.getCapacity());
        assertTrue(c.isFirstClass());
        assertFalse(c.isBuffetCar());
        assertEquals(5, c.getBicycleCapacity());
    }

    @Test
    void testConstructorFromString_InvalidInput() {
        String badInput = "10;randomcucc;káélákasd";
        assertThrows(NumberFormatException.class, () -> {
            new Coach(badInput);
        });
    }

    @Test
    void testAddTicket_UpdatesAvailability() {
        Coach c = new Coach(1, 10, 0, 0, false, false);
        int initialAvailable = c.getAvailable();

        Reservation res = new Reservation(99, 100, 500, "Gajdos Sándor", stops.get(0), stops.get(1), 1, 1, "");
        c.addTicket(res);

        assertEquals(initialAvailable - 1, c.getAvailable(), "A szabad helyek számának csökkennie kell");
        assertTrue(c.getReservedSeatIds().contains(1), "Az 1-es széknek foglaltnak kell lennie");
    }


    @Test
    void testAddTicket_WhenFull_ThrowsException() {
        // Kicsi kocsi: 1 hely
        Coach c = new Coach(1, 1, 0, 0, false, false);
        
        // Első foglalás (Sikerül)
        Reservation r1 = new Reservation(1, 1, 500, "A", stops.get(0), stops.get(1), 1, 1, null);
        c.addTicket(r1);

        // Második foglalás (Ugyanoda vagy tele kocsiba -> Hiba)
        Reservation r2 = new Reservation(2, 1, 500, "B", stops.get(0), stops.get(1), 1, 1, null); // 1-es szék már foglalt
        
        assertThrows(RuntimeException.class, () -> {
            c.addTicket(r2);
        }, "Tele lévő kocsinál hibát kell dobni");
    }

    @Test
    void testGenerateAvailableSeat() {
        Coach c = new Coach(1, 50, 0, 0, false, false);
        for(int i=1; i<50; i++) {
            c.getReservedSeatIds().add(i); 
        }
        
        // Csak az 50-es szabad
        int seat = c.generateAvailableSeat();
        assertEquals(50, seat, "Csak az 50-es szék szabad, azt kellene visszaadnia");
    }

    @Test
    void testTimeBadInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Time("25:61");
        });
    }

    @Test
    void testDeleteTrainByIndex_Success() {
        assertEquals(1, trainHandler.getTrains().size());

        trainHandler.deleteTrainByIndex(101);

        assertTrue(trainHandler.getTrains().isEmpty());
    }

    @Test
    void testDeleteTrainByIndex_NotFound_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            trainHandler.deleteTrainByIndex(999);
        });
        
        assertEquals(1, trainHandler.getTrains().size());
    }

    @Test
    void testResetReservation_Success() {
        
        purchase.setTrain(101);
        purchase.setSeatsToReserve(List.of(1));
        purchase.setPassengers(List.of("Utas1"), List.of(""));
        purchase.setStops("Budapest", "Szolnok");
        trainHandler.reserveSpecificSeats(purchase);
        trainHandler.resetReservation(101);

        assertFalse(trainHandler.getTrainByIndex(101).getCoaches().get(0).getReservedSeatIds().contains(1), "A reset után a széknek szabadnak kell lennie");
    }

    @Test
    void testReserveSpecificSeats() {
        PurchaseController p = new PurchaseController();
        p.setTrain(101);
        p.setFirstClass(true);
        p.setStops("Budapest", "Debrecen"); // Érvényes megállók
        p.setPassengers(List.of("Kovács Béla"), List.of("")); // 1 utas, nincs bérlet

        // Kézzel beállítjuk a választott széket (Kocsi ID: 1, Szék: 5)
        // Global ID számítás: 1 * 1000 + 5 = 1005 (ha a te logikád ez)
        // VAGY ha a PurchaseController getGlobalSeatId-t használod:
        int globalId = p.getGlobalSeatId(1, 5);
        p.setSeatsToReserve(List.of(globalId));

        // Metódus hívása
        trainHandler.reserveSpecificSeats(p);

        // Ellenőrzés: Bekerült-e a jegy a kosárba?
        List<Ticket> tickets = p.getTicketsToBuy();
        assertEquals(1, tickets.size());
        
        Ticket t = tickets.get(0);
        assertTrue(t instanceof Reservation);
        Reservation res = (Reservation) t;
        
        assertEquals("Kovács Béla", res.getPassengerName());
        assertEquals(1, res.getCoach()); // Jó kocsi
        assertEquals(5, res.getSeat());  // Jó szék
    }

    @Test
    void testFinalizeBooking_SeatStatusUpdate() {

        PurchaseController p = new PurchaseController();
        p.setTrain(101);
        p.setStops("Budapest", "Debrecen");
        
        Stop s1 = testTrain.getStopByName("Budapest");
        Stop s2 = testTrain.getStopByName("Debrecen");
        Reservation res = new Reservation(999, 101, 1000, "Gajdos Sándor", s1, s2, 1, 3, null);
        
        p.addTicketToBuy(res);

        // 2. Kocsi állapotának ellenőrzése ELŐTTE
        Coach targetCoach = testTrain.getCoaches().get(0); // ID: 1
        assertFalse(targetCoach.getReservedSeatIds().contains(3), "A 3-as széknek szabadnak kell lennie kezdetben");

        // 3. Finalize hívása
        trainHandler.finalizeBooking(p);

        // 4. Kocsi állapotának ellenőrzése UTÁNA
        assertTrue(targetCoach.getReservedSeatIds().contains(3), "A finalize után a 3-as széknek foglaltnak kell lennie!");
    }

}
