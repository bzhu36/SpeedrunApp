package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ben Zhu on 12/14/2017.
 */

public class SplitsAdd extends Splits {
    public static final int SPLIT_TYPE = 0;
    public static final int ADD_TYPE = 1;

    List<Integer> type;
    SplitsAdd(){
        super();
        type = new ArrayList<>();
        type.add(ADD_TYPE);

    }
    SplitsAdd(int t, HashMap<Integer,Pair<String,String>> s){
        for (int i = 0; i < s.size()-1; i++) {
            type.add(SPLIT_TYPE);
        }
        this.setSplitList(s);
        type.add(ADD_TYPE);
    }

    public int addSplitToEditList(){
        type.remove(type.size()-1);
        type.add(SPLIT_TYPE);
        type.add(ADD_TYPE);
        return type.size()-1;
    }

    @Override
    public void addSplitToList(int position, Pair<String,String> p){
        super.addSplitToList(position, p);
    }


    public int size(){
        return type.size();
    }
}
