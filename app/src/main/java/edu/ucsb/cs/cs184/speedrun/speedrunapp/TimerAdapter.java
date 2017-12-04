package edu.ucsb.cs.cs184.speedrun.speedrunapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Collections;
import java.util.List;
import java.util.Timer;

/**
 * Created by Ben Zhu on 12/2/2017.
 */

public class TimerAdapter extends RecyclerView.Adapter<TimerAdapter.myViewHolder> {

    private LayoutInflater inflater;
    List<GameInfo> gameInfos = Collections.emptyList();

    public TimerAdapter(Context context, List<GameInfo> gameInfos){
        inflater = LayoutInflater.from(context);
        this.gameInfos = gameInfos;

    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row, parent, false);
        myViewHolder holder = new myViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        GameInfo current = gameInfos.get(position);
        holder.gameCover.setImageResource(current.iconId);
        holder.gameTitle.setText(current.gameTitle);
        holder.worldRecord.setText(current.worldRecord);

    }

    @Override
    public int getItemCount() {
        return gameInfos.size();
    }

    class myViewHolder extends RecyclerView.ViewHolder{
        ImageView gameCover;
        TextView gameTitle;
        TextView worldRecord;

        public myViewHolder(View itemView) {
            super(itemView);
            gameCover = itemView.findViewById(R.id.gameCover);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            worldRecord = itemView.findViewById(R.id.worldRecord);
        }
    }
}


