package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben Zhu on 12/10/2017.
 */

public class Splits {
    private HashMap<Integer, Pair<String,String> > splitList;
    Splits(){
        this.splitList = new HashMap<>();
    }
    public void setSplitList(HashMap<Integer,Pair<String,String>> s){
        this.splitList = s;
    }
    public HashMap getSplitList(){
        return this.splitList;
    }

    public void addSplitToList(int position, Pair<String,String> p){
        this.splitList.put(position,p);
    }

    public String getPair(int which, int position){
        Pair<String,String> val = this.splitList.get(position);
        if(val == null)
            return "";
        switch (which){
            case 0:
                return val.first;
            case 1:
                return val.second;
        }
        return "";
    }

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < splitList.size()-1; i++) {
            stringBuilder.append("" + splitList.get(i).first + " " + splitList.get(i).second + "\n");
        }
        return stringBuilder.toString();
    }
}
