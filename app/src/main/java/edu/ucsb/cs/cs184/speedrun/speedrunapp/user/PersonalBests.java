package edu.ucsb.cs.cs184.speedrun.speedrunapp.user;


import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.SpeedRun;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.PlacedRun;

/**
 * Created by giovanni_rojas on 12/2/17.
 */

public class PersonalBests {

    private PlacedRun[] data;

    public PlacedRun[] getData() {
        return data;
    }

    public static PersonalBests forUser(User user) throws IOException {
        Gson g = new Gson();
        URL u = new URL(SpeedRun.API_ROOT + "users/" + user.getId() + "/personal-bests");
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestProperty("User-Agent", SpeedRun.USER_AGENT);
        InputStreamReader r = new InputStreamReader(c.getInputStream());
        PersonalBests pb = g.fromJson(r, PersonalBests.class);
        r.close();
        return pb;
    }

}
