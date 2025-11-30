package vonatjegy;
import java.util.ArrayList;
import java.util.List;

import train.Coach;
import train.Stop;
import train.TrainHandler;
import ui.MainMenu;

public class Main {
    public static void main(String[] args) {
        TrainHandler trains = new TrainHandler();

        // Stop s1 = new Stop("Budapest;10:10;10:10");
        // Stop s2 = new Stop("Debrecen;20:10;20:10");
        // Stop s3 = new Stop("Budapest;11:10;11:10");
        // Stop s4 = new Stop("Debrecen;21:10;21:10");
        // Coach c1 = new Coach(411, 20, 0, 1, false, false);
        // Coach c2 = new Coach(412, 40, 0, 0, false, true);

        // trains.addTrain(920, "Savaria nincskocsi", "Személy", List.of(c1), List.of(s1, s2));
        // trains.addTrain(69, "Esku jo nincsbicikli", "IC", List.of(c1, c2), List.of(s3, s4));
        trains.loadTrainsFromJson();
        MainMenu mainMenu = new MainMenu(trains);
        mainMenu.setVisible(true);
    }
}