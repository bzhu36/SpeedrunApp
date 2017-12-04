package edu.ucsb.cs.cs184.speedrun.speedrunapp;

/**
 * Created by Ben Zhu on 12/2/2017.
 */

public class GameInfo {
    int iconId;
    String gameTitle;
    String worldRecord;

    GameInfo(int i, String g, String w){
        this.iconId = i;
        this.gameTitle = g;
        this.worldRecord = w;
    }
}
