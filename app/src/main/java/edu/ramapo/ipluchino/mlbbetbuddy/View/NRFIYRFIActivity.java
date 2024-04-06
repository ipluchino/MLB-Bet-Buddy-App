package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.Manifest;

import edu.ramapo.ipluchino.mlbbetbuddy.Model.BetPredictorModel;
import edu.ramapo.ipluchino.mlbbetbuddy.R;

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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

public class NRFIYRFIActivity extends AppCompatActivity {
    //Constants.
    private final String NRFIYRFI_TABLE_NAME = "TodayNRFI";

    //Private variables
    private BetPredictorModel m_BPModelObj;
    private Vector<HashMap<String, Object>> m_games;
    private TableLayout m_tableLayout;
    private Spinner m_betChoiceSpinner;
    private TextView m_dateTextView;
    private Button m_homeButton;

    //Constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrfiyrfi);

        //Initialize the private variables.
        m_BPModelObj = new BetPredictorModel();
        m_tableLayout = findViewById(R.id.NRFIYRFITable);
        m_betChoiceSpinner = findViewById(R.id.titleSpinnerNRFIYRFI);
        m_dateTextView = findViewById(R.id.dateTextViewMRFIYRFI);
        m_homeButton = findViewById(R.id.homeButtonMRFIYRFI);

        //Attempt to query the server for the schedule information. This is done on a separate thread, not the main thread.
        GetGames();

        //If no games are returned from the server, display a message alerting the user.
        //if (m_games.isEmpty()) {
            //DisplayLackOfData();
        //}

        //Set all of the onClick listeners for the buttons.
        m_homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigate back to the home screen of the app.
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
            }
        });

        //Fill in the schedule table.
        FillNRFIYRFITable();
    }

    //Gets today's schedule from the server.
    //Assistance: https://stackoverflow.com/questions/6343166/how-can-i-fix-android-os-networkonmainthreadexception
    private void GetGames() {
    }

    //Fills in the schedule table dynamically.
    //https://stackoverflow.com/questions/6583843/how-to-access-resource-with-dynamic-name-in-my-case
    //https://stackoverflow.com/questions/8669747/dynamically-add-imageview-to-tablerow
    private void FillNRFIYRFITable() {
        //if (m_games.isEmpty()) {
        //    return;
        //}
    }
}
