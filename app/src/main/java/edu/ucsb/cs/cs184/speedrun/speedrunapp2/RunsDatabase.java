package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import java.util.ArrayList;

/**
 * Created by unsun on 12/15/2017.
 */

public class RunsDatabase {
    private String game;
    private String name;
    private String userid;
    private String date;
    private String category;
    private String Icon;
    private String time;
    private ArrayList<String> splits;

    public RunsDatabase(){}

    public RunsDatabase(String game,String name,String userid, String date, String category,
                        String icon, String time, ArrayList<String> splits){
        this.game = game;
        this.name = name;
        this.userid = userid;
        this.date = date;
        this.category = category;
        this.Icon = icon;
        this.time = time;
        this.splits = splits;
    }


    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIcon() {
        return Icon;
    }

    public void setIcon(String icon) {
        this.Icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<String> getSplits() {
        return splits;
    }

    public void setSplits(ArrayList<String> splits) {
        this.splits = splits;
    }
}
