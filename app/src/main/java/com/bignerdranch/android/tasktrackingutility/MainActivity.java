package com.bignerdranch.android.tasktrackingutility;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private TextView mTvTime;
    private Spinner mSpList;
    private Button mIn;
    private Button mOut;
    private ScrollView mSvLog;
    private EditText mEtLog;
    private TextView mTvTotal;

    private long totalTime;
    private long currentTime;
    private String currentDate;
    private String checkOutTime;
    private String task;

    private Context mContext;
    private Chronometer mChronometer;
    private Thread mThreadChrono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;

        mTvTime = (TextView) findViewById(R.id.tv_timer);
        mSpList = (Spinner) findViewById(R.id.sp_list);
        mIn = (Button) findViewById(R.id.btn_check_in);
        mOut = (Button) findViewById(R.id.btn_check_out);
        mSvLog = (ScrollView) findViewById(R.id.sv_log);
        mEtLog = (EditText) findViewById(R.id.et_log);
        mTvTotal = (TextView) findViewById(R.id.tv_total);


        mEtLog.setEnabled(false);

        //String[] items = new String[]{"Capstone", "System II", "Physics", "Mobile Programming"};
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, items);
        //mSpList.setAdapter(adapter);

        mIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckOutButton();
                if (mChronometer == null) {
                    mChronometer = new Chronometer(mContext);
                    mThreadChrono = new Thread(mChronometer);
                    mThreadChrono.start();
                    mChronometer.start();

                    task = mSpList.getSelectedItem().toString();
                    currentDate = DateFormat.getDateTimeInstance().format(new Date());
                }
            }
        });

        mOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCheckInButton();
                if (mChronometer != null) {
                    mChronometer.stop();
                    mThreadChrono.interrupt();
                    mThreadChrono = null;

                    mChronometer = null;

                    checkOutTime = DateFormat.getTimeInstance().format(new Date());
                    totalTime += currentTime;
                    mTvTotal.setText(formatTime(totalTime));

                    mEtLog.append(task + ": " + currentDate + " - " + checkOutTime + "\n");

                }
            }
        });


    }

    private void showCheckOutButton() {
        ((Button) findViewById(R.id.btn_check_in)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.btn_check_out)).setVisibility(View.VISIBLE);
    }

    private void showCheckInButton() {
        ((Button) findViewById(R.id.btn_check_out)).setVisibility(View.GONE);
        ((Button) findViewById(R.id.btn_check_in)).setVisibility(View.VISIBLE);
    }

    public void updateTimerText(final String time) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvTime.setText(time);
            }
        });
    }

    public void updateGlobalTime(long since) {
        currentTime = since;
    }

    public String formatTime(long tt) {
        int seconds = (int) ((tt / 1000) % 60);
        int minutes = (int) ((tt / 60000) % 60);
        int hours = (int) ((tt /3600000 ) % 24);

        return String.format("Total %02d:%02d:%02d", hours, minutes, seconds);
    }

}
