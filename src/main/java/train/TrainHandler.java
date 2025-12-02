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

/**
 * A vonatkezelő osztály.
 * Ez az osztály kezeli a vonatok adatait, foglalásait
 * és bonyolítja le a tényleges helyfoglalásokat
 * 
 */

public class TrainHandler {

    private final int seatPrice = 650; 
    private final int seatAndTicketPrice = 1000 + seatPrice; 
    private final String DB_FILE = "trains_db.json";

    private List<Train> trains;
    private static int ticketID;


    public TrainHandler(){
        trains = new LinkedList<>();
    }


    public List<Train> getTrains(){
        return trains;
    }

    /** Hozzáad egy új vonatot a rendszerhez, illetve elmenti a változást a fájlba
     * @param id : A vonat azonosítója
     * @param name : A vonat neve
     * @param type : A vonat típusa
     * @param coaches : A vonat kocsijai
     * @param stops : A vonat megállói
     */
    public void addTrain(int id, String name, String type, List<Coach> coaches, List<Stop> stops){
        for (Train t : this.trains) {
            if(t.getId() == id) throw new IllegalArgumentException("Már van ilyen azonosítóval vonat!");
        }
        trains.add(new Train(id, name, type, coaches, stops));
        saveTrainsToJson();
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

    /** Visszaad egy vonatot az azonosítója alapján
     * @param id : A vonat azonosítója
     * @return : A vonat, null ha nincs ilyen
     */
    public Train getTrainByIndex(int id){
        for (Train t : this.trains) {
            if(t.getId() == id) return t;
        }
        return null;
    }

    /** Visszaad egy véletlenszerű szabad helyet a megadott osztályon a megadott vonaton
     * @param trainId : A vonat azonosítója
     * @param isFirstClass : Igaz, ha elsőosztályú jegyet szeretnénk
     * @return : Tömb, első elem a kocsi, második a szék azonosítója
     * @throws Exception : Ha nincs szabad hely a megadott osztályon
     */
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

    /** Lefoglalja a megadott jegyeket automatikusan a megadott vásárlásvezérlő alapján
     * @param p : A vásárlásvezérlő
     * @throws Exception : Ha nincs elérhető hely a megadott feltételekkel
     */
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

    /** Lefoglal egy jegyet a megadott adatok alapján
     * @param p : A vásárlásvezérlő
     * @param passengerName : Az utas neve
     * @param passId : Az utas bérlet azonosítója (ha nincs, üres String)
     * @param seat : Tömb, első elem a kocsi, második a szék azonosítója
     */
    private void buyTicket(PurchaseController p, String passengerName, String passId, int[] seat) {
        Train t = getTrainByIndex(p.getTrainId());
        boolean isFirstClass = p.isFirstClass();
        String fromStation = p.getStops().get(0);
        String toStation = p.getStops().get(1);

        Ticket newTicket;

        if (!t.getType().equals("Személy")) {
            int seatFee = isFirstClass ? 2 * seatPrice : seatPrice;
            
            int finalPrice = seatFee;

            if (passId == null || passId.strip().isEmpty()) {
                int ticketFee = seatAndTicketPrice - seatPrice;

                if(isFirstClass) ticketFee *= 2; 

                finalPrice += ticketFee;
            }

            //seat[0] = Kocsi ID, seat[1] = szék
            newTicket = new Reservation(ticketID++, t.getId(), finalPrice, passengerName, t.getStopByName(fromStation), t.getStopByName(toStation), seat[0], seat[1], passId);
            
        } else {

            if (passId != null && !passId.strip().isEmpty()) {
                System.out.println(passengerName + " rendelkezik bérlettel, személyvonatra nem szükséges jegyvásárlás.");
                return;
            }

            int price = isFirstClass ? 2 * seatAndTicketPrice : seatAndTicketPrice;
            newTicket = new Ticket(ticketID++, t.getId(), price, passengerName, t.getStopByName(fromStation), t.getStopByName(toStation));
        }

        p.getTicketsToBuy().add(newTicket);
    }

    /** Visszaad egy véletlenszerű szabad helyet a megadott osztályon a megadott vonaton a megadott feltételek alapján
     * @param trainId : A vonat azonosítója
     * @param isFirstClass : Igaz, ha elsőosztályú jegyet szeretnénk
     * @param wantsAccessible : Igaz, ha akadálymentes helyet szeretnénk
     * @param wantsBycicle : Igaz, ha kerékpárhelyet szeretnénk
     * @return : Tömb, első elem a kocsi, második a szék azonosítója
     * @throws Exception : Ha nincs szabad hely a megadott osztályon a megadott feltételekkel
     */
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

    /** Lefoglalja a megadott jegyeket konkrét helyekre a megadott vásárlásvezérlő alapján
     * @param p : A vásárlásvezérlő
     */
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

    /** Véglegesíti a foglalást a megadott vásárlásvezérlő alapján
     * @param p : A vásárlásvezérlő
     */
    public void finalizeBooking(PurchaseController p) {
        List<Ticket> tickets = p.getTicketsToBuy();
        Train train = getTrainByIndex(p.getTrainId()); // A vonat, amire foglalunk

        for (Ticket t : tickets) {
            if (t instanceof Reservation) {
                Reservation res = (Reservation) t;
                
                Coach targetCoach = null;
                for (Coach c : train.getCoaches()) {
                    if (c.getId() == res.getCoach()) { 
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

    /** Menti az adatokat JSON fájlba */
    public void saveTrainsToJson() {
        try (Writer writer = new FileWriter(DB_FILE)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            
            gson.toJson(this.trains, writer);
            
            System.out.println("Adatbázis sikeresen mentve: " + DB_FILE);
        } catch (IOException e) {
            System.err.println("Hiba a mentés során: " + e.getMessage());
        }
    }

    /** Betölti az adatokat JSON fájlból */
    public void loadTrainsFromJson() {
        File file = new File(DB_FILE);
        if (!file.exists()) {
            System.out.println("Nincs mentett adatbázis, üres listával indulunk.");
            this.trains = new LinkedList<>();
            return;
        }

        try (Reader reader = new FileReader(DB_FILE)) {
            Gson gson = new Gson();
            
            Type listType = new TypeToken<LinkedList<Train>>(){}.getType();
            
            this.trains = gson.fromJson(reader, listType);
            
            if (this.trains == null) {
                this.trains = new LinkedList<>();
            }
            
            System.out.println("Sikeres betöltés: " + trains.size() + " db vonat.");
            
        } catch (IOException e) {
            System.err.println("Hiba a betöltés során: " + e.getMessage());
            this.trains = new LinkedList<>(); 
        }
    }

    /** Törli a megadott azonosítójú vonatot */
    public void deleteTrainByIndex(int id) {
        int ind = -1;
        for (int i = 0; i < this.trains.size(); i++) {
            if(this.trains.get(i).getId() == id) ind = i;
        }
        if(ind == -1) throw new IllegalArgumentException("Nincs ilyen vonat");
        this.trains.remove(ind);
        saveTrainsToJson();
    }

    /** Visszaállítja a foglalásokat a megadott azonosítójú vonaton */
    public void resetReservation(int id) {
        int ind = -1;
        for (int i = 0; i < this.trains.size(); i++) {
            if(this.trains.get(i).getId() == id) ind = i;
        }
        if(ind == -1) throw new IllegalArgumentException("Nincs ilyen vonat");
        this.trains.get(ind).resetReservations();
        saveTrainsToJson();
    }

}
