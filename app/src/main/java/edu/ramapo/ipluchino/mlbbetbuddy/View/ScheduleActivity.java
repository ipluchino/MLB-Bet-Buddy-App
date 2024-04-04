package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.Manifest;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

public class ScheduleActivity extends AppCompatActivity {
    //Constants.
    private final String SCHEDULE_TABLE_NAME = "TodaySchedule";

    //Private variables
    private BetPredictorModel m_BPModelObj;
    private Vector<HashMap<String, Object>> m_games;
    private TableLayout m_tableLayout;
    private TextView m_dateTextView;
    private Button m_homeButton;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_tableLayout = findViewById(R.id.scheduleTable);
        m_dateTextView = findViewById(R.id.dateTextViewSchedule);
        m_homeButton = findViewById(R.id.homeButtonSchedule);

        //Attempt to query the server for the schedule information. This is done on a separate thread, not the main thread.
        GetGames();

        //If no games are returned from the server, display a message alerting the user.
        if (m_games.isEmpty()) {
            DisplayLackOfData();
        }

        //Set all of the onClick listeners for the buttons.
        m_homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });

        //Fill in the schedule table.
        FillScheduleTable();
    }

    //Gets today's schedule from the server.
    //Assistance: https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
    private void GetGames() {
        //Set up a new thread to query the server.
        Thread thread = new Thread(() -> {
            try {
                m_games = m_BPModelObj.GetDataFromServer(SCHEDULE_TABLE_NAME);
                //If the server does not respond or is offline, use an empty vector to represent the games.
            } catch (IOException e) {
                m_games = new Vector<HashMap<String, Object>>();
            }
        });

        //Start the thread and wait for it to finish fetching the data from the server.
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            m_games = new Vector<HashMap<String, Object>>();
        }
    }

    //Displays an alert dialog if there is no data returned from the server.
    private void DisplayLackOfData() {
        //Make some of the components invisible.
        m_homeButton.setVisibility(View.GONE);
        m_dateTextView.setVisibility(View.GONE);

        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
        builder.setTitle("No games!");

        builder.setMessage("There were no games found today. Please try again later.");

        //OK button to clear the alert dialog and go back to the home screen of the app.
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    //Fills in the schedule table dynamically.
    //https://stackoverflow.com/questions/6583843/how-to-access-resource-with-dynamic-name-in-my-case
    //https://stackoverflow.com/questions/8669747/dynamically-add-imageview-to-tablerow
    private void FillScheduleTable() {
        if(m_games.isEmpty()) {
            return;
        }

        //Set the date for the schedule table.
        m_dateTextView.setText((String) m_games.get(0).get("Date"));

        //Loop through each of the games being played.
        for (HashMap<String, Object> game : m_games) {
            //Create a new table row.
            TableRow tableRow = new TableRow(this);

            //Create OnClickListener for the entire TableRow. This will view the scheduled game in more detail when clicked.
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Test", String.valueOf(game));
                }
            });

            //Create the away team logo and abbreviation.
            String awayTeamName = (String) game.get("Away Team Name");
            ImageView awayTeamLogo = WidgetUtilities.CreateTeamLogo(this, awayTeamName, 150, 150, 10, 20, 20, 20);
            TextView awayTeamAbbreviation = WidgetUtilities.CreateTextView(this, BetPredictorModel.TEAM_ABBREVIATION.get(awayTeamName), 15, 0, 50, 20, 20);
            tableRow.addView(awayTeamLogo);
            tableRow.addView(awayTeamAbbreviation);

            //Create the @ sign between the team logos.
            TextView atSign = WidgetUtilities.CreateTextView(this,"@", 15, 0, 50, 20, 20);
            tableRow.addView(atSign);

            //Create the home team logo and abbreviation.
            String homeTeamName = (String) game.get("Home Team Name");
            ImageView homeTeamLogo = WidgetUtilities.CreateTeamLogo(this, homeTeamName, 150, 150, 10, 20, 20, 20);
            TextView homeTeamAbbreviation = WidgetUtilities.CreateTextView(this, BetPredictorModel.TEAM_ABBREVIATION.get(homeTeamName), 15, 0, 50, 50, 20);
            tableRow.addView(homeTeamLogo);
            tableRow.addView(homeTeamAbbreviation);

            //Set the local time of the game based on the timezone.
            TimeZone localTimezone = TimeZone.getDefault();
            String UTCGameTime = (String) game.get("DateTime String");
            String localGameTime = m_BPModelObj.ConvertToTimezone(UTCGameTime, localTimezone.getID());
            TextView gameTime = WidgetUtilities.CreateTextView(this, localGameTime, 15, 0, 50, 80, 20);
            tableRow.addView(gameTime);

            //Create the weather icon for the game.
            String weatherDescription = (String) game.get("Weather Description");
            ImageView weatherIcon = WidgetUtilities.CreateWeatherIcon(this, weatherDescription, 150, 150, 10, 20, 20, 20);
            tableRow.addView(weatherIcon);

            //Add the row into the table.
            m_tableLayout.addView(tableRow);
        }
    }
}
