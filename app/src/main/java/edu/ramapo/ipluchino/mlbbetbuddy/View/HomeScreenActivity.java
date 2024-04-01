package edu.ramapo.ipluchino.mlbbetbuddy.View;

import android.Manifest;
import edu.ramapo.ipluchino.mlbbetbuddy.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreenActivity extends AppCompatActivity {
    //On create constructor.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Test", "Test");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
    }

}
