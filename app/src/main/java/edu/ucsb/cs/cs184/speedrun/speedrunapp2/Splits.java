package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ben Zhu on 12/10/2017.
 */

public class Splits {
    private ArrayList<Pair<String,String>> splitList;
    Splits(){
        this.splitList = new ArrayList<>();
    }
    public void setSplitList(ArrayList<Pair<String,String>> s){
        this.splitList = s;
    }
    public ArrayList<Pair<String,String>> getSplitList(){
        return splitList;
    }

    public void addSplitToList(int position, Pair<String,String> p) {
        try{
            this.splitList.set(position, p);
        } catch(IndexOutOfBoundsException e){
            this.splitList.add(p);
        }
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
        for (int i = 0; i < splitList.size(); i++) {
            stringBuilder.append("" + splitList.get(i).first + " " + splitList.get(i).second + "\n");
        }
        return stringBuilder.toString();
    }
}
