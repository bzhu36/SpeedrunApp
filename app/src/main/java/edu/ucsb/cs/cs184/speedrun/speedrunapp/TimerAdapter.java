package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import static edu.ucsb.cs.cs184.speedrun.speedrunapp.GameInfo.ADD_TYPE;
import static edu.ucsb.cs.cs184.speedrun.speedrunapp.GameInfo.GAME_TYPE;

/**
 * Created by Ben Zhu on 12/2/2017.
 */

public class TimerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private LayoutInflater inflater;
    List<GameInfo> gameInfos = Collections.emptyList();

    public TimerAdapter(Context context, List<GameInfo> gameInfos) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.gameInfos = gameInfos;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case GAME_TYPE:
                view = inflater.inflate(R.layout.row, parent, false);
                return new gameHolder(view);
            case ADD_TYPE:
                view = inflater.inflate(R.layout.add_row, parent, false);
                return new addHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GameInfo current = gameInfos.get(position);
        switch (getItemViewType(position)) {
            case GAME_TYPE:
                ((gameHolder) holder).gameCover.setImageResource(current.iconId);
                ((gameHolder) holder).gameTitle.setText(current.gameTitle);
                ((gameHolder) holder).worldRecord.setText(current.worldRecord);

                break;
            case ADD_TYPE:
                break;
        }


    }

    @Override
    public int getItemCount() {
        return gameInfos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return gameInfos.get(position).type;
    }


    class gameHolder extends RecyclerView.ViewHolder {
        ImageView gameCover;
        TextView gameTitle;
        TextView worldRecord;

        public gameHolder(View itemView) {
            super(itemView);
            gameCover = itemView.findViewById(R.id.gameCover);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            worldRecord = itemView.findViewById(R.id.worldRecord);
            GameOnClickListener listener = new GameOnClickListener(gameCover.getId(),gameTitle.getText().toString(),worldRecord.getText().toString());
            itemView.setOnClickListener(listener);
        }


    }

    class addHolder extends RecyclerView.ViewHolder {
        public addHolder(View itemView) {
            super(itemView);

            AddOnClickListener listener = new AddOnClickListener();
            itemView.setOnClickListener(listener);
        }
    }

    class GameOnClickListener implements View.OnClickListener{

        int gameCover;
        String gameTitle;
        String worldRecord;
        GameOnClickListener(int gameCover, String gameTitle, String worldRecord){
            this.gameCover = gameCover;
            this.gameTitle = gameTitle;
            this.worldRecord = worldRecord;

        }
        @Override
        public void onClick(View v){
            FragmentManager manager = ((Activity) context).getFragmentManager();
            SplitFrag popupFrag = new SplitFrag();
//            Bundle args = new Bundle();
//            args.putString("Uri",uri);
//            args.putInt("Rating", rating);
//            popupFrag.setArguments(args);

            popupFrag.show(manager,"SplitFrag");

            }
    }

    class AddOnClickListener implements View.OnClickListener{

        AddOnClickListener(){


        }
        @Override
        public void onClick(View v){
            FragmentManager manager = ((Activity) context).getFragmentManager();
            AddGameFrag popupFrag = new AddGameFrag();
//            Bundle args = new Bundle();
//            args.putString("Uri",uri);
//            args.putInt("Rating", rating);
//            popupFrag.setArguments(args);

            popupFrag.show(manager,"popupfrag");
        }
    }
}


