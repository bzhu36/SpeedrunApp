package edu.ucsb.cs.cs184.speedrun.speedrunapp.game;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.SpeedRun;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.data.Link;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.PlacedRun;

/**
 * Created by giovanni_rojas on 12/2/17.
 */

public class Leaderboard {

    private String weblink;
    private String game;
    private String category;
    private String level;
    private String platform;
    private String region;
    private boolean emulators;
    @SerializedName("video-only")
    private boolean videoOnly;
    private String timing;
    private Map<String, String> values;
    private PlacedRun[] runs;
    private Link[] links;

    public static Leaderboard forCategory(Category c) throws IOException {
        Gson g = new Gson();
        URL u = new URL(SpeedRun.API_ROOT + "leaderboards/" + c.getGame().getId() + "/category/" + c.getId());
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setRequestProperty("User-Agent", SpeedRun.USER_AGENT);
        InputStreamReader r = new InputStreamReader(conn.getInputStream());
        LeaderboardData l = g.fromJson(r, LeaderboardData.class);
        r.close();
        return l.data;
    }

    public String getLevel() {
        return level;
    }

    public boolean isEmulators() {
        return emulators;
    }

    public boolean isVideoOnly() {
        return videoOnly;
    }

    public String getTiming() {
        return timing;
    }

    public PlacedRun[] getRuns() {
        return runs;
    }

    public Link[] getLinks() {
        return links;
    }

    private static class LeaderboardData {
        Leaderboard data;
    }

}
