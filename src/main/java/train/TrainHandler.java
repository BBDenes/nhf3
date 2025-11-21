package train;

import java.util.LinkedList;
import java.util.List;

public class TrainHandler {
    private List<Train> trains;

    public TrainHandler(){
        trains = new LinkedList<>();
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

    
}
