package kr.re.keti.mobiussampleapp_v25;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;


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
                //myService.sendMessage(OPEN+"");
                curtainControl.setText("커튼이 열렸습니다.");
                ControlRequest req = new ControlRequest("3");
                req.setReceiver(new MainActivity.IReceived() {
                    public void getResponseBody(final String msg) {
                        MainActivity.handler.post(new Runnable() {
                            public void run() {
                               // textViewData.setText("************** 도어락 제어(열림) *************\r\n\r\n" + msg);
                            }
                        });
                    }
                });
                req.start();

                break;
            case R.id.closedButton:
                //myService.sendMessage(CLOSE+"");
                curtainControl.setText("커튼이 닫혔습니다.");
                ControlRequest req2 = new ControlRequest("4");
                req2.setReceiver(new MainActivity.IReceived() {
                    public void getResponseBody(final String msg) {
                        MainActivity.handler.post(new Runnable() {
                            public void run() {
                               // textViewData.setText("************** 도어락 제어(열림) *************\r\n\r\n" + msg);
                            }
                        });
                    }
                });
                req2.start();
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
    class ControlRequest extends Thread {
        private final Logger LOG = Logger.getLogger(doorlock.ControlRequest.class.getName());
        private MainActivity.IReceived receiver;
        private String container_name = "door" +"";

        public ContentInstanceObject contentinstance;
        public ControlRequest(String comm) {
            contentinstance = new ContentInstanceObject();
            contentinstance.setContent(comm);
        }
        public void setReceiver(MainActivity.IReceived hanlder) { this.receiver = hanlder; }

        @Override
        public void run() {
            try {
                //String sb = "http://203.253.128.161:7579/Mobius/mite/curtain";
                String sb = MainActivity.csebase.getServiceUrl() +"/" + MainActivity.ServiceAEName + "/" + container_name;

                URL mUrl = new URL(sb);

                HttpURLConnection conn = (HttpURLConnection) mUrl.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setInstanceFollowRedirects(false);

                conn.setRequestProperty("Accept", "application/xml");
                conn.setRequestProperty("Content-Type", "application/vnd.onem2m-res+xml;ty=4");
                conn.setRequestProperty("locale", "ko");
                conn.setRequestProperty("X-M2M-RI", "12345");
                conn.setRequestProperty("X-M2M-Origin", MainActivity.ae.getAEid() );

                String reqContent = contentinstance.makeXML();
                conn.setRequestProperty("Content-Length", String.valueOf(reqContent.length()));

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.write(reqContent.getBytes());
                dos.flush();
                dos.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                String resp = "";
                String strLine="";
                while ((strLine = in.readLine()) != null) {
                    resp += strLine;
                }
                if (resp != "") {
                    receiver.getResponseBody(resp);
                }
                conn.disconnect();

            } catch (Exception exp) {
                LOG.log(Level.SEVERE, exp.getMessage());
            }
        }
    }
}
