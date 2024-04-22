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

public class NRFIYRFIDetailsActivity extends AppCompatActivity {
    //Private variables
    private BetPredictorModel m_BPModelObj;
    private HashMap<String, Object> m_betDetails;
    private Button m_backButton;
    private TextView m_titleTextView;
    private TextView m_dateTextView;
    private TextView m_homePitcherNameTextView, m_awayPitcherNameTextView;
    private TextView m_homePitcherGamesStartedTextView, m_awayPitcherGamesStartedTextView;
    private TextView m_homePitcherRecordTextView, m_awayPitcherRecordTextView;
    private TextView m_homePitcherERATextView, m_awayPitcherERATextView;
    private TextView m_homePitcherWHIPTextView, m_awayPitcherWHIPTextView;
    private TextView m_homePitcherKsPer9TextView, m_awayPitcherKsPer9TextView;
    private TextView m_homePitcherHomersPer9TextView, m_awayPitcherHomersPer9TextView;
    private TextView m_homePitcherYRFIPercentageTextView, m_awayPitcherYRFIPercentageTextView;
    private TextView m_homeTeamNameTextView, m_awayTeamNameTextView;
    private TextView m_homeTeamBATextView, m_awayTeamBATextView;
    private TextView m_homeTeamKRateTextView, m_awayTeamKRateTextView;
    private TextView m_homeTeamHomerRateTextView, m_awayTeamHomerRateTextView;
    private TextView m_homeTeamYRFIPercentageTextView, m_awayTeamYRFIPercentageTextView;
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
        setContentView(R.layout.activity_nrfiyrfidetails);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_betDetails = (HashMap<String, Object>) getIntent().getSerializableExtra("betDetails");
        m_backButton = findViewById(R.id.backButtonNRFIYRFIDetails);
        m_titleTextView = findViewById(R.id.titleTextViewNRFIYRFIDetails);
        m_dateTextView = findViewById(R.id.dateTextViewNRFIYRFIDetails);
        m_homePitcherNameTextView = findViewById(R.id.homeTeamStartingPitcherNRFIYRFI);
        m_awayPitcherNameTextView = findViewById(R.id.awayTeamStartingPitcherNRFIYRFI);
        m_homePitcherGamesStartedTextView = findViewById(R.id.homeTeamPitcherGamesStarted);
        m_awayPitcherGamesStartedTextView = findViewById(R.id.awayTeamPitcherGamesStarted);
        m_homePitcherRecordTextView = findViewById(R.id.homeTeamPitcherRecord);
        m_awayPitcherRecordTextView = findViewById(R.id.awayTeamPitcherRecord);
        m_homePitcherERATextView = findViewById(R.id.homeTeamPitcherERA);
        m_awayPitcherERATextView = findViewById(R.id.awayTeamPitcherERA);
        m_homePitcherWHIPTextView = findViewById(R.id.homeTeamPitcherWHIP);
        m_awayPitcherWHIPTextView = findViewById(R.id.awayTeamPitcherWHIP);
        m_homePitcherKsPer9TextView = findViewById(R.id.homeTeamPitcherKsPer9);
        m_awayPitcherKsPer9TextView = findViewById(R.id.awayTeamPitcherKsPer9);
        m_homePitcherHomersPer9TextView = findViewById(R.id.homeTeamPitcherHomersPer9);
        m_awayPitcherHomersPer9TextView = findViewById(R.id.awayTeamPitcherHomersPer9);
        m_homePitcherYRFIPercentageTextView = findViewById(R.id.homeTeamPitcherYRFIPercentage);
        m_awayPitcherYRFIPercentageTextView = findViewById(R.id.awayTeamPitcherYRFIPercentage);
        m_homeTeamNameTextView = findViewById(R.id.homeTeamNameNRFIYRFI);
        m_awayTeamNameTextView = findViewById(R.id.awayTeamNameNRFIYRFI);
        m_homeTeamBATextView = findViewById(R.id.homeTeamBA);
        m_awayTeamBATextView = findViewById(R.id.awayTeamBA);
        m_homeTeamKRateTextView = findViewById(R.id.homeTeamKRate);
        m_awayTeamKRateTextView = findViewById(R.id.awayTeamKRate);
        m_homeTeamHomerRateTextView = findViewById(R.id.homeTeamHomerRate);
        m_awayTeamHomerRateTextView = findViewById(R.id.awayTeamHomerRate);
        m_homeTeamYRFIPercentageTextView = findViewById(R.id.homeTeamYRFIPercentage);
        m_awayTeamYRFIPercentageTextView = findViewById(R.id.awayTeamYRFIPercentage);
        m_localGameTimeTextView = findViewById(R.id.localTimeNRFIYRFI);
        m_stadiumTextView = findViewById(R.id.stadiumNRFIYRFI);
        m_ballParkFactorTextView = findViewById(R.id.ballparkFactorNRFIYRFI);
        m_weatherDescriptionTextView = findViewById(R.id.weatherDescriptionNRFIYRFI);
        m_temperatureTextView = findViewById(R.id.temperatureNRFIYRFI);
        m_windSpeedTextView = findViewById(R.id.windSpeedNRFIYRFI);
        m_overallBetScoreTextView = findViewById(R.id.overallBetScore);

        //Set all of the onClick listeners for the buttons.
        m_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(getApplicationContext(), NRFIYRFIActivity.class);

                //Include the previous bet choice so that the initial ordering of the bets are correct again.
                String previousBetChoice = getIntent().getStringExtra("betChoice");
                intent.putExtra("betChoice", previousBetChoice);
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
            add(new Vector<Object>() { { add(m_homePitcherNameTextView); add("Home Pitcher Name"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_awayPitcherNameTextView); add("Away Pitcher Name"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_homePitcherGamesStartedTextView); add("Home Pitcher Games Started"); add(false); add(0);} });
            add(new Vector<Object>() { { add(m_awayPitcherGamesStartedTextView); add("Away Pitcher Games Started"); add(false); add(0);} });
            add(new Vector<Object>() { { add(m_homePitcherRecordTextView); add("Home Pitcher Record"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_awayPitcherRecordTextView); add("Away Pitcher Record"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_homePitcherERATextView); add("Home Pitcher ERA"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_awayPitcherERATextView); add("Away Pitcher ERA"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_homePitcherWHIPTextView); add("Home Pitcher WHIP"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_awayPitcherWHIPTextView); add("Away Pitcher WHIP"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_homePitcherKsPer9TextView); add("Home Pitcher Strikeouts Per 9"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_awayPitcherKsPer9TextView); add("Away Pitcher Strikeouts Per 9"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_homePitcherHomersPer9TextView); add("Home Pitcher Homeruns Per 9"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_awayPitcherHomersPer9TextView); add("Away Pitcher Homeruns Per 9"); add(false); add(2);} });
            add(new Vector<Object>() { { add(m_homeTeamNameTextView); add("Home Team Name"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_awayTeamNameTextView); add("Away Team Name"); add(false); add(null);} });
            add(new Vector<Object>() { { add(m_homeTeamBATextView); add("Home Team BA"); add(false); add(3);} });
            add(new Vector<Object>() { { add(m_awayTeamBATextView); add("Away Team BA"); add(false); add(3);} });
            add(new Vector<Object>() { { add(m_homeTeamKRateTextView); add("Home Team Strikeout Rate"); add(false); add(4);} });
            add(new Vector<Object>() { { add(m_awayTeamKRateTextView); add("Away Team Strikeout Rate"); add(false); add(4);} });
            add(new Vector<Object>() { { add(m_homeTeamHomerRateTextView); add("Home Team Homerun Rate"); add(false); add(4);} });
            add(new Vector<Object>() { { add(m_awayTeamHomerRateTextView); add("Away Team Homerun Rate"); add(false); add(4);} });
            add(new Vector<Object>() { { add(m_stadiumTextView); add("Stadium"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_ballParkFactorTextView); add("Ballpark Factor"); add(true); add(0);} });
            add(new Vector<Object>() { { add(m_weatherDescriptionTextView); add("Weather Description"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_temperatureTextView); add("Temperature"); add(true); add(null);} });
            add(new Vector<Object>() { { add(m_windSpeedTextView); add("Wind Speed"); add(true); add(null);} });
        }};

        //Fill in the NRFI/YRFI table.
        FillBetDetails(fieldInformation);
    }

    //Fill in the specific game details into the
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void FillBetDetails(Vector<Vector<Object>> a_fieldInformation) {
        //Set the fields that require additional processing.
        String homeTeamName = (String) m_betDetails.get("Home Team Name");
        String homeTeamAbbreviation = BetPredictorModel.TEAM_ABBREVIATION.get(homeTeamName);
        String awayTeamName = (String) m_betDetails.get("Away Team Name");
        String awayTeamAbbreviation = BetPredictorModel.TEAM_ABBREVIATION.get(awayTeamName);
        m_titleTextView.setText(awayTeamAbbreviation + " @ " + homeTeamAbbreviation);

        //Set the local time.
        String localGameTime = m_BPModelObj.ConvertToTimezone((String) m_betDetails.get("DateTime String"), TimeZone.getDefault().getID());
        m_localGameTimeTextView.setText(WidgetUtilities.MakePartialTextBold("Game Time: ", localGameTime));

        //Set the YRFI percentages to be actual percentages, rather than a number between 0 and 1.
        Double homePitcherYRFIPercentage = ((Double) m_betDetails.get("Home Pitcher YRFI Percentage") * 100.0);
        Double awayPitcherYRFIPercentage = ((Double) m_betDetails.get("Away Pitcher YRFI Percentage") * 100.0);
        Double homeTeamYRFIPercentage = ((Double) m_betDetails.get("Home Team YRFI Percentage") * 100.0);
        Double awayTeamYRFIPercentage = ((Double) m_betDetails.get("Away Team YRFI Percentage") * 100.0);

        //Round the percentages to 2 decimal places.
        m_homePitcherYRFIPercentageTextView.setText(String.format("%.2f", homePitcherYRFIPercentage) + "%");
        m_awayPitcherYRFIPercentageTextView.setText(String.format("%.2f", awayPitcherYRFIPercentage) + "%");
        m_homeTeamYRFIPercentageTextView.setText(String.format("%.2f", homeTeamYRFIPercentage) + "%");
        m_awayTeamYRFIPercentageTextView.setText(String.format("%.2f", awayTeamYRFIPercentage) + "%");

        //Set the overall bet score.
        String roundedScore = String.format("%.3f", (Double) m_betDetails.get("Overall NRFI Score") * 100.0);
        m_overallBetScoreTextView.setText(WidgetUtilities.MakePartialTextBold("Overall Bet Score: ", roundedScore));

        //Fill the remaining fields in.
        WidgetUtilities.FillInTableTextViews(a_fieldInformation, m_betDetails);
    }
}