package edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run;

import android.graphics.Color;

import java.io.IOException;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.user.User;

/**
 * Created by giovanni_rojas on 12/2/17.
 */

public class Player {

    private String rel;
    private String id;
    private String name;
    private String uri;

    public String getName() throws IOException{
        if(id != null)
            return User.fromID(id).getNames().get("international");
        else
            return name;
    }

//    public Color getColor() throws IOException{
//        if(id != null)
//            return User.fromID(id).getNameStyle().getColor();
//        else
//            return Color.WHITE;
//    }

}
