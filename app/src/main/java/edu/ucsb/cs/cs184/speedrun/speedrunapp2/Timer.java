package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

/**
 * Created by Ben Zhu on 12/10/2017.
 */

public class Timer {
    private String gameName;
    private String catagory;
    private String imageURL;
    private String WRInfo;
    private String PBInfo;

    public String getId() {
        return gameName;
    }
    public String getCatagory() {
        return catagory;
    }
    public String getImageURL() {
        return imageURL;
    }
    public String getWRInfo() {
        return WRInfo;
    }
    public String getPBInfo() {
        return PBInfo;
    }
}

