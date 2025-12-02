
# Osztálydiagram

```mermaid
classDiagram
    %% --- CSOMAG: vonatjegy ---
    namespace vonatjegy {
        class Main {
            +main(String[] args)
        }
    }

    %% --- CSOMAG: utilities ---
    namespace utilities {
        class Time {
            -int hour
            -int minute
            +Time(int h, int m)
            +Time(String t)
            +getHour() int
            +getMinute() int
            +compareTo(Time other) int
            +toString() String
        }

        class PurchaseController {
            -int maxSeats
            -String from
            -String to
            -int numOfPassengers
            -List~String~ names
            -List~String~ passIds
            -int trainId
            -boolean isFirstClass
            -List~Boolean~ accessibleMask
            -List~Boolean~ bycicleMask
            -List~Ticket~ ticketsToBuy
            -List~Integer~ seatsToReserve
            +getGlobalSeatId(int coachId, int seatId) int
            +getSeatFromGlobalId(int gId) int[]
            +addTicketToBuy(Ticket t)
            +getTicketsToBuy() List~Ticket~
            +setStops(String from, String to)
            +setPassengers(List~String~ names, List~String~ passIds)
            +setTrain(int trainId)
            +setFirstClass(boolean isFirstClass)
            +setMasks(List~Boolean~ accessibleMask, List~Boolean~ bycicleMask)
            +setSeatsToReserve(List~Integer~ seats)
        }
    }

    %% --- CSOMAG: tickets ---
    namespace tickets {
        class Ticket {
            #int id
            #int trainId
            #int price
            #String passengerName
            #Stop from
            #Stop to
            #Time timeOfLeave
            #Time timeOfArrival
            +Ticket(int id, int trainId, int price, String passengerName, Stop from, Stop to)
            +getTicketType() String
            +writeToHtml()
            #writeRow(FileWriter w, String label, String value)
            #getExtraDetails() String
        }

        class Reservation {
            -int seatNum
            -int coachNum
            -Type type
            -String passId
            +Reservation(...)
            +getSeat() int
            +getCoach() int
            +getPassId() String
            +getExtraDetails() String
        }

        class ReservationType {
            <<enumeration>>
            WITH_PASS
            WITHOUT_PASS
        }
    }

    %% --- CSOMAG: train ---
    namespace train {
        class Train {
            -int id
            -String name
            -String type
            -List~Coach~ coaches
            -List~Stop~ stops
            -List~String~ services
            +Train(...)
            +addCoach(Coach c)
            +removeCoach(Coach c)
            +getStopByName(String stationName) Stop
            +resetReservations()
        }

        class Coach {
            -int id
            -int capacity
            -int bicycleCapacity
            -int wheelchairCapacity
            -int available
            -boolean firstClass
            -boolean buffetCar
            -List~Ticket~ tickets
            -List~Seat~ seats
            -List~Integer~ reservedSeats
            +Coach(...)
            +addTicket(Reservation t)
            +generateAvailableSeat() int
            +resetReservations()
        }

        class Seat {
            -int id
            -int coachId
            -Position position
            -boolean atTable
            -boolean reserved
            -boolean isSelected
            +Seat(int id, int coachId)
            +toggleSelect()
        }

        class SeatPosition {
            <<enumeration>>
            WINDOW
            CORRIDOR
        }

        class Stop {
            -String name
            -Time arrive
            -Time leave
            +Stop(String name, Time arrive, Time leave)
            +Stop(String formattedInput)
        }

        class TrainHandler {
            -List~Train~ trains
            -int ticketID
            +TrainHandler()
            +addTrain(...)
            +searchTrains(String from, String to) List~Train~
            +getTrainByIndex(int id) Train
            +getRandom(int trainId, boolean isFirstClass) int[]
            +reserveAutomaticSeats(PurchaseController p)
            +reserveSpecificSeats(PurchaseController p)
            +finalizeBooking(PurchaseController p)
            +saveTrainsToJson()
            +loadTrainsFromJson()
            +deleteTrainByIndex(int id)
            +resetReservation(int id)
            -buyTicket(...)
            -getSeatByAttribute(...)
        }
    }

    %% --- CSOMAG: ui ---
    namespace ui {
        class ModernComponents {
            +createStyledLabel(...) JLabel
            +createModernButton(...) JButton
            +styleComponent(JComponent comp)
            +createCoachButton(Coach c) JButton
            +createSeatButton(...) JButton
        }

        class MainMenu {
            -JButton buyTicketButton
            -JButton editTrainsButton
            -PurchaseController purchase
            +MainMenu(TrainHandler th)
            -renderMainMenu(TrainHandler th)
        }

        class TicketMenu {
            -List~JTextField~ utasNevMezok
            -JSpinner passengerNumSpinner
            -PurchaseController purchase
            -TrainHandler trainHandler
            +TicketMenu(...)
            -renderSearchMenu()
            +createPassengersMenu() JPanel
            +showTrainListPanel(List~Train~ results)
            +showVisualSeatPanel()
            +showSummaryPanel()
            +processAutomaticReservation()
            +showAttributeSelectionPanel()
        }

        class AddTrainWindow {
            -List~Coach~ coachesToAdd
            -List~Stop~ stopsToAdd
            +AddTrainWindow(...)
            -renderWindow()
            -renderBasicInfo(JPanel p)
            -createSectionPanel(...) JPanel
        }

        class TrainListPanel {
            -TicketMenu controller
            -PurchaseController purchase
            -JComboBox reserveSelector
            +TrainListPanel(...)
            -initUI(List~Train~ results)
            -createTrainCard(...) JPanel
            -handleNavigation()
        }

        class VisualSeatPanel {
            -TicketMenu controller
            -TrainHandler trainHandler
            -PurchaseController purchase
            -List~Integer~ reservedSeatIds
            +VisualSeatPanel(...)
            -initUI()
            -renderSeatMap(...)
        }

        class SummaryPanel {
            -TicketMenu controller
            -PurchaseController purchase
            -TrainHandler trainHandler
            +SummaryPanel(...)
            -initUI()
            -createTicketCard(Ticket t) JPanel
            -finalizePurchase()
        }
    }

    %% KAPCSOLATOK
    Main ..> MainMenu : creates
    Main ..> TrainHandler : creates

    Reservation --|> Ticket : extends
    Reservation ..> ReservationType : uses
    Ticket *-- Stop : has
    Ticket *-- Time : has

    Train *-- Coach : contains
    Train *-- Stop : contains
    Coach *-- Seat : contains
    Coach o-- Ticket : contains
    Seat ..> SeatPosition : uses
    Stop *-- Time : uses

    TrainHandler o-- Train : manages
    TrainHandler ..> PurchaseController : uses
    TrainHandler ..> Ticket : creates
    TrainHandler ..> Reservation : creates

    PurchaseController o-- Ticket : holds

    MainMenu ..> TicketMenu : creates
    MainMenu ..> AddTrainWindow : creates
    MainMenu ..> ModernComponents : uses

    TicketMenu ..> TrainHandler : uses
    TicketMenu ..> PurchaseController : uses
    TicketMenu ..> TrainListPanel : creates/navigates
    TicketMenu ..> VisualSeatPanel : creates/navigates
    TicketMenu ..> SummaryPanel : creates/navigates
    TicketMenu ..> ModernComponents : uses

    AddTrainWindow ..> TrainHandler : uses
    AddTrainWindow ..> Coach : creates
    AddTrainWindow ..> Stop : creates
    AddTrainWindow ..> ModernComponents : uses

    TrainListPanel --> TicketMenu : controls
    TrainListPanel ..> PurchaseController : uses
    TrainListPanel ..> Train : displays
    TrainListPanel ..> ModernComponents : uses

    VisualSeatPanel --> TicketMenu : controls
    VisualSeatPanel ..> TrainHandler : uses
    VisualSeatPanel ..> PurchaseController : uses
    VisualSeatPanel ..> Coach : displays
    VisualSeatPanel ..> ModernComponents : uses

    SummaryPanel --> TicketMenu : controls
    SummaryPanel ..> TrainHandler : uses
    SummaryPanel ..> PurchaseController : uses
    SummaryPanel ..> Ticket : displays
    SummaryPanel ..> ModernComponents : uses
```

# Felhasználói kézikönyv

A program futtatáskor megpróbál beolvasni vonatokat egy fájlból, majd a főmenüben választhatjuk ki, hogy mit szeretnénk csinálni, jegyet venni, vagy a vonatokat kezelni (Admin panel).

## Admin panel

Ennek a menünek a fő funkcionalitása új vonat hozzáadása a meglévőkhöz. A fejlécben adhatjuk meg a vonat nevét, azonosítóját valamint típusát, amit egy legördülő menüből választhatunk ki. A vonatok azonosítója egyedi. Ezalatt, a bal oldalon kocsit, a jobb oldalon megállót adhatunk hozzá a táblázatok alatt található sávok segítségével úgy, hogy a megfelelő adatokat pontosvesszővel elválasztva beírjuk a következőképpen:
- Kocsi hozzáadása: ``` azonosító;férőhely;kerékpárhelyek;akadálymentes helyek;elsőosztály?;büfékosi? ```
	- A két utolsó paraméter értéke `1` amennyiben igaz, `0` amennyiben nem
	- Pl.: a 412-es, elsőosztályú, 20 fős, kerekesszék- és kerékpárszállításra nem alkalmas nem büfékocsit a következőképpen lehet hozzáadni: ``` 412;20;0;0;1;0 ```
- Megálló hozzáadása: ```megálló neve;indulás;érkezés```
	- Az indulási és érkezési idő formátuma: `ÓÓ:PP` 
	- Az indulási idő nem lehet korábbi, mint az érkezési idő, ezt a program ellenőrzi
A hozzáad gombra kattintás után megjelennek a kocsik/megállók a táblázatokban. Törléshez rákattintunk a törölni kívánt megálló vagy kocsi sorára, amit kék színű kijelöléssel jelez a program, majd a törö gombra kattintva törlődik a vonatból.

Ha végeztünk, a vonat mentése gombra kattintva a program elmenti a változtatásokat mind a fájlba, mint a jelenlegi munkamenethez. Lehetőség van még vonatot törölni azonosító alapján, illetve vonat "alaphelyzetbe állítására", szintén azonosító alapján. Ekkor a vonat minden helyfoglalását töröljük, "üressé tesszük" a vonatot.

## Jegyvásárlás

Itt egy menürendszeren keresztül lehet jegyet venni. Nagy vonalakban egy jegyvásárlás menete:

```mermaid
graph TD

Step1(Honnan-Hova állomások, utasok számának megadása);
Step2(Utasok nevének, opcionálisan bérletük számának megadása);
Step3(Vonat, helyfoglalás módjának kiválasztása);
Step3/1(Grafikus helyválasztás/Helytulajdonságok választása);
Step4(Jegyek ellenőrzése, vásárlás);

Step1---Step2---Step3--Ha nem automatikus a helyfoglalás---Step3/1---Step4;
Step3---Step4;

```
A vonat kiválasztásánál választhatunk, hogy első- vagy másodosztályú helyjegyet szeretnénk vásárolni, amennyiben nem személyvonatról van szó, akkor csak menetjegyeket étékesítünk. A helyet háromféleképpen lehet kiválasztani:
- **Automatikus** : A program automatikusan kisorsolja a helyeket(ha nem személyvonatról van szó) 
- **Helytulajdonság választása**: Ekkor a felhasználó a következő ablakban kiválaszthatja minden utasnak, hogy akadálymentes és/vagy kerékpárszállító kocsiba kapjanak helyet. Nem zárják ki egymást, tehát ha valaki nem kér pl. akadálymentes helyet, attól még kaphat olyan kocsiba jegyet.
- **Grafikus helyfoglalás** 
	Ekkor a következő ablakban választhat a felhasználó helyet. a felső sávban a vonat kocsijai közül választhat, természetesen csak azon osztályú kocsikból lehet csak választani, amit az előző menüben megadtunk (azaz ha a 2.soztályú jegyre nyomunk, csak a 2.osztályú kocsikat fogja megjeleníteni a menü). A képernyő közepén pedig az ülések találhatók. 
	
	- A <span style="color:rgb(46, 204, 113)">zöld</span> színű ülések azt jelentik, hogy szabad a hely, le lehet foglalni
	- A <span style="color:rgb(15, 110, 50)">sötétzöld</span> színű ülés jelenti a kiválasztott ülést
	- A <span style="color:red">piros</span> színű ülés már foglalt, oda nem lehet foglalni
	
Miután kiválasztottuk a helyeket, a program egy ellenőrző menübe dob, ahol a megvásárlásra váró jegyek tartalmát és árá tekinthetjük meg. Egy standard, menetjegy nélküli helyjegy ára 650 FT, egy menetjegy 1000 forint. Az elsőosztály felára 2-szeres minden esetben.