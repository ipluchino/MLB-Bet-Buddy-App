package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.Manifest;
import edu.ramapo.ipluchino.mlbbetbuddy.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {
    //Private variables
    private Button m_scheduleButton;
    private Button m_NRFIYRFIButton;
    private Button m_hittingButton;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        //Initialize the private variables.
        m_scheduleButton = findViewById(R.id.scheduleButton);
        m_NRFIYRFIButton = findViewById(R.id.NRFIYRFIButton);
        m_hittingButton = findViewById(R.id.hittingButton);

        //Set all of the onClick listeners for the buttons.
        m_scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to the schedule screen.
                Intent intent = new Intent(getApplicationContext(), ScheduleActivity.class);
                startActivity(intent);
            }
        });

        m_NRFIYRFIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Determine which type of bet the user would like to view.
                DetermineNRFIYRFI();
            }
        });

        m_hittingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate to the schedule screen.
                Intent intent = new Intent(getApplicationContext(), HittingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void DetermineNRFIYRFI() {
        //Create an alert dialog box to get input from the user.
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreenActivity.this);
        builder.setTitle("Choose a bet type");
        builder.setMessage("Would you like to view the NRFI or YRFI bets?");

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
