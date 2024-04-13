package edu.ramapo.ipluchino.mlbbetbuddy.View;

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

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;

public class HittingDetailsActivity extends AppCompatActivity {
    //Private variables
    private BetPredictorModel m_BPModelObj;
    private HashMap<String, Object> m_betDetails;
    private Button m_backButton;
    private TextView m_titleTextView;
    private TextView m_dateTextView;
    private TextView m_hitterName;
    private TextView m_hitterTeam;
    private TextView m_batHand;
    private TextView m_seasonBA, m_seasonOBP, m_seasonHomers, m_seasonOPS;
    private TextView m_vsLeftBA, m_vsRightBA;
    private TextView m_opposingPitcherName, m_opposingPitcherTeam, m_pitchHand;
    private TextView m_vsLeftBAA, m_vsRightBAA;
    private TextView m_careerAgainstPA, m_careerAgainstBA, m_careerAgainstOPS, m_careerAgainstHittingClassification;
    private TextView m_last10GamesPA, m_last10GamesBA, m_last10GamesOPS, m_last10GamesHittingClassification;
    private TextView m_localGameTimeTextView;
    private TextView m_stadiumTextView;
    private TextView m_ballParkFactorTextView;
    private TextView m_weatherDescriptionTextView;
    private TextView m_temperatureTextView;
    private TextView m_windSpeedTextView;
    private TextView m_overallBetScore;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hittingdetails);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_betDetails = (HashMap<String, Object>) getIntent().getSerializableExtra("betDetails");
        m_backButton = findViewById(R.id.backButtonHittingDetails);
        m_titleTextView = findViewById(R.id.titleTextViewHittingDetails);
        m_dateTextView = findViewById(R.id.dateTextViewHittingDetails);
        m_hitterName = findViewById(R.id.hitterName);
        m_hitterTeam = findViewById(R.id.hitterTeam);
        m_batHand = findViewById(R.id.batHand);
        m_seasonBA = findViewById(R.id.seasonBA);
        m_seasonOBP = findViewById(R.id.seasonOBP);
        m_seasonHomers = findViewById(R.id.seasonHomers);
        m_seasonOPS = findViewById(R.id.seasonOPS);
        m_vsLeftBA = findViewById(R.id.seasonVsLeftBA);
        m_vsRightBA = findViewById(R.id.seasonVsRightBA);
        m_opposingPitcherName = findViewById(R.id.pitcherName);
        m_opposingPitcherTeam = findViewById(R.id.pitcherTeam);
        m_pitchHand = findViewById(R.id.pitchHand);
        m_vsLeftBAA = findViewById(R.id.seasonVsLeftBAA);
        m_vsRightBAA = findViewById(R.id.seasonVsRightBAA);
        m_careerAgainstPA = findViewById(R.id.careerAgainstPA);
        m_careerAgainstBA = findViewById(R.id.careerAgainstBA);
        m_careerAgainstOPS = findViewById(R.id.careerAgainstOPS);
        m_careerAgainstHittingClassification = findViewById(R.id.careerAgainstHittingClassification);
        m_last10GamesPA = findViewById(R.id.Last10PA);
        m_last10GamesBA = findViewById(R.id.Last10BA);
        m_last10GamesOPS = findViewById(R.id.Last10OPS);
        m_last10GamesHittingClassification = findViewById(R.id.Last10HittingClassification);
        m_localGameTimeTextView = findViewById(R.id.localTimeHitting);
        m_stadiumTextView = findViewById(R.id.stadiumHitting);
        m_ballParkFactorTextView = findViewById(R.id.ballparkFactorHitting);
        m_weatherDescriptionTextView = findViewById(R.id.weatherDescriptionHitting);
        m_temperatureTextView = findViewById(R.id.temperatureHitting);
        m_windSpeedTextView = findViewById(R.id.windSpeedHitting);
        m_overallBetScore = findViewById(R.id.overallBetScore);

        //Set all of the onClick listeners for the buttons.
        m_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(getApplicationContext(), HittingActivity.class);
                startActivity(intent);
            }
        });

        //If no game information was sent to this screen, there must have been an error so alert the user.

        //A vector of vectors containing all of the required information to fill in the bet details table.
        //FORMAT: TextView object, key in hashmap for bet information, and whether or not the text should be partially bolded (for single columns lines).
        //There is an additional entry for fields that are doubles, used to determine the number of decimal places to round to.
        //Assistance: https://stackoverflow.com/questions/66844568/how-to-initialize-a-vector-with-values-in-java
        Vector<Vector<Object>> fieldInformation = new Vector<Vector<Object>>() {{
            add(new Vector<Object>() { { add(m_dateTextView); add("Date"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_hitterName); add("Hitter Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_hitterTeam); add("Hitter Team Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_batHand); add("Bat Hand"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_seasonBA); add("BA"); add(true); add(3);}  });
            add(new Vector<Object>() { { add(m_seasonOBP); add("OBP"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_seasonHomers); add("Homers"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_seasonOPS); add("OPS"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_vsLeftBA); add("Vs. Left BA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_vsRightBA); add("Vs. Right BA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_opposingPitcherName); add("Pitcher Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_opposingPitcherTeam); add("Pitcher Team Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_pitchHand); add("Pitching Hand"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_vsLeftBAA); add("Vs. Left BAA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_vsRightBAA); add("Vs. Right BAA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_careerAgainstPA); add("Career Plate Appearances Vs. Pitcher"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_careerAgainstBA); add("Career BA Vs. Pitcher"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_careerAgainstOPS); add("Career OPS Vs. Pitcher"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_careerAgainstHittingClassification); add("Career Stats Description"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_last10GamesPA); add("Last 10 Games Plate Appearances"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_last10GamesBA); add("Last 10 Games BA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_last10GamesOPS); add("Last 10 Games OPS"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_last10GamesHittingClassification); add("Hot/Cold Description"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_stadiumTextView); add("Stadium"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_ballParkFactorTextView); add("Ballpark Factor"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_weatherDescriptionTextView); add("Weather Description"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_temperatureTextView); add("Temperature"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_windSpeedTextView); add("Wind Speed"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_overallBetScore); add("Overall Hitting Score"); add(true); add(3);} });
        }};

        //Fill in the hitting table.
        FillBetDetails(fieldInformation);
    }

    //Fill in the specific game details into the
    @SuppressLint("SetTextI18n")
    private void FillBetDetails(Vector<Vector<Object>> a_fieldInformation) {
        //Set the local time.
        String localGameTime = m_BPModelObj.ConvertToTimezone((String) m_betDetails.get("DateTime String"), TimeZone.getDefault().getID());
        m_localGameTimeTextView.setText(WidgetUtilities.MakePartialTextBold("Game Time: ", localGameTime));

        //Fill the remaining fields in.
        WidgetUtilities.FillInTableTextViews(a_fieldInformation, m_betDetails);
    }
}