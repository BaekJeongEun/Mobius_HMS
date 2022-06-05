package com.example.study36_month_calender;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class doorlock extends AppCompatActivity {

    ToggleButton onoffButton;
    TextView tellingText;
    MyService myService;
    boolean isService = false;
    ImageView imageView1 = null;

    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorlock);



        serviceBind();

        imageView1 = (ImageView)findViewById(R.id.doorlockImage);

        Button returnButton = (Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        tellingText = (TextView) findViewById(R.id.tellingText);

        onoffButton = (ToggleButton)findViewById(R.id.onoffButton);

        /*onoffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i = 1 - i;

                if ( i == 0 ){
                    imageView1.setImageResource(R.drawable.closeddoor);
                }
                else{
                    imageView1.setImageResource(R.drawable.opendoor);
                }
            }
        });*/
        onoffButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonview, boolean isChecked) {


                if(isChecked == true) {
                    onoffButton.setTextOn("문 닫힘");
                    tellingText.setText("문이 열렸습니다.");
                    myService.sendMessage("1");
                    imageView1.setImageResource(R.drawable.opendoor);
                }

                else {
                    onoffButton.setTextOff("문 열림");
                    tellingText.setText("문이 닫혔습니다.");
                    myService.sendMessage("2");
                    imageView1.setImageResource(R.drawable.closeddoor);


                }
            }
        });
    }

    public void serviceBind(){
        Intent intent = new Intent(
                doorlock.this, // 현재 화면
                MyService.class); // 다음넘어갈 컴퍼넌트

        bindService(intent, // intent 객체
                conn, // 서비스와 연결에 대한 정의
                Context.BIND_NOT_FOREGROUND);
        //처음 서비스를 시작하는 액티비티에서는 Context.BIND_AUTO_CREATE
        //다른 액티비티에서는 Context.BIND_NOT_FOREGROUND를 주어야합니다.
    }

    protected void onDestroy() {
        super.onDestroy();
        if(isService){
            unbindService(conn); // 서비스 종료
            isService = false;
        }
    }

    protected void onResume() {
        super.onResume();

    }

    ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            // 서비스와 연결되었을 때 호출되는 메서드
            // 서비스 객체를 전역변수로 저장
            MyService.MyBinder mb = (MyService.MyBinder) service;
            myService = mb.getService(); // 서비스가 제공하는 메소드 호출하여
            // 서비스쪽 객체를 전달받을수 있슴
            isService = true;
            Toast.makeText(getApplicationContext(),
                    "서비스 연결",
                    Toast.LENGTH_LONG).show();
        }

        public void onServiceDisconnected(ComponentName name) {
            // 서비스와 연결이 끊겼을 때 호출되는 메서드
            isService = false;
            Toast.makeText(getApplicationContext(),
                    "서비스 연결 해제",
                    Toast.LENGTH_LONG).show();
        }
    };

}
