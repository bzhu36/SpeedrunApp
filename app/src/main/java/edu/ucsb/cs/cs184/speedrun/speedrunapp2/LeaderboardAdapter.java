package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.GameList;

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


