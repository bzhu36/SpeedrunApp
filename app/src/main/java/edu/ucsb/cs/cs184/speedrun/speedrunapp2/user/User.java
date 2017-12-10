package edu.ucsb.cs.cs184.speedrun.speedrunapp2.user;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.Speedrun;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.data.Link;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.data.Resource;

/**
 * Created by giovanni_rojas on 12/5/17.
 */

public class User {

    private String id;
    private Map<String, String> names;
    private String weblink;
    @SerializedName("name-style") private NameStyle nameStyle;
    private String role;
    private String signup;
    private Location location;
    private Resource twitch;
    private Resource hitbox;
    private Resource youtube;
    private Resource twitter;
    private Resource speedrunslive;
    private Link[] links;

    public static User fromID(String id) throws IOException {
        Gson g = new Gson();
        URL u = new URL(Speedrun.API_ROOT + "users/" + id);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.addRequestProperty("User-Agent", Speedrun.USER_AGENT);
        InputStreamReader r = new InputStreamReader(c.getInputStream());
        UserData d = g.fromJson(r, UserData.class);
        r.close();
        return d.data;
    }

    public static User fromApiKey(String key) throws IOException {
        Gson g = new Gson();
        URL u = new URL(Speedrun.API_ROOT + "profile");
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.addRequestProperty("User-Agent", Speedrun.USER_AGENT);
        c.addRequestProperty("X-Api-Key", key);
        InputStreamReader r = new InputStreamReader(c.getInputStream());
        UserData d = g.fromJson(r, UserData.class);
        r.close();
        return d.data;
    }

    public String getId() {
        return id;
    }

    /**
     * Valid keys: "international", "japanese"
     * Names can be null
     * @return A map of locales to user names
     */
    public Map<String, String> getNames() {
        return names;
    }

    public String getWeblink() {
        return weblink;
    }

    public NameStyle getNameStyle() {
        return nameStyle;
    }

    public String getRole() {
        return role;
    }

    public String getSignup() {
        return signup;
    }

    public Location getLocation() {
        return location;
    }

    public Resource getTwitch() {
        return twitch;
    }

    public Resource getHitbox() {
        return hitbox;
    }

    public Resource getYoutube() {
        return youtube;
    }

    public Resource getTwitter() {
        return twitter;
    }

    public Resource getSpeedrunslive() {
        return speedrunslive;
    }

    public Link[] getLinks() {
        return links;
    }

    public PersonalBests getPBs() throws IOException {
        return PersonalBests.forUser(this);
    }

    private static class UserData{User data;}

}
