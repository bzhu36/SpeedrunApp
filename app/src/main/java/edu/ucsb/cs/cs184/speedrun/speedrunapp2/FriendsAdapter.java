package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by icema on 12/10/2017.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.friendsViewHolder> {
    private LayoutInflater inflater;
    private UserDatabase userDatabase;
    private int size;
    private String key;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private boolean followed=false;
    Context context;

    public FriendsAdapter(Context context, UserDatabase userDatabase, String key){

        inflater = LayoutInflater.from(context);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if(userDatabase==null){
            size=0;
        }
        else {
            this.context = context;
            this.userDatabase = userDatabase;
            this.key = key;
            size=1;
        }

    }

    @Override
    public friendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.friends_row, parent, false);
        friendsViewHolder holder = new friendsViewHolder(view);

        return holder;

    }

    @Override
    public void onBindViewHolder(friendsViewHolder holder, int position) {
        holder.username.setText(userDatabase.getName());

        //Checks to see if the User is already following the searched User. If so, the follow button
        //is changed to an unfollow button.
        DatabaseReference db = FirebaseDatabase.getInstance().getReference("friends/"+user.getUid());
        Query query = db.orderByKey().equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                        holder.followButton.setText("Unfollow");
                        holder.followButton.setTextColor(Color.WHITE);
                        followed=true;
                    }
                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add the searched user to the current user's followers in the database
                if(followed==false) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("friends");
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put(user.getUid() + "/" + key, true);
                    ref.updateChildren(childUpdates);
                    holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.colorAccent));
                    holder.followButton.setText("Unfollow");
                    holder.followButton.setTextColor(Color.WHITE);
                    followed=true;
                }
                //Delete the searched user from the current user's followers
                else{
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("friends/"+user.getUid()+"/"+key);
                    ref.removeValue();
                    holder.followButton.setBackgroundColor(context.getResources().getColor(R.color.button_follow));
                    holder.followButton.setText("Follow");
                    holder.followButton.setTextColor(Color.BLACK);
                    followed=false;
                }
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

    class friendsViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        Button followButton;

        public friendsViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.displayName);
            followButton = itemView.findViewById(R.id.follow);
        }
    }

}
