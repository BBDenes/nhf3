package utilities;

import java.io.Serializable;

/**
 * Egyszerű időpont osztály, órát és percet tárol.
*/

public class Time implements Comparable<Time>, Serializable{
    int hour, minute;
    public Time(int h, int m){
        this.hour = h;
        this.minute = m;
    }
    public Time(String t){
        String[] split = t.split(":");
        this.hour = Integer.parseInt(split[0]);
        this.minute = Integer.parseInt(split[1]);
        if(hour < 0 || hour > 23 || minute < 0 || minute > 59){
            throw new IllegalArgumentException("Invalid time format: " + t);
        }
    }

    public int getHour(){return hour;}
    public int getMinute(){return minute;}

    /**
     * A comparable interfész miatt kell, időpontok összehasonlítására szolgál.
     * @param other A másik időpont.
     * @return Negatív érték, ha ez az időpont korábbi, pozitív érték, ha későbbi, nulla, ha egyenlő.
     */
    @Override
    public int compareTo(Time other){
        if (this.hour != other.hour) return (this.hour > other.hour)? 1 : -1;
        else if(this.minute != other.minute) return (this.minute > other.minute)? 1: -1;
        return 0; 
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d", hour, minute);
    }

}
