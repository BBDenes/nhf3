package train;

import java.util.LinkedList;
import java.util.List;

public class TrainHandler {
    private List<Train> trains;

    public TrainHandler(){
        trains = new LinkedList<>();
    }

    public void readTrains(String path){
        if(path == null){
            //TODO: Beolvasó ablak
            System.out.println("No trains added.");
        }
        this.trains.add(new Train(1, "ASD", "IC", new LinkedList<Coach>(), new LinkedList<Stop>()));
        
    }
    
}
