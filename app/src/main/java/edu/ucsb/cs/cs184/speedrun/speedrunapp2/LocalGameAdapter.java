package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by unsun on 12/18/2017.
 */

public class LocalGameAdapter extends RecyclerView.Adapter<LocalGameAdapter.gameLeaderboardViewHolder> {

    private LayoutInflater inflater;
    ArrayList<RunsDatabase> runs;
    Context context;
    int runSize;

    public LocalGameAdapter(Context context, ArrayList<RunsDatabase> gameLeaderboard){
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.runs = gameLeaderboard;
        if(runs== null){
            runSize = 0;
        }
        else {
            runSize = runs.size();
        }
    }

    @Override
    public LocalGameAdapter.gameLeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.leaderboardrow, parent, false);
        LocalGameAdapter.gameLeaderboardViewHolder holder = new LocalGameAdapter.gameLeaderboardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(LocalGameAdapter.gameLeaderboardViewHolder holder, int position) {
        determineColor(holder,position);
        holder.time.setText(runs.get(position).getTime());
        holder.place.setText(position + 1 + ".");
        holder.username.setText(runs.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return runSize;
    }

    class gameLeaderboardViewHolder extends RecyclerView.ViewHolder{
        TextView place;
        TextView username;
        TextView time;


        public gameLeaderboardViewHolder(View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.placeView);
            username = itemView.findViewById(R.id.usernameView);
            time = itemView.findViewById(R.id.timeView);


        }
    }
    public void determineColor(LocalGameAdapter.gameLeaderboardViewHolder holder, int position){
        switch (position){
            case 0:
                holder.place.setTextColor(ContextCompat.getColor(context, R.color.gold));
                holder.username.setTextColor(ContextCompat.getColor(context, R.color.gold));
                holder.time.setTextColor(ContextCompat.getColor(context, R.color.gold));
                break;
            case 1:
                holder.place.setTextColor(ContextCompat.getColor(context, R.color.silver));
                holder.username.setTextColor(ContextCompat.getColor(context, R.color.silver));
                holder.time.setTextColor(ContextCompat.getColor(context, R.color.silver));
                break;
            case 2:
                holder.place.setTextColor(ContextCompat.getColor(context, R.color.bronze));
                holder.username.setTextColor(ContextCompat.getColor(context, R.color.bronze));
                holder.time.setTextColor(ContextCompat.getColor(context, R.color.bronze));
                break;
            default:
                holder.place.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.username.setTextColor(ContextCompat.getColor(context, R.color.white));
                holder.time.setTextColor(ContextCompat.getColor(context, R.color.white));
                break;
        }
    }
}
