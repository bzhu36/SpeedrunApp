package edu.ucsb.cs.cs184.speedrun.speedrunapp.game;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.SpeedRun;

/**
 * Created by giovanni_rojas on 12/2/17.
 */

public class GameList {

    private Game[] data;

    public static GameList withName(String name) throws IOException {
        Gson g = new Gson();
        URL u = new URL(SpeedRun.API_ROOT + "games?name=" + name.replaceAll(" ", "_"));
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.addRequestProperty("User-Agent", SpeedRun.USER_AGENT);
        InputStreamReader r = new InputStreamReader(c.getInputStream());
        GameList l = g.fromJson(r, GameList.class);
        r.close();
        return l;
    }

    public Game[] getGames() {
        return data;
    }

}
