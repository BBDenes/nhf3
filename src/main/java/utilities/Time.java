package utilities;

import java.io.Serializable;

public class Time implements Comparable<Time>, Serializable{
    int hour, minute;
    public Time(int h, int m){
        this.hour = h;
        this.minute = m;
    }

    public int getHour(){return hour;}
    public int getMinute(){return minute;}

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
