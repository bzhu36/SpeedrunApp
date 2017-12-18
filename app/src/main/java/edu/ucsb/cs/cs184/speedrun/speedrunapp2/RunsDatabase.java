package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Created by unsun on 12/15/2017.
 */

public class RunsDatabase implements Comparable<RunsDatabase>{
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

    @Override
    public int compareTo(@NonNull RunsDatabase runsDatabase) {
        int comparedTime = Integer.valueOf(removePunct(runsDatabase.getTime()));
        int currentTime = Integer.valueOf(removePunct(this.getTime()));
        return currentTime - comparedTime;
    }

    public String removePunct(String time){
        final StringBuilder builder = new StringBuilder();
        for(final char c : time.toCharArray())
            if(Character.isLetterOrDigit(c))
                builder.append(Character.isLowerCase(c) ? c : Character.toLowerCase(c));
        return builder.toString();
    }
}
