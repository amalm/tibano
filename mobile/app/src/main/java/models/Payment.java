package models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rame on 09.11.2016.
 */

public class Payment {

    private String paying;
    private String licenseplate;

    public Payment() {
    }

    public Payment(JSONObject object) {

        try {
            this.licenseplate = object.getString("licensePlate");
            this.paying = object.getString("paying");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPaying() {
        return paying;
    }

    public void setPaying(String paying) {
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
