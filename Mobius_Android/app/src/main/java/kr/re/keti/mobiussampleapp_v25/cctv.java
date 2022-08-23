package kr.re.keti.mobiussampleapp_v25;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

//import androidx.appcompat.app.AppCompatActivity;

public class cctv extends AppCompatActivity {
    WebView webV;
    WebSettings settings;
    Button returnButton;
    Button emailButton;
    Button cctv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cctv);

        webV=(WebView)findViewById(R.id.web);
        webV.setWebViewClient(new WebViewClient());
        settings=webV.getSettings();
        settings.setJavaScriptEnabled(true);
        webV.loadUrl("http://192.168.11.205:7003/");

        returnButton = (Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        emailButton =(Button)findViewById(R.id.emailButton);
        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webV.loadUrl("http://mail.naver.com");
            }
        });
        cctv=(Button)findViewById(R.id.cctv);
        cctv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webV.loadUrl("http://192.168.11.205:7003/");
            }
        });
    }
}