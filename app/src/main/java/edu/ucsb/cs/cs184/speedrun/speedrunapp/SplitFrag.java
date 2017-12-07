package edu.ucsb.cs.cs184.speedrun.speedrunapp;


import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplitFrag extends DialogFragment {

    Button start;
    Button stop;
    Button lap;
    TextView timer;
    Context context;
    LinearLayout container;
    Handler customHandler = new Handler();
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

    long startTime=0L,timeInMillis=0L,timeSwapBuff=0L, updateTime=0L;
    public SplitFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_split, container, false);
        start = (Button) view.findViewById(R.id.start);
        stop = (Button) view.findViewById(R.id.stop);
        lap = (Button) view.findViewById(R.id.lap);
        timer = (TextView) view.findViewById(R.id.display);
        this.container = (LinearLayout) view.findViewById(R.id.container);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTime = SystemClock.uptimeMillis();

                customHandler.postDelayed(timerThread,0);

            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSwapBuff += timeInMillis;

                customHandler.removeCallbacks(timerThread);
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
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }
}
