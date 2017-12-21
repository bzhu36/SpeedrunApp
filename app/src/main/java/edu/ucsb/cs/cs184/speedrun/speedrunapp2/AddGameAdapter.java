package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.app.Activity;
import android.app.FragmentManager;
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

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.GameFragment;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.MainActivity;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.GameList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Leaderboard;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.SplitFrag;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.R;

public class AddGameAdapter extends RecyclerView.Adapter<AddGameAdapter.gameViewHolder> {

    private LayoutInflater inflater;
    Game games[];
    Context context;

    public AddGameAdapter(Context context, GameList gameLists){
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
        Picasso.with(context).load(games[position].getAssets().getCoverLarge().getUri()).into(holder.gameCover,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.position = position;
                holder.onClickListenerCall();
            }

            @Override
            public void onError() {

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
        int position;

        public gameViewHolder(View itemView) {
            super(itemView);
            gameCover = itemView.findViewById(R.id.gameCover1);
            gameTitle = itemView.findViewById(R.id.gameTitle1);
        }
        public void onClickListenerCall(){
            System.out.println("gamecover"+gameCover.getDrawable() + gameCover);
            itemView.setOnClickListener( new GameOnClickListener(position, gameTitle.getText().toString(), gameCover.getDrawable()));
        }
    }
    class GameOnClickListener implements View.OnClickListener{

        String gameTitle;
        Drawable drawable;
        int position;
        GameOnClickListener(int position, String gameTitle, Drawable drawable){
            this.gameTitle = gameTitle;
            this.drawable = drawable;
            this.position = position;
        }
        @Override
        public void onClick(View view) {
            MainActivity mainActivity=(MainActivity)context;
            FragmentTransaction ft = mainActivity.getFragmentManager().beginTransaction().addToBackStack(null);
            System.out.println(drawable);
            AddGameCategoryFrag.newInstance(games[position],drawable).show(ft,"");
        }
    }
}


