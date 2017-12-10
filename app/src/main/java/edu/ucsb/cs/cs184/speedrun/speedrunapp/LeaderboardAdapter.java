package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.StrictMode;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Timer;

import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.GameList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp.game.Leaderboard;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.gameViewHolder> {

    private LayoutInflater inflater;
    Game games[];
    Context context;

    public LeaderboardAdapter(Context context, GameList gameLists){
        inflater = LayoutInflater.from(context);
        games = gameLists.getGames();
        this.context=context;

    }

    @Override
    public gameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.gamerow_test, parent, false);
        gameViewHolder holder = new gameViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(gameViewHolder holder, int position) {
        holder.gameTitle.setText(games[position].getNames().get("international"));
        Picasso.with(context).load(games[position].getAssets().getCoverMedium().getUri()).into(holder.gameCover);
        holder.gameTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable2 = holder.gameCover.getDrawable();
                MainActivity mainActivity=(MainActivity)context;
                FragmentTransaction ft = mainActivity.getFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, GameFragment.newInstance(games[position], drawable2)).addToBackStack(null).commit();
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return Array.getLength(games);
    }

    class gameViewHolder extends RecyclerView.ViewHolder{
        ImageView gameCover;
        TextView gameTitle;

        public gameViewHolder(View itemView) {
            super(itemView);
            gameCover = itemView.findViewById(R.id.gameCover1);
            gameTitle = itemView.findViewById(R.id.gameTitle1);
        }
    }
}


