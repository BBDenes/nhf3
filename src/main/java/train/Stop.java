package train;
import java.io.Serializable;

import utilities.*;

public class Stop implements Serializable{
    private String name;
    private Time arrive, leave;

    public Stop(String name, Time arrive, Time leave){
        this.name = name;
        this.arrive = arrive;
        this.leave = leave;
        if (arrive.compareTo(leave) > 0) throw new IllegalArgumentException("Arrival time is earlier than Leave time");
    }


    public String getName(){return name;}
    public Time getArrive(){return arrive;}
    public Time getLeave(){return leave;}
    @Override
    public String toString() {
        return "Stop: " +
                "name='" + name + '\'' +
                ", arrive=" + arrive +
                ", leave=" + leave;
    }
}
