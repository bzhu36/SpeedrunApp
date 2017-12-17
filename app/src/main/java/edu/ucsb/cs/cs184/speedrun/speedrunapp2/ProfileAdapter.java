package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by unsun on 12/12/2017.
 */

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.runListViewHolder> {
    private LayoutInflater inflater;
    private int size;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    ArrayList<RunsDatabase> runs;
    Context context;

    public ProfileAdapter(Context context,ArrayList<RunsDatabase> runs){
        if (context != null){
            inflater = LayoutInflater.from(context);
        }
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (runs == null){
            size = 0;
        }
        else {
            this.context = context;
            this.runs = runs;
            this.size = runs.size();
        }
    }


    @Override
    public runListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.homepage_runsrows, parent, false);
        runListViewHolder holder = new runListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(runListViewHolder holder, int position) {
        holder.gameTitle.setText(runs.get(position).getGame());
        Picasso.with(context).load(runs.get(position).getIcon()).into(holder.gameCover);
        holder.time.setText(runs.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return size;
    }

    class runListViewHolder extends RecyclerView.ViewHolder{
        ImageView gameCover;
        TextView gameTitle;
        TextView time;

        public runListViewHolder(View itemView) {
            super(itemView);
            gameCover = itemView.findViewById(R.id.gameCover3);
            gameTitle = itemView.findViewById(R.id.gameTitle3);
            time = itemView.findViewById(R.id.time);
        }
    }
}
