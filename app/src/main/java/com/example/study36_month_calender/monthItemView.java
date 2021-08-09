package com.example.study36_month_calender;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import javax.security.auth.login.LoginException;

import static com.example.study36_month_calender.R.drawable.grid_line;

public class monthItemView extends RelativeLayout {
    TextView textView;
    Drawable drawable;
    public monthItemView(Context context) {
        super(context);
        init(context);
    }

    public monthItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    private void init(Context context){
        LayoutInflater inflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.month_item,this,true);

      drawable=this.getResources().getDrawable(grid_line);
      textView=(TextView)findViewById(R.id.textView);



    }

    public void setDay(int day){
        textView.setText(String.valueOf(day));
        if(day==0){
            textView.setText("");
            textView.setBackgroundColor(Color.WHITE);
        }else {
            textView.setBackground(drawable);
        }



    }
}
