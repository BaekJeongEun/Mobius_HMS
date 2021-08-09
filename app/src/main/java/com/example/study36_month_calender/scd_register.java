package com.example.study36_month_calender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.sql.Connection;
import java.sql.DriverManager;

public class scd_register extends AppCompatActivity {

    Button goCalender, goSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scd_register);

        goCalender = (Button)findViewById(R.id.goCalendar);
        goCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), scheduler.class);
                startActivity(i);
            }
        });

        goSearch = (Button)findViewById(R.id.goSearch);
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), scd_search.class);
                startActivity(i);
            }
        });

    }
}