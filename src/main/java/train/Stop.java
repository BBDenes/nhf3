package train;

import utilities.Time;

public class Stop{
    private String name;
    private Time arrive, leave;

    public Stop(String name, Time arrive, Time leave){
        this.name = name;
        this.arrive = arrive;
        this.leave = leave;
        if (arrive.compareTo(leave) > 0) throw new IllegalArgumentException("Arrival time is earlier than Leave time");
    }
    public Stop(String formattedInput) throws NumberFormatException, IllegalArgumentException{
        String[] split = formattedInput.split(";");
        if(split.length != 3) throw new NumberFormatException("Nem megfelelő számú bemenet!: " + split.length);
        this.name = split[0];
        this.arrive = new Time(split[1]);
        this.leave = new Time(split[2]);
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
