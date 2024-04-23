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
    private TextView m_dateTextView;
    private TextView m_hitterNameTextView;
    private TextView m_hitterTeamTextView;
    private TextView m_batHandTextView;
    private TextView m_seasonBATextView, m_seasonOBPTextView, m_seasonHomersTextView, m_seasonOPSTextView;
    private TextView m_vsLeftBATextView, m_vsRightBATextView;
    private TextView m_opposingPitcherNameTextView, m_opposingPitcherTeamTextView, m_pitchHandTextView;
    private TextView m_vsLeftBAATextView, m_vsRightBAATextView;
    private TextView m_careerAgainstPATextView, m_careerAgainstBATextView, m_careerAgainstOPSTextView, m_careerAgainstHittingClassificationTextView;
    private TextView m_last10GamesPATextView, m_last10GamesBATextView, m_last10GamesOPSTextView, m_last10GamesHittingClassificationTextView;
    private TextView m_localGameTimeTextView;
    private TextView m_stadiumTextView;
    private TextView m_ballParkFactorTextView;
    private TextView m_weatherDescriptionTextView;
    private TextView m_temperatureTextView;
    private TextView m_windSpeedTextView;
    private TextView m_overallBetScoreTextView;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hittingdetails);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_betDetails = (HashMap<String, Object>) getIntent().getSerializableExtra("betDetails");
        m_backButton = findViewById(R.id.backButtonHittingDetails);
        m_dateTextView = findViewById(R.id.dateTextViewHittingDetails);
        m_hitterNameTextView = findViewById(R.id.hitterName);
        m_hitterTeamTextView = findViewById(R.id.hitterTeam);
        m_batHandTextView = findViewById(R.id.batHand);
        m_seasonBATextView = findViewById(R.id.seasonBA);
        m_seasonOBPTextView = findViewById(R.id.seasonOBP);
        m_seasonHomersTextView = findViewById(R.id.seasonHomers);
        m_seasonOPSTextView = findViewById(R.id.seasonOPS);
        m_vsLeftBATextView = findViewById(R.id.seasonVsLeftBA);
        m_vsRightBATextView = findViewById(R.id.seasonVsRightBA);
        m_opposingPitcherNameTextView = findViewById(R.id.pitcherName);
        m_opposingPitcherTeamTextView = findViewById(R.id.pitcherTeam);
        m_pitchHandTextView = findViewById(R.id.pitchHand);
        m_vsLeftBAATextView = findViewById(R.id.seasonVsLeftBAA);
        m_vsRightBAATextView = findViewById(R.id.seasonVsRightBAA);
        m_careerAgainstPATextView = findViewById(R.id.careerAgainstPA);
        m_careerAgainstBATextView = findViewById(R.id.careerAgainstBA);
        m_careerAgainstOPSTextView = findViewById(R.id.careerAgainstOPS);
        m_careerAgainstHittingClassificationTextView = findViewById(R.id.careerAgainstHittingClassification);
        m_last10GamesPATextView = findViewById(R.id.Last10PA);
        m_last10GamesBATextView = findViewById(R.id.Last10BA);
        m_last10GamesOPSTextView = findViewById(R.id.Last10OPS);
        m_last10GamesHittingClassificationTextView = findViewById(R.id.Last10HittingClassification);
        m_localGameTimeTextView = findViewById(R.id.localTimeHitting);
        m_stadiumTextView = findViewById(R.id.stadiumHitting);
        m_ballParkFactorTextView = findViewById(R.id.ballparkFactorHitting);
        m_weatherDescriptionTextView = findViewById(R.id.weatherDescriptionHitting);
        m_temperatureTextView = findViewById(R.id.temperatureHitting);
        m_windSpeedTextView = findViewById(R.id.windSpeedHitting);
        m_overallBetScoreTextView = findViewById(R.id.overallBetScore);

        //Set all of the onClick listeners for the buttons.
        m_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(getApplicationContext(), HittingActivity.class);
                startActivity(intent);
            }
        });

        //A vector of vectors containing all of the required information to fill in the bet details table.
        //FORMAT: TextView object, key in hashmap for bet information, and whether or not the text should be partially bolded (for single columns lines).
        //There is an additional entry for fields that are doubles, used to determine the number of decimal places to round to.
        //Assistance: https://stackoverflow.com/questions/66844568/how-to-initialize-a-vector-with-values-in-java
        Vector<Vector<Object>> fieldInformation = new Vector<Vector<Object>>() {{
            add(new Vector<Object>() { { add(m_dateTextView); add("Date"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_hitterNameTextView); add("Hitter Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_hitterTeamTextView); add("Hitter Team Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_batHandTextView); add("Bat Hand"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_seasonBATextView); add("BA"); add(true); add(3);}  });
            add(new Vector<Object>() { { add(m_seasonOBPTextView); add("OBP"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_seasonHomersTextView); add("Homers"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_seasonOPSTextView); add("OPS"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_vsLeftBATextView); add("Vs. Left BA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_vsRightBATextView); add("Vs. Right BA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_opposingPitcherNameTextView); add("Pitcher Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_opposingPitcherTeamTextView); add("Pitcher Team Name"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_pitchHandTextView); add("Pitching Hand"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_vsLeftBAATextView); add("Vs. Left BAA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_vsRightBAATextView); add("Vs. Right BAA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_careerAgainstPATextView); add("Career Plate Appearances Vs. Pitcher"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_careerAgainstBATextView); add("Career BA Vs. Pitcher"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_careerAgainstOPSTextView); add("Career OPS Vs. Pitcher"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_careerAgainstHittingClassificationTextView); add("Career Stats Description"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_last10GamesPATextView); add("Last 10 Games Plate Appearances"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_last10GamesBATextView); add("Last 10 Games BA"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_last10GamesOPSTextView); add("Last 10 Games OPS"); add(true); add(3);} });
            add(new Vector<Object>() { { add(m_last10GamesHittingClassificationTextView); add("Hot/Cold Description"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_stadiumTextView); add("Stadium"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_ballParkFactorTextView); add("Ballpark Factor"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_weatherDescriptionTextView); add("Weather Description"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_temperatureTextView); add("Temperature"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_windSpeedTextView); add("Wind Speed"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_overallBetScoreTextView); add("Overall Hitting Score"); add(true); add(3);} });
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