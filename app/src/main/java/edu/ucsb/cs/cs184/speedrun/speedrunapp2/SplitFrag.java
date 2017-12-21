package edu.ucsb.cs.cs184.speedrun.speedrunapp2;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Category;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.CategoryList;
import edu.ucsb.cs.cs184.speedrun.speedrunapp2.game.Game;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplitFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    static int runNum=10;
    Button submit;
    Spinner category1;
    TextView time1;
    TextView gameName1;
    Button start;
    Button stop;
    static Game game1;
    Button lap;
    Button reset;
    Boolean started;
    TextView timer;
    Context context;
    LinearLayout container;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    List<Pair<String,String>> value;
    String category;
    String game;
    ConstraintLayout splitLayout;
    RecyclerView recyclerView;
    SplitViewAdapter adapter;
    long currentSplitTime;
    int currentSplit;
    boolean[] completed;
    static final int GOLD = 0;
    static final int GREEN = 1;
    static final int RED = 2;

    Handler customHandler = new Handler();
    long startTime=0L,timeInMillis=0L,timeSwapBuff=0L, updateTime=0L;
    Runnable timerThread = new Runnable() {
        @Override
        public void run() {
            timeInMillis = SystemClock.uptimeMillis()-startTime;
            updateTime = timeSwapBuff+timeInMillis;
            int secs = (int) updateTime/1000;
            int mins = secs/60;
            int hours = mins/60;
            mins%=60;
            secs%=60;
            int millis = (int) updateTime%1000;
            if(hours > 0)
                timer.setText(""+hours+":"+String.format("%02d",mins)+":"+String.format("%02d",secs)+"."+String.format("%03d",millis));
            else if(hours == 0)
                timer.setText(""+mins+":"+String.format("%02d",secs)+"."+String.format("%03d",millis));
            double timeDiff = 2*updateTime/currentSplitTime;
            Log.d(TAG, "updateTime: "+updateTime);
            Log.d(TAG, "currentSplitTime: "+currentSplitTime);
            Log.d(TAG, "timeDiff: "+timeDiff);
            int splitSecs = (int) Math.abs((currentSplitTime-updateTime)/1000);
            long splitMins = Math.abs(splitSecs/60);
            splitSecs%=60;
            splitMins%=60;
            long splitMillis = (long) Math.abs((currentSplitTime-updateTime)%1000);
            if(timeDiff < 0.75){
                adapter.updateLive("", currentSplit, GOLD);

            }
            if(timeDiff >= 0.85 && timeDiff < 0.95){
                String splitTime = "+"+splitMins+":"+String.format("%02d",splitSecs)+"."+String.format("%03d",splitMillis);
                adapter.updateLive(splitTime, currentSplit, GOLD);
            }
            else if(timeDiff >= 0.95 && timeDiff <= 1.0){
                String splitTime = "+"+splitMins+":"+String.format("%02d",splitSecs)+"."+String.format("%03d",splitMillis);
                adapter.updateLive(splitTime, currentSplit, GREEN);
            }
            else if(timeDiff > 1.0){
                String splitTime = "-"+splitMins+":"+String.format("%02d",splitSecs)+"."+String.format("%03d",splitMillis);
                adapter.updateLive(splitTime, currentSplit, RED);
            }
            customHandler.postDelayed(this,0);
        }
    };
    private String mParam1;
    private String mParam2;
    private TimerFrag.OnFragmentInteractionListener mListener;
    public SplitFrag() {
        // Required empty public constructor
    }

    public static SplitFrag newInstance(Game game2) {
        SplitFrag fragment = new SplitFrag();
        Bundle args = new Bundle();
        game1 = game2;
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //game = game;
        //category = category;
        //splitID = sID;
        //splits = splits; (List<Pair<String,String>>)
        List<Pair<String,String>> splits = new ArrayList<>();
        splits.add(Pair.create("1-1","0:10.12"));
        splits.add(Pair.create("1-2","0:20.344"));
        splits.add(Pair.create("1-3","1:03:00.001"));

        completed = new boolean[splits.size()];

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_split, container, false);
        started = false;
        category1 = (Spinner) view.findViewById(R.id.categorySpin);
        start = (Button) view.findViewById(R.id.start);
        stop = (Button) view.findViewById(R.id.stop);
        lap = (Button) view.findViewById(R.id.lap);
        reset = (Button) view.findViewById(R.id.reset);
        submit = (Button) view.findViewById(R.id.submit);
        time1 = (TextView) view.findViewById(R.id.time2);
        timer = (TextView) view.findViewById(R.id.display);
        recyclerView = view.findViewById(R.id.recyclerViewSplit);
        gameName1 = (TextView)view.findViewById(R.id.gameName2);
        gameName1.setText(game1.getNames().get("international"));
        adapter = new SplitViewAdapter(getContext(), splits);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        CategoryList categoryList=null;
        try {
            categoryList = game1.getCategories();
        } catch (Exception e) {
            System.out.println("catch");
            e.printStackTrace();
        }

        //Set the spinner's items to the catgories
        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item);
        Category categories[]=categoryList.getCategories();
        for (int i = 0; i< Array.getLength(categories); i++){
            adapter1.add(categories[i].getName());
        }
        category1.setAdapter(adapter1);

        //splitLayout = view.findViewById(R.id.splitLayout);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("runs/");
                DatabaseReference pushedRef = ref.push();
                String postId = pushedRef.getKey();
                Map<String,Object> childUpdates = new HashMap<>();
                childUpdates.put("game", game1.getNames().get("international"));
                childUpdates.put("category", category1.getSelectedItem().toString());
                childUpdates.put("Icon", game1.getAssets().getCoverMedium().getUri());
                childUpdates.put("name", user.getDisplayName());
                childUpdates.put("time", time1.getText().toString());
                childUpdates.put("time", time1.getText().toString());
                childUpdates.put("userid", user.getUid());
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String date = df.format(Calendar.getInstance().getTime());
                childUpdates.put("date", date);
                pushedRef.updateChildren(childUpdates);
                DatabaseReference splitsListRef = pushedRef.child("splitsList");
                Timer2Fragment fragment = new Timer2Fragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content_main, fragment).commit();
            }
        });
                start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!started) {
                            started = true;
                            currentSplit = 0;
                            startTime = SystemClock.uptimeMillis();
                            currentSplitTime = adapter.onTimerStart(startTime);
                            customHandler.postDelayed(timerThread, 0);

                        } else {
                            completed[currentSplit] = true;
                            currentSplit++;
                            long newtime = adapter.onTimerSplit(updateTime, currentSplit);
                            if (newtime != -1) {
                                currentSplitTime = newtime;
                            }
                            boolean check = true;
                            for (boolean item : completed) {
                                if (item == false)
                                    check = false;
                            }
                            if (check == true) {
                                customHandler.removeCallbacks(timerThread);
                                packageSplit();
                            }
                        }
                    }
                });

//        implementation for a stop ()
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (started) {
//                    customHandler.removeCallbacks(timerThread);
//                    started = false;
//
//                }
//            }
//        });
//        implementation for a restart
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (started) {
//                    startTime = SystemClock.uptimeMillis();
//
//                    customHandler.postDelayed(timerThread, 0);
//
//                }
//            }
//        });
//
//        implementation for pause
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (started) {
                    timeSwapBuff += timeInMillis;
                    time1.setText(timer.getText());

                    customHandler.removeCallbacks(timerThread);
                    started = false;
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started){
                    timeSwapBuff = 0;
                    startTime = SystemClock.uptimeMillis();

                    timer.setText("0:00:000");
                }
            }
        });
        lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mView = inflater.inflate(R.layout.lap, null);
                TextView value = (TextView) mView.findViewById(R.id.textContent);
                value.setText(timer.getText().toString());
                container.addView(mView);
            }
        });


        return view;
    }

    private void packageSplit() {
        List<Pair<String,String>> splits = adapter.getSplits();

    }

    private void submitRun(){

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TimerFrag.OnFragmentInteractionListener) {
            mListener = (TimerFrag.OnFragmentInteractionListener) context;
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_split, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        FragmentManager manager = ((Activity) context).getFragmentManager();

        //noinspection SimplifiableIfStatement
        if (id == R.id.split_edit) {
            Fragment choiceFrag = new SplitEditFrag();
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.content_main, choiceFrag).addToBackStack(null).commit();

            return true;
        }
        if (id == R.id.split_import) {
            //TODO: add code to import splits config from game (going to need fragment)

            return true;
        }
        if (id == R.id.split_delete) {
            //TODO: add code to delete current splits config

            return true;
        }
        if (id == R.id.split_help){

            SplitHelpFrag helpFrag = new SplitHelpFrag();
            helpFrag.show(manager, "HelpFrag");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
