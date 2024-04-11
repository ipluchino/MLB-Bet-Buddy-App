package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.content.Intent;
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
import java.util.Vector;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;

public class HittingActivity extends AppCompatActivity {
    //Constants.
    private final String HITTING_TABLE_NAME = "TodayHitting";

    //Private variables
    private BetPredictorModel m_BPModelObj;
    private Vector<HashMap<String, Object>> m_hittingBetPredictions;
    private TableLayout m_tableLayout;
    private TextView m_dateTextView;
    private Button m_homeButton;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hitting);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_tableLayout = findViewById(R.id.hittingTable);
        m_dateTextView = findViewById(R.id.dateTextViewHitting);
        m_homeButton = findViewById(R.id.homeButtonHitting);

        //Attempt to query the server for the hitting bet information. This is done on a separate thread, not the main thread.
        m_hittingBetPredictions = WidgetUtilities.GetData(HITTING_TABLE_NAME);

        //If no games are returned from the server, display a message alerting the user.
        if (m_hittingBetPredictions.isEmpty()) {
            //Make some of the components invisible.
            m_homeButton.setVisibility(View.GONE);
            m_dateTextView.setVisibility(View.GONE);

            WidgetUtilities.DisplayLackOfData(this);
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

        //Fill in the hitting table.
        FillHittingTable();
    }

    //Fills in the hitting table dynamically.
    //https://stackoverflow.com/questions/6583843/how-to-access-resource-with-dynamic-name-in-my-case
    //https://stackoverflow.com/questions/8669747/dynamically-add-imageview-to-tablerow
    private void FillHittingTable() {
        if(m_hittingBetPredictions.isEmpty()) {
            return;
        }

        //Set the date for the hitting table.
        m_dateTextView.setText((String) m_hittingBetPredictions.get(0).get("Date"));

        //Loop through each of the games being played.
        for (HashMap<String, Object> betPrediction : m_hittingBetPredictions) {
            //Create a new table row.
            TableRow tableRow = new TableRow(this);

            //Create OnClickListener for the entire TableRow. This will view the hitting bet in more detail when clicked.
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Switch to the hitting details state.
                    Intent intent = new Intent(getApplicationContext(), HittingDetailsActivity.class);

                    //Pass the bet prediction details to the HittingDetails Activity.
                    intent.putExtra("betDetails", betPrediction);
                    startActivity(intent);
                }
            });

            //Create the hitter's team logo.
            String hitterTeamName = (String) betPrediction.get("Hitter Team Name");
            ImageView hitterTeamLogo = WidgetUtilities.CreateTeamLogo(this, hitterTeamName, 150, 150, 30, 20, 20, 20);
            tableRow.addView(hitterTeamLogo);

            //Create the hitter's name.
            String hitterName = (String) betPrediction.get("Hitter Name");
            TextView name = WidgetUtilities.CreateTextView(this,hitterName, 15, 0, 50, 20, 20);
            tableRow.addView(name);

            //Set the local time of the game based on the timezone.
            TimeZone localTimezone = TimeZone.getDefault();
            String UTCGameTime = (String) betPrediction.get("DateTime String");
            String localGameTime = m_BPModelObj.ConvertToTimezone(UTCGameTime, localTimezone.getID());
            TextView gameTime = WidgetUtilities.CreateTextView(this, localGameTime, 15, 0, 50, 55, 20);
            tableRow.addView(gameTime);

            //Create the hitting bet score.
            String roundedScore = String.format("%.3f", betPrediction.get("Overall Hitting Score"));
            String betScore = "Score: " + roundedScore;
            TextView betChoice = WidgetUtilities.CreateTextView(this, betScore, 15, 0, 50, 0, 20);
            tableRow.addView(betChoice);

            //Add the row into the table.
            m_tableLayout.addView(tableRow);
        }
    }
}
