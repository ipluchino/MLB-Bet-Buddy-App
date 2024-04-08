package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.Manifest;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;

import android.annotation.SuppressLint;
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
import java.util.HashMap;
import java.util.TimeZone;

public class ScheduleDetailsActivity extends AppCompatActivity {
    //Private variables
    private BetPredictorModel m_BPModelObj;
    private HashMap<String, Object> m_gameDetails;
    private Button m_backButton;
    private TextView m_titleTextView;
    private TextView m_dateTextView;
    private TextView m_homeTeamNameTextView, m_awayTeamNameTextView;
    private TextView m_homeTeamRecordTextView, m_awayTeamRecordTextView;
    private TextView m_homeStartingPitcherTextView, m_awayStartingPitcherTextView;
    private TextView m_localTimeTextView;
    private TextView m_stadiumTextView;
    private TextView m_ballParkFactorTextView;
    private TextView m_weatherDescriptionTextView;
    private TextView m_temperatureTextView;
    private TextView m_windSpeedTextView;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduledetails);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_gameDetails = (HashMap<String, Object>) getIntent().getSerializableExtra("gameDetails");
        m_backButton = findViewById(R.id.backButtonScheduleDetails);
        m_titleTextView = findViewById(R.id.titleTextViewScheduleDetails);
        m_dateTextView = findViewById(R.id.dateTextViewScheduleDetails);
        m_homeTeamNameTextView = findViewById(R.id.homeTeamNameSchedule);
        m_awayTeamNameTextView = findViewById(R.id.awayTeamNameSchedule);
        m_homeTeamRecordTextView = findViewById(R.id.homeTeamRecordSchedule);
        m_awayTeamRecordTextView = findViewById(R.id.awayTeamRecordSchedule);
        m_homeStartingPitcherTextView = findViewById(R.id.homeTeamStartingPitcherSchedule);
        m_awayStartingPitcherTextView = findViewById(R.id.awayTeamStartingPitcherSchedule);
        m_localTimeTextView = findViewById(R.id.localTimeSchedule);
        m_stadiumTextView = findViewById(R.id.stadiumSchedule);
        m_ballParkFactorTextView = findViewById(R.id.ballparkFactorSchedule);
        m_weatherDescriptionTextView = findViewById(R.id.weatherDescriptionSchedule);
        m_temperatureTextView = findViewById(R.id.temperatureSchedule);
        m_windSpeedTextView = findViewById(R.id.windSpeedSchedule);

        //Set all of the onClick listeners for the buttons.
        m_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //If no game information was sent to this screen, there must have been an error so alert the user.

        //Fill in the schedule table.
        FillGameDetails();
    }

    //Fill in the specific game details into the
    @SuppressLint("SetTextI18n")
    private void FillGameDetails() {
        //Set the title and date.
        String homeTeamName = (String) m_gameDetails.get("Home Team Name");
        String homeTeamAbbreviation = BetPredictorModel.TEAM_ABBREVIATION.get(homeTeamName);
        String awayTeamName = (String) m_gameDetails.get("Away Team Name");
        String awayTeamAbbreviation = BetPredictorModel.TEAM_ABBREVIATION.get(awayTeamName);

        m_titleTextView.setText(homeTeamAbbreviation + " @ " + awayTeamAbbreviation);
        m_dateTextView.setText((String) m_gameDetails.get("Date"));

        //Set the team information for both teams.
        m_homeTeamNameTextView.setText(homeTeamName);
        m_awayTeamNameTextView.setText(awayTeamName);

        m_homeTeamRecordTextView.setText((String) m_gameDetails.get("Home Team Record"));
        m_awayTeamRecordTextView.setText((String) m_gameDetails.get("Away Team Record"));

        m_homeStartingPitcherTextView.setText((String) m_gameDetails.get("Home Team Probable Pitcher Name"));
        m_awayStartingPitcherTextView.setText((String) m_gameDetails.get("Away Team Probable Pitcher Name"));

        //Set the local factors information
        String localGameTime = m_BPModelObj.ConvertToTimezone((String) m_gameDetails.get("DateTime String"), TimeZone.getDefault().getID());
        m_localTimeTextView.setText(WidgetUtilities.MakePartialTextBold("Local Time: ", localGameTime));
        m_stadiumTextView.setText(WidgetUtilities.MakePartialTextBold("Stadium: ", (String) m_gameDetails.get("Stadium")));
        m_ballParkFactorTextView.setText(WidgetUtilities.MakePartialTextBold("Ballpark Factor: ", String.valueOf(m_gameDetails.get("Ballpark Factor"))));
        m_weatherDescriptionTextView.setText(WidgetUtilities.MakePartialTextBold("Weather: ", (String) m_gameDetails.get("Weather Description")));
        m_temperatureTextView.setText(WidgetUtilities.MakePartialTextBold("Temperature: ", (String) m_gameDetails.get("Temperature")));
        m_windSpeedTextView.setText(WidgetUtilities.MakePartialTextBold("Wind Speed: ", (String) m_gameDetails.get("Wind Speed")));
    }
}