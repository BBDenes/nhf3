package train;

import java.util.List;

public class Train {
    public static int ticketId = 1;

    private int id; //a vonat azonosítója, egyedi
    private String name;
    private String type;
    private List<Coach> coaches;
    private List<Stop> stops;
    private List<String> services;

    public Train(int id, String name,String type, List<Coach> coaches, List<Stop> stops){
        this.id = id;
        this.type = type;
        this.name = name;
        this.coaches = coaches;
        this.stops = stops;

        for (Coach c : this.coaches) {
            if(c.isFirstClass()) if(!services.contains("First class coach")) services.add("First class coach");
            if(c.isBuffetCar()) if(!services.contains("Buffet coach")) services.add("Buffet coach");
        }
    }

    public int getId(){return id;}
    public String getName(){return name;}
    public String getType(){return type;}
    public List<Coach> getCoaches(){return coaches;}
    public List<Stop> getStops(){return stops;}

    public void addCoach(Coach c){
        if(c != null){
            this.coaches.add(c);
        }else{throw new IllegalArgumentException("Coach is null!");}
    }

    public void setName(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Train name is empty!");
        }
        this.name = name;
    }

    public void removeCoach(Coach c){
        if(c == null) throw new IllegalArgumentException("Coach is null!");
        this.coaches.remove(c);
    }

    public void removeCoach(int i){
        if (i >= 0 && i < coaches.size()) {
            this.coaches.remove(i);
        }
    }

    @Override
    public String toString() {
        return id + " " + name + " "+ type + "- Kocsik: " + coaches.toString() + ", Megállók: " + stops.toString();
    }

    public Stop getStopByName(String stationName) {
    for (Stop s : this.stops) {
        if (s.getName().equalsIgnoreCase(stationName)) {
            return s;
        }
    }
    return null; 
}

}
