package edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run;

import java.io.IOException;
import java.util.Map;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.data.Link;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.data.Videos;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Category;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.platform.System;

/**
 * Created by giovanni_rojas on 12/5/17.
 */

public class Run {

    private String id;
    private String weblink;
    private String game;
    private String level;
    private String category;
    private Videos videos;
    private String comment;
    private Status status;
    private Player[] players;
    private String date;
    private String submitted;
    private Timeset times;
    private System system;
    private Link splits;
    private Map<String, String> values;

    public String getId() {
        return id;
    }

    public String getWeblink() {
        return weblink;
    }

    public Videos getVideos() {
        return videos;
    }

    public String getComment() {
        return comment;
    }

    public Status getStatus() {
        return status;
    }

    public Player[] getPlayers() {
        return players;
    }

    public String getDate() {
        return date;
    }

    public String getSubmitted() {
        return submitted;
    }

    public Timeset getTimes() {
        return times;
    }

    public System getSystem() {
        return system;
    }

    public Link getSplits() {
        return splits;
    }

    public Link[] getLinks() {
        return links;
    }

    public Category getCategory() throws IOException {
        return Category.fromID(category);
    }

    public String getGameNames(){ return game;}

    private Link[] links;

}
