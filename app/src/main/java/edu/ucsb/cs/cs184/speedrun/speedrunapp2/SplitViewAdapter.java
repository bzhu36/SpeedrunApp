package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;
import static edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitFrag.GOLD;
import static edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitFrag.GREEN;
import static edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitFrag.RED;
import static edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitsAdd.ADD_TYPE;
import static edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitsAdd.SPLIT_TYPE;
/**
 * Created by Ben Zhu on 12/13/2017.
 */

class SplitViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    LayoutInflater inflater;
    long startTime;
    List<splitHolder> splitHolders;
    String[] TIME_FORMATS = {"HH:mm:ss.SSS","HH:mm:ss:SSS","mm:ss.SSS","mm:ss:sss"};
            //"m:ss", "mm:ss", "H:mm:ss","HH:mm:ss", "m:ss.S", "m:ss.SSS", "m:ss:SS","mm:ss.SSS", "mm:ss.SS", "mm:ss.S", "mm:ss:SSS", "mm:ss:SS", "mm:ss:S", "H:mm:ss.SSS", "H:mm:ss.SS", "H:mm:ss.S", "H:mm:ss:SSS", "H:mm:ss:SS", "H:mm:ss:S", "HH:mm:ss.SSS", "HH:mm:ss.SS", "HH:mm:ss.S", "HH:mm:ss:SSS", "HH:mm:ss:SS", "HH:mm:ss:S", };
    List<Pair<String,String>> splits;
    public SplitViewAdapter(Context context, List<Pair<String,String>> splits){
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.splits = splits;
        splitHolders = new ArrayList<>();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.splits_view_row, parent, false);
        splitHolder holder = new splitHolder(view);
        splitHolders.add(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((splitHolder)holder).splitName.setText(splits.get(position).first);
        ((splitHolder)holder).bestTime.setText(splits.get(position).second);
        ((splitHolder)holder).parsedTime = parseStringTime(((splitHolder)holder).bestTime.getText().toString());
    }

    @Override
    public int getItemCount() {
        return splits.size();
    }

    class splitHolder extends RecyclerView.ViewHolder{
        TextView splitName;
        TextView currentTime;
        TextView bestTime;
        long parsedTime;
        public splitHolder(View itemView) {
            super(itemView);
            //splitImage = itemView.findViewById(R.id.imageAddSplits);
            splitName = itemView.findViewById(R.id.nameSplitView);
            currentTime = itemView.findViewById(R.id.timeCurrentSplitView);
            bestTime = itemView.findViewById(R.id.timeBestSplitView);
        }
    }
    public long parseStringTime(String input){
        System.out.println("parsing " + input);
        for(String formatString : TIME_FORMATS){
            try{
                SimpleDateFormat dateFormat = new SimpleDateFormat(formatString);
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                long time = dateFormat.parse(input).getTime();
                System.out.println("parsed " + time);
                return time;
            }catch (ParseException e){
                System.out.println("parsing next");
            }
        }
        return 0;
    }
    public long onTimerStart(long timeInMills){
        startTime = timeInMills;
        return parseStringTime(splits.get(0).second);
    }

    public long onTimerSplit(long updateTime, int position) {
        if(updateTime < parseStringTime(splitHolders.get(position-1).bestTime.getText().toString())){
            int secs = (int) updateTime/1000;
            int mins = secs/60;
            int hours = mins/60;
            mins%=60;
            secs%=60;
            int millis = (int) updateTime%1000;
            if(hours > 0) {
                splits.set(position-1,Pair.create(splits.get(position-1).first,"" + hours + ":" + String.format("%02d", mins) + ":" + String.format("%02d", secs) + "." + String.format("%03d", millis)));
            }
            else if(hours == 0) {
                splits.set(position - 1,Pair.create(splits.get(position-1).first,"" + mins + ":" + String.format("%02d", secs) + "." + String.format("%03d", millis)));
            }
        }
        notifyDataSetChanged();
        try {
            return parseStringTime(splits.get(position).second);

        }catch(IndexOutOfBoundsException e){
            return -1;
        }

    }
    public List<Pair<String,String>> getSplits(){
        return this.splits;
    }
    public void updateLive(String splitTime, int currentSplit, int color){
        splitHolders.get(currentSplit).currentTime.setText(splitTime);
        switch(color){
            case GOLD:
                splitHolders.get(currentSplit).currentTime.setTextColor(context.getResources().getColor(R.color.gold));
                break;
            case GREEN:
                splitHolders.get(currentSplit).currentTime.setTextColor(Color.GREEN);
                break;
            case RED:
                splitHolders.get(currentSplit).currentTime.setTextColor(Color.RED);
                break;
            }
        notifyDataSetChanged();
    }
}
