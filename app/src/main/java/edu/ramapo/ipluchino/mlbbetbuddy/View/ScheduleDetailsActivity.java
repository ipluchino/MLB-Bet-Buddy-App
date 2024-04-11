package edu.ramapo.ipluchino.mlbbetbuddy.View;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

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
    private TextView m_localGameTimeTextView;
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
        m_localGameTimeTextView = findViewById(R.id.localTimeSchedule);
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

        //A vector of vectors containing all of the required information to fill in the schedule details table.
        //FORMAT: TextView object, key in hashmap of schedule information, and whether or not the text should be partially bolded (for single columns lines).
        //There is an additional entry for fields that are doubles, used to determine the number of decimal places to round to.
        //Assistance: https://stackoverflow.com/questions/66844568/how-to-initialize-a-vector-with-values-in-java
        Vector<Vector<Object>> fieldInformation = new Vector<Vector<Object>>() {{
            add(new Vector<Object>() { { add(m_dateTextView); add("Date"); add(false);} });
            add(new Vector<Object>() { { add(m_homeTeamNameTextView); add("Home Team Name"); add(false);} });
            add(new Vector<Object>() { { add(m_awayTeamNameTextView); add("Away Team Name"); add(false);} });
            add(new Vector<Object>() { { add(m_homeTeamRecordTextView); add("Home Team Record"); add(false);} });
            add(new Vector<Object>() { { add(m_awayTeamRecordTextView); add("Away Team Record"); add(false);} });
            add(new Vector<Object>() { { add(m_homeStartingPitcherTextView); add("Home Team Probable Pitcher Name"); add(false);} });
            add(new Vector<Object>() { { add(m_awayStartingPitcherTextView); add("Away Team Probable Pitcher Name"); add(false);} });
            add(new Vector<Object>() { { add(m_stadiumTextView); add("Stadium"); add(true);} });
            add(new Vector<Object>() { { add(m_ballParkFactorTextView); add("Ballpark Factor"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_weatherDescriptionTextView); add("Weather Description"); add(true);} });
            add(new Vector<Object>() { { add(m_temperatureTextView); add("Temperature"); add(true);} });
            add(new Vector<Object>() { { add(m_windSpeedTextView); add("Wind Speed"); add(true);} });
        }};

        //Fill in the schedule table.
        FillGameDetails(fieldInformation);
    }

    //Fill in the specific game details into the
    @SuppressLint("SetTextI18n")
    private void FillGameDetails(Vector<Vector<Object>> a_fieldInformation) {
        //Set the fields that require additional processing.
        String homeTeamName = (String) m_gameDetails.get("Home Team Name");
        String homeTeamAbbreviation = BetPredictorModel.TEAM_ABBREVIATION.get(homeTeamName);
        String awayTeamName = (String) m_gameDetails.get("Away Team Name");
        String awayTeamAbbreviation = BetPredictorModel.TEAM_ABBREVIATION.get(awayTeamName);
        m_titleTextView.setText(homeTeamAbbreviation + " @ " + awayTeamAbbreviation);

        //Set the local time.
        String localGameTime = m_BPModelObj.ConvertToTimezone((String) m_gameDetails.get("DateTime String"), TimeZone.getDefault().getID());
        m_localGameTimeTextView.setText(WidgetUtilities.MakePartialTextBold("Game Time: ", localGameTime));

        //Fill the remaining fields in.
        WidgetUtilities.FillInTableTextViews(a_fieldInformation, m_gameDetails);
    }
}