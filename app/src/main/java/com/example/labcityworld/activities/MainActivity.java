package com.example.labcityworld.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labcityworld.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab = (FloatingActionButton) findViewById(R.id.fabAddCity);

    }
}