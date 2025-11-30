package train;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import tickets.Reservation;
import tickets.Ticket;
import utilities.PurchaseController;

public class TrainHandler {

    private final int seatPrice = 650; 
    private final int seatAndTicketPrice = 1000 + seatPrice; 
    private final int simpleTicketPrice = 1000;
    private final String DB_FILE = "trains_db.json";

    private List<Train> trains;
    private static int ticketID;

    //vásárláshoz

    //új vonat létrehozásához
    private int newTrainId;
    private String newTrainString;
    private List<Coach> coachesToAdd;
    private List<Stop> stopsToAdd;



    public TrainHandler(){
        trains = new LinkedList<>();
    }


    public List<Train> getTrains(){
        return trains;
    }


    public void readTrains(String path){
        //TODO: fájlbeolvasás
        if(path == null){
            
            System.out.println("No trains added.");
        }
        this.trains.add(new Train(1, "ASD", "IC", new LinkedList<Coach>(), new LinkedList<Stop>()));
        
    }

    public void addTrain(int id, String name, String type, List<Coach> coaches, List<Stop> stops){
        trains.add(new Train(id, name, type, coaches, stops));
    }

    public List<Train> searchTrains(String from, String to){
        List<Train> selectedTrains = new LinkedList<>();
        for (Train train : this.trains) {
            Stop fstop = train.getStopByName(from);
            Stop tstop = train.getStopByName(to);
            if (fstop != null && tstop != null) {
                List<Stop> stops = train.getStops();
                int fIndex = stops.indexOf(fstop);
                int tIndex = stops.indexOf(tstop);
                if (fIndex != -1 && tIndex != -1 && fIndex < tIndex) {
                    selectedTrains.add(train);
                }
            }
        }
        return selectedTrains;
    }


    public Train getTrainByIndex(int id){
        for (Train t : this.trains) {
            if(t.getId() == id) return t;
        }
        return null;
    }

    public int[] getRandom(int trainId, boolean isFirstClass) throws Exception{
        Train currentTrain = this.getTrainByIndex(trainId);
        List<Coach> validCoaches = new ArrayList<>();
        for (Coach c : currentTrain.getCoaches()) {
            boolean classMatch = (isFirstClass == c.isFirstClass()); 
            boolean hasSpace = (c.getAvailable() > 0);

            if (classMatch && hasSpace) {
                validCoaches.add(c);
            }
        }

        if (validCoaches.isEmpty()) {
            throw new Exception("Nincs szabad hely a kért osztályon ezen a vonaton!");
        }
        Random rand = new Random();
        int randomIndex = rand.nextInt(validCoaches.size());
        Coach selectedCoach = validCoaches.get(randomIndex);
        int seatNum = selectedCoach.generateAvailableSeat();

        int[] result = {selectedCoach.getId(), seatNum}; //coach, seatId

        return result;


    }


    public void reserveAutomaticSeats(PurchaseController p) throws Exception {
        
        List<String> pNames = p.getPassengerNames();
        List<String> pIds = p.getPassIds();
        List<Boolean> accessibleMask = p.getAccessibleMask();
        List<Boolean> bycicleMask = p.getBycicleMask();

        for (int i = 0; i < pNames.size(); i++) {
            String pName = pNames.get(i);
            String passId = pIds.get(i);
            
            int[] seatInfo = (accessibleMask != null && bycicleMask != null) ? this.getSeatByAttribute(p.getTrainId(), p.isFirstClass(), accessibleMask.get(i), bycicleMask.get(i)) : this.getRandom(p.getTrainId(), p.isFirstClass()); 
            buyTicket(p, pName, passId, seatInfo);
            
        }

    }
    
private void buyTicket(PurchaseController p, String passengerName, String passId, int[] seat) {
        Train t = getTrainByIndex(p.getTrainId());
        boolean isFirstClass = p.isFirstClass();
        String fromStation = p.getStops().get(0);
        String toStation = p.getStops().get(1);

        Ticket newTicket;

        if (!t.getType().equals("Személy")) {
            int price = isFirstClass ? 2 * seatPrice : seatPrice;
            
            if (passId == null || passId.strip().isEmpty()) {
                price = seatAndTicketPrice;
            }

            // Fontos: seat[0] = Kocsi ID, seat[1] = szék
            newTicket = new Reservation(ticketID++, t.getId(), price, passengerName, t.getStopByName(fromStation), t.getStopByName(toStation), seat[0], seat[1], passId);
            
        } else {
            int price = isFirstClass ? 2 * seatAndTicketPrice : seatAndTicketPrice;
            newTicket = new Ticket(ticketID++, t.getId(), price, passengerName, t.getStopByName(fromStation), t.getStopByName(toStation));
        }

        p.getTicketsToBuy().add(newTicket);
    }

    private int[] getSeatByAttribute(int trainId, boolean isFirstClass, boolean wantsAccessible, boolean wantsBycicle) throws Exception{
        List<Coach> validCoaches = new ArrayList<>();
        
        for (Coach c : this.getTrainByIndex(trainId).getCoaches()) {

            if (c.getAvailable() <= 0) continue;
            if (c.isFirstClass() != isFirstClass) continue;

            boolean goodForBycicle = !wantsBycicle || (c.getBicycleCapacity() > 0);
            boolean goodForAccess = !wantsAccessible || (c.getWheelchairCapacity() > 0);

            if(goodForAccess && goodForBycicle) validCoaches.add(c);

        }

        if (validCoaches.isEmpty()) {
            throw new Exception("Nincs ilyen kocsiii");
        }
        
        Random rand = new Random();
        int randomIndex = rand.nextInt(validCoaches.size());
        Coach selectedCoach = validCoaches.get(randomIndex);
        int seatNum = selectedCoach.generateAvailableSeat();

        int[] result = {selectedCoach.getId(), seatNum}; //coach, seatId

        return result;
        
    }

    public void reserveSpecificSeats(PurchaseController p) {

        List<Integer> globalSeatIds = p.getSeatsToReserve();
        List<String> names = p.getPassengerNames();
        List<String> passIds = p.getPassIds();


        for (int i = 0; i < names.size(); i++) {
            int globalId = globalSeatIds.get(i);

            int[] seat = p.getSeatFromGlobalId(globalId); //seat[0] = coachId, seat[1] = seatId
            buyTicket(p, names.get(i), passIds.get(i), seat);
            System.out.println("Lefoglalva: " + names.get(i) + " -> Kocsi: " + seat[0] + " Szék: " + seat[1]);

        }
    }

    public void finalizeBooking(PurchaseController p) {
        List<Ticket> tickets = p.getTicketsToBuy();
        Train train = getTrainByIndex(p.getTrainId()); // A vonat, amire foglalunk

        for (Ticket t : tickets) {
            // Csak akkor kell széket állítani, ha ez egy Reservation (Helyjegy)
            if (t instanceof Reservation) {
                Reservation res = (Reservation) t;
                
                // Megkeressük a kocsit ID alapján
                Coach targetCoach = null;
                for (Coach c : train.getCoaches()) {
                    if (c.getId() == res.getCoach()) { // Feltételezve, hogy a Reservation tárolja a kocsi ID-t
                        targetCoach = c;
                        break;
                    }
                }

                if (targetCoach != null) {
                    targetCoach.addTicket(t);
                }
            } else {
                //TODO: Miafasz van ha nem helyjegy? 
            }
        }

        saveTrainsToJson();

        System.out.println("Adatok sikeresen frissítve és elmentve.");
    }


    public void saveTrainsToJson() {
        try (Writer writer = new FileWriter(DB_FILE)) {
            // A setPrettyPrinting() miatt szépen tördelt, olvasható JSON lesz
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            
            // Ez az EGYETLEN sor végzi a varázslatot:
            // Végigmegy a listán, a vonatokon, a kocsikon, a székeken, és mindent átalakít szöveggé.
            gson.toJson(this.trains, writer);
            
            System.out.println("Adatbázis sikeresen mentve: " + DB_FILE);
        } catch (IOException e) {
            System.err.println("Hiba a mentés során: " + e.getMessage());
        }
    }

    public void loadTrainsFromJson() {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            System.out.println("Nincs mentett adatbázis, üres listával indulunk.");
            this.trains = new LinkedList<>();
            return;
        }

        try (Reader reader = new FileReader(DB_FILE)) {
            Gson gson = new Gson();
            
            // Meg kell mondani a Gson-nak, hogy pontosan MILYEN típusú listát várunk vissza
            // (Mivel a List<Train> generikus típus)
            Type listType = new TypeToken<LinkedList<Train>>(){}.getType();
            
            this.trains = gson.fromJson(reader, listType);
            
            // Null check, ha esetleg üres lenne a fájl
            if (this.trains == null) {
                this.trains = new LinkedList<>();
            }
            
            System.out.println("Sikeres betöltés: " + trains.size() + " db vonat.");
            
        } catch (IOException e) {
            System.err.println("Hiba a betöltés során: " + e.getMessage());
            this.trains = new LinkedList<>(); // Hiba esetén üres lista, hogy ne fagyjon le
        }
    }

}
