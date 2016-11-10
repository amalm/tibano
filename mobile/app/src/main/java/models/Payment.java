package models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rame on 09.11.2016.
 */

public class Payment {

    private boolean paying;
    private String licenseplate;

    public Payment() {
    }

    public Payment(JSONObject object) {

        try {
            this.licenseplate = object.getString("licensePlate");
            this.paying = object.getBoolean("paying");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public boolean getPaying() {
        return paying;
    }

    public void setPaying(boolean paying) {
        this.paying = paying;
    }

    public String getLicenseplate() {
        return licenseplate;
    }

    public void setLicenseplate(String licenseplate) {
        this.licenseplate = licenseplate;
    }


    @Override
    public String toString() {
        return licenseplate + " paid:" + paying;
    }
}
