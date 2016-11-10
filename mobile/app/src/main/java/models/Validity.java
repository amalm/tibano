package models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rame on 09.11.2016.
 */

public class Validity {

    private String valid;

    public Validity() {
    }

    public Validity(JSONObject object) {

        try {
            this.valid = object.getString("valid");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "Validity{" +
                "valid='" + valid + '}';
    }
}
