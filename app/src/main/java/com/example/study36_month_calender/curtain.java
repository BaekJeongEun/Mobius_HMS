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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class curtain extends AppCompatActivity implements View.OnClickListener {

    final int OPEN = 3;
    final int CLOSE = 4;
    final int STOP = 5;
    MyService myService;
    boolean isService =false;
    String text;
    EditText alarm;
    TextView curtainControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);

        serviceBind();
        findViewById(R.id.IPButton).setOnClickListener(this);
        findViewById(R.id.openButton).setOnClickListener(this);
        findViewById(R.id.closedButton).setOnClickListener(this);
        //findViewById(R.id.stopButton).setOnClickListener(this);
        curtainControl = (TextView)findViewById(R.id.curtainControl);

        alarm = (EditText) findViewById(R.id.IPText);
        text = alarm.getText().toString();

        Button returnButton = (Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void serviceBind(){
        Intent intent = new Intent(
                curtain.this, // 현재 화면
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


    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.openButton :
                myService.sendMessage(OPEN+"");
                curtainControl.setText("커튼이 열렸습니다.");

            break;
            case R.id.closedButton:
                myService.sendMessage(CLOSE+"");
                curtainControl.setText("커튼이 닫혔습니다.");
                break;
            /*case R.id.stopButton:
                myService.sendMessage(String.valueOf(STOP));
                curtainControl.setText("커튼이 멈췄습니다.");
                break;*/
            case R.id.IPButton:
                text = alarm.getText().toString();
                myService.sendMessage(text);
                break;

        }
    }
}
