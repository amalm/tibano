package models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rame on 09.11.2016.
 */

public class Area {

    private String name;
    private String capacity;
    private String occupied;
    private String runningPayments;

    public Area(JSONObject object) {

        try {
            this.name = object.getString("name");
            this.capacity = object.getString("capacity");
            this.occupied = object.getString("occupied");
            this.runningPayments = object.getString("runningPayments");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getOccupied() {
        return occupied;
    }

    public void setOccupied(String occupied) {
        this.occupied = occupied;
    }

    public String getRunningPayments() {
        return runningPayments;
    }

    public void setRunningPayments(String runningPayments) {
        this.runningPayments = runningPayments;
    }

    @Override
    public String toString() {
        return "Area " + name +
                " (" + capacity + ")" +
                " paid " + runningPayments + " (" + occupied + ")";
    }

}
