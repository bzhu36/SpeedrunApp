package edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.Speedrun;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Category;

/**
 * Created by giovanni_rojas on 12/4/17.
 */

public class RunList {

    private Run[] data;

    public static RunList forStatus(Category cat, String status) throws IOException {
        Gson g = new Gson();
        URL u = new URL(Speedrun.API_ROOT + "runs?category=" + cat.getId() + "&status=" + status);
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.addRequestProperty("User-Agent", Speedrun.USER_AGENT);
        InputStreamReader r = new InputStreamReader(c.getInputStream());
        RunList l = g.fromJson(r, RunList.class);
        r.close();
        return l;
    }

    public Run[] getRuns() {
        return data;
    }

}
