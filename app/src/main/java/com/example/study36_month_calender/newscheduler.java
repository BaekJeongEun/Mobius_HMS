package com.example.study36_month_calender;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Executors;

public class newscheduler extends AppCompatActivity  {

    MaterialCalendarView calendarView;
    String[] eventDay ={"2019,11,10","2019,11,17","2019,11,20","2019,11,23"}; //특정 날짜 선택, 마지막 배열에 있는 날짜는 출력되지 않음.

    TextView info;
    String infoString;
    String wday, wtext;
    EditText wantDay, wantText;
    int numday;
    Button register;
    ArrayList<ArrayText> list = new ArrayList<>();

    ApiSimulator dotday;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newscheduler);

        info = (TextView) findViewById(R.id.infoTextView);
        wantDay = (EditText) findViewById(R.id.wantDay);
        wantText = (EditText) findViewById(R.id.wantText);

        dotday = new ApiSimulator(eventDay);

        Button returnButton = (Button)findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        Button searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), scd_search.class);
                startActivity(intent);
            }
        });

        register = (Button) findViewById(R.id.registerButton);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wday = wantDay.getText().toString();
                wtext = wantText.getText().toString();
                numday = Integer.parseInt(wday);
                list.add(new ArrayText(numday, wtext));
                dotday.addDay(2019,11,numday);




            }
        });


        calendarView = (MaterialCalendarView)findViewById(R.id.calendar); //calendarView init
        calendarView.state().edit() //CalendarView 설정
                .setFirstDayOfWeek(Calendar.SUNDAY)
                .setMinimumDate(CalendarDay.from(2019,6,10))
                .setMaximumDate(CalendarDay.from(2021,11,14))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();
        calendarView.addDecorators(new WeekendDecorator(0),new WeekendDecorator(1),new oneDayDecorater());
        new ApiSimulator(eventDay).executeOnExecutor(Executors.newSingleThreadExecutor());
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                int year =date.getYear();
                int month =date.getMonth()+1;
                int day =date.getDay();

                String shot_day =year+","+month+","+day;
                Log.e("?",shot_day);

                Toast.makeText(getApplicationContext(), shot_day, Toast.LENGTH_SHORT).show();

                /*infomationString(day);
                info.setText(infoString);*/
                //String infoString = "문자열 출력하기";

                /*if(register.isClickable()){
                    infomationString(numday);
                    info.setText(infoString);
                }*/

                if(day==10) infoString = "최철훈 할아버지 치과 임플란트 예약";
                if(day==17) infoString = "김수자 할머니 퇴원 예정";
                if(day==20) infoString = "이예순 할머니 폐렴구균 백신 예방접종 예약";
                info.setText(infoString);

                for(ArrayText at : list){
                    if(day==at.arrayDay){
                        infoString=at.arrayString;
                        info.setText(infoString);
                       // calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));


                    }
                    else
                        infoString = "";
                }

                //info.setText(infoString);



                calendarView.clearSelection();
            }
        });


    }

    //날짜에 따라 출력되는 정보(환자정보 등)를 다르게 설정
    /*public void infomationString(int day){

        if(day==10) infoString = "최철훈 할아버지 치과 임플란트 예약";
        else if(day==17) infoString = "김수자 할머니 퇴원 예정";
        else if(day==20) infoString = "이예순 할머니 폐렴구균 백신 예방접종 예약";
        else if(day==numday) infoString = wtext;
        else infoString = " ";
    }*/


    //날짜를 꾸며주는 데코레이터 클래스들
    //weekend - 토(파랑), 일(빨강)으로 색 설정
    //oneday -오늘 날짜의 색 설정
    //eventDecorator - 특정 날짜에 효과주기
    //출처 : https://dpdpwl.tistory.com/3



    public class WeekendDecorator implements DayViewDecorator {

        Calendar calendar =Calendar.getInstance();
        int sundayAndSaturday; // 0이면 일요일, 1이면 토요일로지정

        public WeekendDecorator(int sundayAndSaturday){
            this.sundayAndSaturday =sundayAndSaturday;
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            day.copyTo(calendar);
            int weekday = calendar.get(Calendar.DAY_OF_WEEK);
            if(sundayAndSaturday == 0 )
                return weekday == Calendar.SUNDAY;
            else
                return weekday == Calendar.SATURDAY;
        }

        @Override
        public void decorate(DayViewFacade view) {
            if(sundayAndSaturday == 0)
                view.addSpan(new ForegroundColorSpan(Color.RED));
            else
                view.addSpan(new ForegroundColorSpan(Color.BLUE));
        }
    }

    public class oneDayDecorater implements DayViewDecorator{

        CalendarDay date;

        public oneDayDecorater(){
            date = CalendarDay.today();
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null & day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(2.0f));
            view.addSpan(new ForegroundColorSpan(Color.BLACK));
        }

        public void setDate(Date date){
            this.date = CalendarDay.from(date);
        }
    }
    public class EventDecorator implements DayViewDecorator{

        int color;
        HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay>dates){
            this.color = color;
            this.dates = new HashSet<>(dates);
        }
        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5,color));
        }
    }
    public class ApiSimulator extends AsyncTask<Void,Void, List<CalendarDay>>{

        String [] eventday;
        int year, month, dayofmonth;

        public void addDay(int y, int m, int d){
            this.year=y;
            this.month=m;
            this.dayofmonth=d;

        }

        public ApiSimulator(String [] eventday){
            this.eventday = eventday;
        }
        @Override
        protected List<CalendarDay> doInBackground(Void... voids) {
            try {
                Thread.sleep(500);
            } catch (Exception e) {e.printStackTrace();}

            Calendar calendar = Calendar.getInstance();
            ArrayList<CalendarDay> dates = new ArrayList<>();
            //특정 날짜 달력에 점 표시해주는 곳
            //String인 문자열인 eventday를 받아와서 ,를 기준으로 자르고 String을 int로 변환
            for(int i=0; i<eventday.length; i++){
                CalendarDay day = CalendarDay.from(calendar);
                String[] time = eventDay[i].split(",");
                year = Integer.parseInt(time[0]);
                month = Integer.parseInt(time[1]);
                dayofmonth = Integer.parseInt(time[2]);

                dates.add(day);
                calendar.set(year,month-1,dayofmonth);
            }
            return dates;
        }

        @Override
        protected void onPostExecute(@NonNull List<CalendarDay> calendarDays) {
            super.onPostExecute(calendarDays);

            if (isFinishing()) {
                return;
            }

            calendarView.addDecorator(new EventDecorator(Color.RED, calendarDays));


        }


    }

    public class ArrayText {
        int arrayDay;
        String arrayString;

        public ArrayText(int day, String text){
            arrayDay = day;
            arrayString = text;
        }

        public int getArrayDay() {
            return arrayDay;
        }

        public void setArrayDay(int arrayDay) {
            this.arrayDay = arrayDay;
        }

        public String getArrayString() {
            return arrayString;
        }

        public void setArrayString(String arrayString) {
            this.arrayString = arrayString;
        }
    }


}
