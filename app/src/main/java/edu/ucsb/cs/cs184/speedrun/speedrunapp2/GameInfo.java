package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.support.annotation.Nullable;

/**
 * Created by Ben Zhu on 12/2/2017.
 */

public class GameInfo {
    public static final int GAME_TYPE = 0;
    public static final int ADD_TYPE = 1;

    String uri;
    String gameTitle;
    String category;
    int type;
    GameInfo(@Nullable String i,@Nullable String g,@Nullable String w, int t){
        this.uri = i;
        this.gameTitle = g;
        this.category = w;
        this.type = t;
    }
}