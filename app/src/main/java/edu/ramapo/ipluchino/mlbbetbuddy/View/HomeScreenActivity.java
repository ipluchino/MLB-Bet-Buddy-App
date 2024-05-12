/**
 *********************************************************************************************************************************
 * Author: Ian Pluchino                                                                                                          *
 * Class: HomeScreenActivity class                                                                                               *
 * Description: A view class, used to configure and display the HomeScreen screen of the application.                            *
 * Date: 5/2/24                                                                                                                  *
 *********************************************************************************************************************************
 */

package edu.ramapo.ipluchino.mlbbetbuddy.View;

import edu.ramapo.ipluchino.mlbbetbuddy.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {
    //PRIVATE VARIABLES
    private Button m_scheduleButton;
    private Button m_NRFIYRFIButton;
    private Button m_hittingButton;

    /**
     * Creates the HomeScreenActivity.
     * <p>
     * This method creates the HomeScreenActivity and sets the screen's view to the home screen layout. It also initializes the private variables,
     * sets event handlers for buttons to navigate around the app.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most
     *        recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        //Initialize the private variables.
        m_scheduleButton = findViewById(R.id.scheduleButton);
        m_NRFIYRFIButton = findViewById(R.id.NRFIYRFIButton);
        m_hittingButton = findViewById(R.id.hittingButton);

        //OnClick listener for the schedule button.
        m_scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to the schedule screen.
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);
            }
        });

        //OnClick listener for the NRFI & YRFI button.
        m_NRFIYRFIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Determine which type of bet the user would like to view.
                DetermineNRFIYRFI();
            }
        });

        //OnClick listener for the hitting button.
        m_hittingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to the schedule screen.
                Intent intent = new Intent(getApplicationContext(), HittingActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Determines the initial bet choice if the user wishes to view NRFI/YRFI bet predictions.
     * <p>
     * This method is used to determine which type of bet, either NRFI or YRFI, the user would like to view when they select the "NRFI & YRFI" button
     * on the home screen. The choice is determined via an alert dialog. Once the user makes a selection, the screen is switched to the NRFIYRFIActivity,
     * with the initial ordering of the bets depending on what the user selected on the alert dialog.
     */
    private void DetermineNRFIYRFI() {
        //Create an alert dialog box to get input from the user.
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
        builder.setTitle("Bet Choice");
        builder.setMessage("Please choose a bet type: NRFI or YRFI. \n\nNote: The lower the bet score, the better for NRFI, and the higher the bet score, the better for YRFI.");

        //OK button to clear the alert dialog.
        builder.setPositiveButton("YRFI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Navigate to the YRFI screen.
                Intent intent = new Intent(getApplicationContext(), NRFIYRFIActivity.class);
                intent.putExtra("betChoice", "YRFI");
                startActivity(intent);
            }
        });

        //OK button to clear the alert dialog.
        builder.setNeutralButton("NRFI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //Navigate to the NRFI screen.
                Intent intent = new Intent(getApplicationContext(), NRFIYRFIActivity.class);
                intent.putExtra("betChoice", "NRFI");
                startActivity(intent);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}