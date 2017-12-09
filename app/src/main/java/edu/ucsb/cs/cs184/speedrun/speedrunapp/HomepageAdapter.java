package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.Run;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.run.RunList;

/**
 * Created by giovanni_rojas on 12/8/17.
 */

public class HomepageAdapter extends RecyclerView.Adapter<HomepageAdapter.runViewHolder> {

    private LayoutInflater inflater;
    Run runs[];
    Game game;
    Context context;

    public HomepageAdapter(Context context, RunList runList){
        inflater = LayoutInflater.from(context);
        runs = runList.getRuns();
        this.context=context;

    }

    @Override
    public runViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.homepage_runsrows, parent, false);
        runViewHolder holder = new runViewHolder(view);

        return holder;

    }

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

    @Override
    public void onBindViewHolder(HomepageAdapter.runViewHolder holder, int position) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        holder.gameTitle.setText(runs[position].getGameNames());
        String runTime = parseTime((String) runs[position].getTimes().getPrimary());
        holder.time.setText(runTime);
        System.out.println("something else");
        try {
            game = Game.fromID(holder.gameTitle.getText().toString());
            holder.gameTitle.setText(game.getNames().get("international"));
            URL myUrl = new URL(game.getAssets().getCoverMedium().getUri());
            InputStream inputStream = (InputStream) myUrl.getContent();
            Drawable drawable = Drawable.createFromStream(inputStream, null);
            holder.gameCover.setImageDrawable(drawable);

        }
        catch (Exception e){
            System.out.println("catch");

        }
        holder.gameTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(runs[position].getVideos()!=null){
                    if(runs[position].getVideos().getLinks()!=null) {
                        String link = runs[position].getVideos().getLinks()[0].getUri();
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

    @Override
    public int getItemCount() {
        return Array.getLength(runs);
    }

    class runViewHolder extends RecyclerView.ViewHolder{
        ImageView gameCover;
        TextView gameTitle;
        TextView time;

        public runViewHolder(View itemView) {
            super(itemView);
            gameCover = itemView.findViewById(R.id.gameCover3);
            gameTitle = itemView.findViewById(R.id.gameTitle3);
            time = itemView.findViewById(R.id.time);
        }
    }

}
