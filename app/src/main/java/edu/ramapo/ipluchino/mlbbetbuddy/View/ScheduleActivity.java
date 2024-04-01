package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.Manifest;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class ScheduleActivity extends AppCompatActivity {
    //Constants.
    private final String SCHEDULE_TABLE_NAME = "TodaySchedule";

    //Private variables
    private BetPredictorModel m_bpModelObj;
    private Vector<HashMap<String, Object>> m_games;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Might change this - Allow the http request to be done on the main thread of the phone.
        //https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Initialize the private variables.
        m_bpModelObj = new BetPredictorModel();

        //Attempt to query the database for the schedule information.
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    m_games = m_bpModelObj.GetDataFromServer(SCHEDULE_TABLE_NAME);
                    //If the server does not respond or is offline, use an empty vector to represent the games.
                } catch (IOException e) {
                    Log.d("test", "I'm here");
                    m_games = new Vector<HashMap<String, Object>>();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        Log.d("test", String.valueOf(m_games));
    }

}
