package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by icema on 12/10/2017.
 */

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.followListViewHolder> {
    private LayoutInflater inflater;
    private ArrayList<UserDatabase> userDatabases;
    private int size;
    private ArrayList<String> keys;
    private ArrayList<Boolean> following;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private boolean followed = true;
    Context context;

    public FriendsListAdapter(Context context, ArrayList<UserDatabase> userDatabases, ArrayList<String> keys) {
        if(context!=null) {
            inflater = LayoutInflater.from(context);
        }
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        following = new ArrayList<>();
        if (userDatabases == null) {
            size = 0;
        } else {
            this.context = context;
            this.userDatabases = userDatabases;
            this.keys = keys;
            for(int i=0; i<userDatabases.size(); i++){
                following.add(i, true);
            }
            size = userDatabases.size();
        }

    }

    @Override
    public followListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friends_row, parent, false);
        followListViewHolder holder = new followListViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(followListViewHolder holder, int position) {
        holder.username.setText(userDatabases.get(position).getName());
        if(following.get(position)==true){
            holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
            holder.followButton.setText("Unfollow");
            holder.followButton.setTextColor(Color.WHITE);
        }
        holder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add the searched user to the current user's followers in the database
                if (following.get(position)==false) {
                }
                //Delete the searched user from the current user's followers
                else {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("friends/" + user.getUid() + "/" + keys.get(position));
                    ref.removeValue();
                }
            }
        });
        holder.username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity mainActivity=(MainActivity)context;
                FragmentTransaction ft = mainActivity.getFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, ProfileFragment.newInstance(keys.get(position), userDatabases.get(position).getName())).addToBackStack(null).commit();
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return size;
    }

    class followListViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        Button followButton;

        public followListViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.displayName);
            followButton = itemView.findViewById(R.id.follow);
        }
    }
}