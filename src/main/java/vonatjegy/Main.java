package vonatjegy;
import train.TrainHandler;
import ui.MainMenu;

public class Main {
    public static void main(String[] args) {
        TrainHandler trains = new TrainHandler();
        MainMenu mainMenu = new MainMenu(trains);
        mainMenu.setVisible(true);
    }
}