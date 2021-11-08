package Course;

import java.util.ArrayList;

public class Schedule {
    
    // Time Format --> "$DAY-$STARTHOUR.$STARTMINUTE-$ENDHOUR.$ENDMINUTE" (0 for Monday, 6 for Sunday)
    private ArrayList<String> timeTable;

    public Schedule() {
        timeTable = new ArrayList<String>();
    }

    public Schedule(ArrayList<String> timeTable) {
        this.timeTable = timeTable;
    }

    public void addTime(String time) {
        timeTable.add(time);
    }

    // public void deleteTime(String time) {
    //     timeTable.remove(time);
    // }

    // public void deleteTime(int index) {
    //     timeTable.remove(index);
    // }

    public ArrayList<String> getTimeTable() {
        return timeTable;
    } 
}
