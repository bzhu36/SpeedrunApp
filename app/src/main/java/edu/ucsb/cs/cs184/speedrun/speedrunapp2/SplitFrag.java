package edu.ucsb.cs.cs184.speedrun.speedrunapp2;


import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplitFrag extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button start;
    Button stop;
    Button lap;
    Button reset;
    Boolean started;
    TextView timer;
    Context context;
    LinearLayout container;
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
                timer.setText(""+hours+":"+String.format("%02d",mins)+":"+String.format("%02d",secs)+":"+String.format("%03d",millis));
            else if(hours == 0)
                timer.setText(""+mins+":"+String.format("%02d",secs)+":"+String.format("%03d",millis));
            customHandler.postDelayed(this,0);
        }
    };
    private String mParam1;
    private String mParam2;
    private TimerFrag.OnFragmentInteractionListener mListener;
    public SplitFrag() {
        // Required empty public constructor
    }

    public static SplitFrag newInstance(String param1, String param2) {
        SplitFrag fragment = new SplitFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_split, container, false);
        started = false;
        start = (Button) view.findViewById(R.id.start);
        stop = (Button) view.findViewById(R.id.stop);
        lap = (Button) view.findViewById(R.id.lap);
        reset = (Button) view.findViewById(R.id.reset);
        timer = (TextView) view.findViewById(R.id.display);
        this.container = (LinearLayout) view.findViewById(R.id.container);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!started) {
                    started = true;
                    startTime = SystemClock.uptimeMillis();

                    customHandler.postDelayed(timerThread, 0);

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
//            Fragment choiceFrag = new SplitEditFrag();
            FragmentTransaction ft = manager.beginTransaction();
//            ft.replace(R.id.content_main, choiceFrag).addToBackStack(null).commit();

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

//            SplitHelpFrag helpFrag = new SplitHelpFrag();
//            helpFrag.show(manager, "HelpFrag");

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
