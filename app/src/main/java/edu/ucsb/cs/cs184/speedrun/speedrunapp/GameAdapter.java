package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.data.Link;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.data.Resource;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.data.Videos;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Leaderboard;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.PlacedRun;

//Shows the leaderboard for an individual game
public class GameAdapter extends RecyclerView.Adapter<GameAdapter.gameLeaderboardViewHolder> {

    private LayoutInflater inflater;
    PlacedRun runs[];
    Context context;
    int runSize;

    public GameAdapter(Context context, Leaderboard leaderboard){
        inflater = LayoutInflater.from(context);
        if(leaderboard!=null) {
            runs = leaderboard.getRuns();
            runSize = Array.getLength(runs);
        }
        else{
            runSize=0;
        }
        this.context = context;

    }

    @Override
    public gameLeaderboardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.leaderboardrow, parent, false);
        gameLeaderboardViewHolder holder = new gameLeaderboardViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(gameLeaderboardViewHolder holder, int position) {
        //Used to set the colors for 1st, 2nd, and 3rd
        determineColor(holder, position);


        holder.place.setText((position+1)+ ".");
        String s="";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            s=runs[position].getRun().getPlayers()[0].getName();

        } catch (Exception e) {
            System.out.println("catch");
            e.printStackTrace();
        }
        holder.username.setText(s);

        //Parses the time into a readable format
        String timeString=parseTime(runs[position].getRun().getTimes().getPrimary());
        holder.time.setText(timeString);

        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(runs[position].getRun().getVideos()!=null){
                    if(runs[position].getRun().getVideos().getLinks()!=null) {
                        String link = runs[position].getRun().getVideos().getLinks()[0].getUri();
                        if(link.substring(4,11).equals("youtube")){
                            link = link.substring(32);
                            watchYoutubeVideo(link);
                        }
                        else{
                            watchWebVideo(link);
                        }
                    }
                }
                else{
                   Toast.makeText(context, "eoj", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return runSize;
    }

    public void watchYoutubeVideo(String id) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public void watchWebVideo(String id) {
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(id));
        context.startActivity(webIntent);
    }
    //Parse the completion time of the run. The format is originally like this:
    //PT5H50M12S, which is 5h 50m 12s
    public String parseTime(String timeString){
        String finalString="";
        for (int i = 0; i < timeString.length(); i++){
            if(timeString.charAt(i)=='P'||timeString.charAt(i)=='T'||timeString.charAt(i)=='H'
                    ||timeString.charAt(i)=='M'||timeString.charAt(i)=='S'){
                if(timeString.charAt(i)=='H') {
                    finalString += "h ";
                }
                if(timeString.charAt(i)=='M'){
                    finalString += "m ";
                }
                if(timeString.charAt(i)=='S'){
                    finalString += "s ";
                }
            }
            else{
                finalString+=timeString.charAt(i);
            }
        }
        return finalString;

    }


    public void determineColor(gameLeaderboardViewHolder holder, int position){
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

    class gameLeaderboardViewHolder extends RecyclerView.ViewHolder{
        TextView place;
        TextView username;
        TextView time;
        //TextView platform;

        public gameLeaderboardViewHolder(View itemView) {
            super(itemView);
            place = itemView.findViewById(R.id.placeView);
            username = itemView.findViewById(R.id.usernameView);
            time = itemView.findViewById(R.id.timeView);
            //platform = itemView.findViewById(R.id.platformView);

        }
    }
}


