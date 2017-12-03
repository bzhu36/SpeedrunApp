package edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run;

import com.google.gson.annotations.SerializedName;

/**
 * Created by giovanni_rojas on 12/2/17.
 */

public class Ruleset {

    @SerializedName("show-milliseconds") private boolean showMilliseconds;
    @SerializedName("require-verification") private boolean requireVerification;
    @SerializedName("require-video") private boolean requireVideo;
    @SerializedName("run-times") private String[] runTimes;
    @SerializedName("default-time") private String defaultTime;
    @SerializedName("emulators-allowed") private boolean emulatorsAllowed;

}
