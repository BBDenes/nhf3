package train;

import java.util.LinkedList;
import java.util.List;

public class TrainHandler {
    private List<Train> trains;

    public TrainHandler(){
        trains = new LinkedList<>();
    }

    public List<Train> readTrains(String path){
        if(path == null){
            //TODO: Beolvasó ablak
            System.out.println("No trains added.");
        }
        //TODO: Actually beolvasni vonatokat
        return new LinkedList<>();
    }
    
}
