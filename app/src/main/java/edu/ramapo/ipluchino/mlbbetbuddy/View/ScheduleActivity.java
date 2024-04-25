package edu.ramapo.ipluchino.mlbbetbuddy.View;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;
import android.content.Intent;
import android.os.Bundle;
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
        m_games = WidgetUtilities.GetData(SCHEDULE_TABLE_NAME);

        //If no games are returned from the server, display a message alerting the user.
        if (m_games.isEmpty()) {
            //Make some of the components invisible.
            m_homeButton.setVisibility(View.GONE);
            m_dateTextView.setVisibility(View.GONE);

            WidgetUtilities.DisplayLackOfData(this);
        }

        //OnClick listener for the home button.
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

    /**
     * Dynamically fills in the TableLayout with the schedule information.
     *
     * This method dynamically fills in the TableLayout with TableRows, with each TableRow representing a game on the schedule for the current
     * day. Each created TableRow object contains the contains the away team playing in the game and associated logo, the home team playing in
     * the game and associated logo, the local start time of the game, and a weather icon representing the weather at the start time of the game.
     * Additionally, each TableRow object has its OnClickListener set so that if you click anywhere in the row, it takes you to the
     * ScheduleDetailsActivity screen to show more information about the game.
     *
     * Assistance Received:
     * https://stackoverflow.com/questions/6583843/how-to-access-resource-with-dynamic-name-in-my-case
     * https://stackoverflow.com/questions/8669747/dynamically-add-imageview-to-tablerow
     */
    private void FillScheduleTable() {
        if(m_games.isEmpty()) {
            return;
        }

        //Set the date for the schedule table.
        m_dateTextView.setText((String) m_games.get(0).get("Date"));

        //Add an empty row to the beginning of the table, so that the initial divider gets shown.
        TableRow emptyTopRow = new TableRow(this);
        m_tableLayout.addView(emptyTopRow);

        //Loop through each of the games being played.
        for (HashMap<String, Object> game : m_games) {
            //Create a new table row.
            TableRow tableRow = new TableRow(this);

            //Create OnClickListener for the entire TableRow. This will view the scheduled game in more detail when clicked.
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Switch to the schedule details state.
                    Intent intent = new Intent(getApplicationContext(), ScheduleDetailsActivity.class);

                    //Pass the game details to the Schedule Details Activity.
                    intent.putExtra("gameDetails", game);
                    startActivity(intent);
                }
            });

            //Create the away team logo and abbreviation.
            String awayTeamName = (String) game.get("Away Team Name");
            ImageView awayTeamLogo = WidgetUtilities.CreateTeamLogo(this, awayTeamName, 150, 150, 30, 20, 20, 20);
            TextView awayTeamAbbreviation = WidgetUtilities.CreateTextView(this, BetPredictorModel.TEAM_ABBREVIATION.get(awayTeamName), 15, 0, 50, 20, 20);
            tableRow.addView(awayTeamLogo);
            tableRow.addView(awayTeamAbbreviation);

            //Create the @ sign between the team logos.
            TextView atSign = WidgetUtilities.CreateTextView(this,"@", 15, 0, 50, 20, 20);
            tableRow.addView(atSign);

            //Create the home team logo and abbreviation.
            String homeTeamName = (String) game.get("Home Team Name");
            ImageView homeTeamLogo = WidgetUtilities.CreateTeamLogo(this, homeTeamName, 150, 150, 10, 20, 20, 20);
            TextView homeTeamAbbreviation = WidgetUtilities.CreateTextView(this, BetPredictorModel.TEAM_ABBREVIATION.get(homeTeamName), 15, 0, 50, 70, 20);
            tableRow.addView(homeTeamLogo);
            tableRow.addView(homeTeamAbbreviation);

            //Set the local time of the game based on the timezone.
            TimeZone localTimezone = TimeZone.getDefault();
            String UTCGameTime = (String) game.get("DateTime String");
            String localGameTime = m_BPModelObj.ConvertToTimezone(UTCGameTime, localTimezone.getID());
            TextView gameTime = WidgetUtilities.CreateTextView(this, localGameTime, 15, 0, 50, 100, 20);
            tableRow.addView(gameTime);

            //Create the weather icon for the game.
            String weatherDescription = (String) game.get("Weather Description");
            ImageView weatherIcon = WidgetUtilities.CreateWeatherIcon(this, weatherDescription, 150, 150, 10, 20, 20, 20);
            tableRow.addView(weatherIcon);

            //Add the row into the table.
            m_tableLayout.addView(tableRow);
        }

        //Add an empty row to the end so that the divider gets shown on the bottom of the last row.
        TableRow emptyBottomRow = new TableRow(this);
        m_tableLayout.addView(emptyBottomRow);
    }
}
