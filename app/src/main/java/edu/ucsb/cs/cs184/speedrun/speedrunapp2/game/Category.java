package edu.ucsb.cs.cs184.speedrun.speedrunapp2.game;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.Speedrun;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.data.Link;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.run.Playtype;

/**
 * Created by giovanni_rojas on 12/4/17.
 */

public class Category {

    private String id;
    private String name;
    private String weblink;
    private String type;
    private String rules;
    private Playtype players;
    private boolean miscellaneous;
    private Link[] links;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWeblink() {
        return weblink;
    }

    public String getType() {
        return type;
    }

    public String getRules() {
        return rules;
    }

    public Playtype getPlayers() {
        return players;
    }

    public boolean isMiscellaneous() {
        return miscellaneous;
    }

    public Link[] getLinks() {
        return links;
    }

    public Game getGame() throws IOException {
        String uri = Arrays.stream(links).filter((Link l) -> "game".equals(l.getRel())).findFirst().get().getUri();
        return Game.fromID(uri.substring(uri.lastIndexOf('/') + 1));
    }

    public static Category fromID(String id) throws IOException {
        Gson g = new Gson();
        URL u = new URL(Speedrun.API_ROOT + "categories/" + id);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestProperty("User-Agent", Speedrun.USER_AGENT);
        InputStreamReader r = new InputStreamReader(c.getInputStream());
        CategoryData cd = g.fromJson(r, CategoryData.class);
        r.close();
        return cd.data;
    }

    private static class CategoryData {
        Category data;
    }


}
