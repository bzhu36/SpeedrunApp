package edu.ucsb.cs.cs184.speedrun.speedrunapp2.game;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.Speedrun;

/**
 * Created by giovanni_rojas on 12/4/17.
 */

public class CategoryList {

    private Category[] data;

    public static CategoryList forGame(Game g) throws IOException{
        Gson gson = new Gson();
        URL u = new URL(Speedrun.API_ROOT + "games/" + g.getId() + "/categories");
        HttpURLConnection c = (HttpURLConnection) u.openConnection();
        c.setRequestProperty("User-Agent", Speedrun.USER_AGENT);
        InputStreamReader r = new InputStreamReader(c.getInputStream());
        CategoryList cl = gson.fromJson(r, CategoryList.class);
        r.close();
        return cl;
    }

    public Category[] getCategories(){
        return data;
    }


}
