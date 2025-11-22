package train;

import java.util.LinkedList;
import java.util.List;

public class TrainHandler {
    private List<Train> trains;
    private List<String> passengerNames;
    private List<Integer> passengerPassIds; //id=-1, ha nincs

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


    
    
}
