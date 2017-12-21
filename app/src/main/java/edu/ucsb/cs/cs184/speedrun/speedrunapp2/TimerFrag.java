package edu.ucsb.cs.cs184.speedrun.speedrunapp2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TimerFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TimerFrag extends Fragment implements AddGameCategoryFrag.DialogCategoryListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button start;
    Button stop;
    Button lap;
    TextView timer;
    Context context;
    Handler customHandler = new Handler();
    RecyclerView recyclerView;

    private TimerAdapter adapter;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private String gameName;
    private String category;
    private Game game;

    public TimerFrag() {
        // Required empty public constructor
    }

    public static TimerFrag newInstance(String param1, String param2) {
        TimerFrag fragment = new TimerFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
//    private void setGamesLeaderboard(ArrayList<String> friends) {
//        DatabaseReference db = FirebaseDatabase.getInstance().getReference("runs");
//        Query query = db.orderByChild("game").equalTo(game.getNames().get("international"));
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (MainActivity.local == false){
//                    GameRetriever.getRun(categories, i, null, recyclerView, new GameRetriever.RunResultListener() {
//                        @Override
//                        public void onRun(LeaderboardPlayers leaderboard) {
//                            gameAdapter = new GameAdapter(getContext(), leaderboard);
//                            recyclerView.setAdapter(gameAdapter);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                            gameAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//                else {
//                    runs.clear();
//                    setAdapter();
//                }
//                runs.clear();
//                for (DataSnapshot snap: dataSnapshot.getChildren()){
//                    System.out.println(categorySpinner.getSelectedItem().toString());
//                    if ((friendsList.contains(snap.getValue(RunsDatabase.class).getUserid()))
//                            && (snap.getValue(RunsDatabase.class).getCategory().equals(categorySpinner.getSelectedItem().toString()))) {
//                        System.out.println(snap.getValue(RunsDatabase.class).getCategory());
//                        runs.add(snap.getValue(RunsDatabase.class));
//                    }
//                }
//                Collections.sort(runs);
//                localgameAdapter = new LocalGameAdapter(getContext(), runs);
//                recyclerView.setAdapter(localgameAdapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                localgameAdapter.notifyDataSetChanged();
//                progressBar.setVisibility(View.INVISIBLE);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }
        public static List<GameInfo> dummyData(){
        List<GameInfo> data = new ArrayList<>();
        int gameCover = (R.drawable.mario);
        String gameTitle = "Super Mario Sunshine";
        String worldRecord = "WR: 1:26:13 by WiseMuffin";
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("runs");
            Query query2 = db.orderByChild("userid").equalTo(user.getUid());
            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        for (DataSnapshot snap: dataSnapshot.getChildren()){
                            Timer temp = (snap.getValue(Timer.class));
                            data.add(new GameInfo(temp.getUri(),temp.getName(), temp.getCategory(), GameInfo.GAME_TYPE));

                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

        });
            data.add(new GameInfo(null, null, null, GameInfo.ADD_TYPE));

        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =  inflater.inflate(R.layout.fragment_timer, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new TimerAdapter(getContext(), dummyData());

        recyclerView.setAdapter(adapter);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    this.context = context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void gameCategoryListener(String game, String category) {
        this.gameName = game;
        this.category = category;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
