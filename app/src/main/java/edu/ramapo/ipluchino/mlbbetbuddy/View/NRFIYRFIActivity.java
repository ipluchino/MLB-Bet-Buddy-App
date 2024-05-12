/**
 *********************************************************************************************************************************
 * Author: Ian Pluchino                                                                                                          *
 * Class: NRFIYRFIActivity class                                                                                                 *
 * Description: A view class, used to configure and dynamically display the NRFIYRFI screen of the application.                  *
 * Date: 5/2/24                                                                                                                  *
 *********************************************************************************************************************************
 */

package edu.ramapo.ipluchino.mlbbetbuddy.View;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;
import java.util.Vector;

public class NRFIYRFIActivity extends AppCompatActivity {
    //CONSTANTS
    private final String NRFIYRFI_TABLE_NAME = "TodayNRFI";

    //PRIVATE VARIABLES
    private BetPredictorModel m_BPModelObj;
    private Vector<HashMap<String, Object>> m_NRFIYRFIBetPredictions;
    private TableLayout m_tableLayout;
    private Spinner m_betChoiceSpinner;
    private TextView m_dateTextView;
    private Button m_homeButton;
    private String m_betChoice;

    /**
     * Creates the NRFIYRFIActivity.
     * <p>
     * This method creates the NRFIYRFIActivity and sets the screen's view to the NRFIYRFI layout. It also initializes the private variables,
     * sets event handlers for buttons as well as the Spinner object that determines NRFI or YRFI as the bet type being displayed. The initial
     * ordering of the NRFI or YRFI bets is determined through the Intent object passed to this activity. Finally, the NRFIYRFI bet table is
     * dynamically filled in.
     * <p>
     * Assistance Received:
     * <p>
     * https://www.geeksforgeeks.org/reverse-order-of-all-elements-of-java-vector/
     * <p>
     * https://stackoverflow.com/questions/11072576/set-selected-item-of-spinner-programmatically
     * <p>
     * https://stackoverflow.com/questions/1337424/android-spinner-get-the-selected-item-change-event
     * <p>
     * https://stackoverflow.com/questions/36131281/how-to-get-string-from-setonitemselectedlistener-method
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most
     *        recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrfiyrfi);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_tableLayout = findViewById(R.id.NRFIYRFITable);
        m_betChoiceSpinner = findViewById(R.id.titleSpinnerNRFIYRFI);
        m_dateTextView = findViewById(R.id.dateTextViewNRFIYRFI);
        m_homeButton = findViewById(R.id.homeButtonNRFIYRFI);

        //Attempt to query the server for the NRFI/YRFI information. This is done on a separate thread, not the main thread.
        m_NRFIYRFIBetPredictions = WidgetUtilities.GetData(NRFIYRFI_TABLE_NAME);

        //Determine the initial ordering of the bets.
        if (Objects.equals(getIntent().getStringExtra("betChoice"), "YRFI")) {
            //If the initial ordering if "YRFI", the order of the games vector needs to be reversed since the games are returned by the server in NRFI order by default.
            Collections.reverse(m_NRFIYRFIBetPredictions);

            //Set the choice in the spinner as well to match.
            m_betChoiceSpinner.setSelection(1);
            m_betChoice = "YRFI";
        }
        else {
            m_betChoice = "NRFI";
        }

        //If no games are returned from the server, display a message alerting the user.
        if (m_NRFIYRFIBetPredictions.isEmpty()) {
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

        //Set an event listener for the bet choice spinner, to know when it changed.
        m_betChoiceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedItem = (String) parentView.getItemAtPosition(position);
                Log.d("test", selectedItem);

                if (selectedItem.equals("NRFI")) {
                    //If this selection was already selected, no need to do anything.
                    if (Objects.equals(m_betChoice, "NRFI")) {
                        return;
                    }
                    //Otherwise, update the bet choice, reverse the ordering, and rebuild the table.
                    else
                    {
                        m_betChoice = "NRFI";
                        Collections.reverse(m_NRFIYRFIBetPredictions);
                        FillNRFIYRFITable();
                    }
                }
                else
                {
                    if (selectedItem.equals("YRFI")) {
                        //If this selection was already selected, no need to do anything.
                        if (Objects.equals(m_betChoice, "YRFI")) {
                            return;
                        }
                        //Otherwise, update the bet choice, reverse the ordering, and rebuild the table.
                        else
                        {
                            m_betChoice = "YRFI";
                            Collections.reverse(m_NRFIYRFIBetPredictions);
                            FillNRFIYRFITable();
                        }
                    }
                }
            }

            //If nothing ends up selected, there's nothing to do.
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        //Fill in the NRFI/YRFI table for the first time.
        FillNRFIYRFITable();
    }

    /**
     * Dynamically fills in the TableLayout with the NRFI/YRFI bet predictions.
     * <p>
     * This method dynamically fills in the TableLayout with TableRows, with each TableRow representing a NRFI or YRFI bet prediction for the current
     * day. Each created TableRow object contains the away team playing in the game and associated logo, the home team playing in the game and
     * associated logo, the local start time of the game, and the overall bet score. Additionally, each TableRow object has its OnClickListener
     * set so that if you click anywhere in the row, it takes you to the NRFIYRFIDetailsActivity screen to show more information about the bet prediction.
     * <p>
     * Assistance Received:
     * <p>
     * https://stackoverflow.com/questions/6583843/how-to-access-resource-with-dynamic-name-in-my-case
     * <p>
     * https://stackoverflow.com/questions/8669747/dynamically-add-imageview-to-tablerow
     */
    private void FillNRFIYRFITable() {
        if(m_NRFIYRFIBetPredictions.isEmpty()) {
            return;
        }

        //Set the date for the NRFI/YRFI table.
        m_dateTextView.setText((String) m_NRFIYRFIBetPredictions.get(0).get("Date"));

        //Remove all the rows, if any already exist (for when changing bet types).
        m_tableLayout.removeAllViews();

        //Add an empty row to the beginning of the table, so that the initial divider gets shown.
        TableRow emptyTopRow = new TableRow(this);
        m_tableLayout.addView(emptyTopRow);

        //Loop through each of the bet predictions.
        for (HashMap<String, Object> betPrediction : m_NRFIYRFIBetPredictions) {
            //Create a new table row.
            TableRow tableRow = new TableRow(this);

            //Create OnClickListener for the entire TableRow. This will view the NRFI or YRFI bet prediction in more detail when clicked.
            tableRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Switch to the NRFI/YRFI details state.
                    Intent intent = new Intent(getApplicationContext(), NRFIYRFIDetailsActivity.class);

                    //Pass the bet details and bet choice to the NRFIYRFIDetails Activity.
                    intent.putExtra("betDetails", betPrediction);
                    intent.putExtra("betChoice", m_betChoice);
                    startActivity(intent);
                }
            });

            //Create the away team logo and abbreviation.
            String awayTeamName = (String) betPrediction.get("Away Team Name");
            ImageView awayTeamLogo = WidgetUtilities.CreateTeamLogo(this, awayTeamName, 150, 150, 30, 20, 20, 20);
            TextView awayTeamAbbreviation = WidgetUtilities.CreateTextView(this, BetPredictorModel.TEAM_ABBREVIATION.get(awayTeamName), 15, 0, 50, 20, 20);
            tableRow.addView(awayTeamLogo);
            tableRow.addView(awayTeamAbbreviation);

            //Create the @ sign between the team logos.
            TextView atSign = WidgetUtilities.CreateTextView(this,"@", 15, 0, 50, 20, 20);
            tableRow.addView(atSign);

            //Create the home team logo and abbreviation.
            String homeTeamName = (String) betPrediction.get("Home Team Name");
            ImageView homeTeamLogo = WidgetUtilities.CreateTeamLogo(this, homeTeamName, 150, 150, 10, 20, 20, 20);
            TextView homeTeamAbbreviation = WidgetUtilities.CreateTextView(this, BetPredictorModel.TEAM_ABBREVIATION.get(homeTeamName), 15, 0, 50, 40, 20);
            tableRow.addView(homeTeamLogo);
            tableRow.addView(homeTeamAbbreviation);

            //Set the local time of the game based on the timezone.
            TimeZone localTimezone = TimeZone.getDefault();
            String UTCGameTime = (String) betPrediction.get("DateTime String");
            String localGameTime = m_BPModelObj.ConvertToTimezone(UTCGameTime, localTimezone.getID());
            TextView gameTime = WidgetUtilities.CreateTextView(this, localGameTime, 15, 0, 50, 60, 20);
            tableRow.addView(gameTime);

            //Create the NRFI or YRFI score. Note: All scores are stored in the database as "Overall NRFI Score". Low NRFI scores are good for NRFI, and high NRFI scores are good for YRFI.
            String roundedScore = String.format("%.1f", (Double) betPrediction.get("Overall NRFI Score") * 100.0);
            TextView betChoice = WidgetUtilities.CreateTextView(this, "", 15, 0, 50, 0, 20);
            betChoice.setText(WidgetUtilities.MakePartialTextBold("Score: ", roundedScore));
            tableRow.addView(betChoice);

            m_tableLayout.addView(tableRow);
        }

        //Set the backgrounds of the top bets to gold, silver, and bronze.
        WidgetUtilities.SetTopBets(m_tableLayout, 1, 1 ,1);

        //Add an empty row to the end so that the divider gets shown on the bottom of the last row.
        TableRow emptyBottomRow = new TableRow(this);
        m_tableLayout.addView(emptyBottomRow);
    }
}