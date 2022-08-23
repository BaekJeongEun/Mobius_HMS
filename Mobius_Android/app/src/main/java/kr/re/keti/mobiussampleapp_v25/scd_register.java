package kr.re.keti.mobiussampleapp_v25;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

//import androidx.appcompat.app.AppCompatActivity;

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