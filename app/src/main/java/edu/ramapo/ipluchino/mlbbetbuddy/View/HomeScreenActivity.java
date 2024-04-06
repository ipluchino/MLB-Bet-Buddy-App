package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.Manifest;
import edu.ramapo.ipluchino.mlbbetbuddy.R;
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
                //Navigate to the schedule screen.
                Intent intent = new Intent(getApplicationContext(), NRFIYRFIActivity.class);
                startActivity(intent);
            }
        });
    }

}
