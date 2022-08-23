package kr.re.keti.mobiussampleapp_v25;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

//import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class scd_search extends AppCompatActivity {

    ListView listview;
    TextView name, genderAgeView, diseaseView, timeView, numView, searchTitle;
    String titleStr;
    ListViewAdapter adapter;
    ListViewItem item;
    PatientInfo selectParent, searchPatient;
    Button searchNameButton;
    EditText searchName;
    String name123 = null;

    Button goCalender, goRegister;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scd_search);

        goCalender = (Button)findViewById(R.id.goCalendar);
        goCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), newscheduler.class);
                startActivity(i);
            }
        });



        final ArrayList<PatientInfo> pList = new ArrayList<>();

        adapter = new ListViewAdapter();

        name = (TextView) findViewById(R.id.InfoName);
        genderAgeView = (TextView) findViewById(R.id.InfoGenderAge);
        diseaseView = (TextView) findViewById(R.id.InfoDisease);
        timeView = (TextView) findViewById(R.id.InfoTime);
        numView = (TextView) findViewById(R.id.InfoNum);

        searchName = (EditText) findViewById(R.id.searchNameText);


        searchTitle = (TextView) findViewById(R.id.searchTitle);

        searchNameButton = (Button) findViewById(R.id.searchNameButton);


        listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);

        adapter.addItem("방호남");
        adapter.addItem("정승우");
        adapter.addItem("최철훈");
        adapter.addItem("김수자");
        adapter.addItem("박민덕");
        adapter.addItem("이예순");
        adapter.addItem("길태자");
        adapter.addItem("백정순");
        adapter.addItem("성다옥");


        /*
        *
        * final Handler handler = new Handler(){
            @Override public void handleMessage(Message msg){
                textView.setText(selectParent.gender + ", " + selectParent.age + ", "+ selectParent.disease + ", " + selectParent.time + ", 보호자 연락처 : " + selectParent.phone);
            }
        };
        * */

        pList.add(new PatientInfo("방호남", "남성", "75세", "노인성 치매", "식전 투약할 것", "010-1111-1111"));
        pList.add(new PatientInfo("정승우", "남성", "71세", "알츠하이머", "식사 후 10분 이내 투약할 것", "010-2222-2222"));
        pList.add(new PatientInfo("최철훈", "남성", "69세", "파킨슨병", "식사 후 30분 이후 투약할 것", "010-3333-3333"));
        pList.add(new PatientInfo("김수자", "여성", "69세", "노인성 치매", "식전 투약할 것", "010-4444-4444"));
        pList.add(new PatientInfo("박민덕", "여성", "65세", "혈관성 치매", "식전 투약할 것", "010-5555-5555"));
        pList.add(new PatientInfo("이예순", "여성", "63세", "알츠하이머", "식사 후 10분 이내 투약할 것", "010-6666-6666"));
        pList.add(new PatientInfo("길태자", "여성", "63세", "혈관성 치매", "식전 투약할 것", "010-7777-7777"));
        pList.add(new PatientInfo("백정순", "여성", "62세", "알츠하이머", "식사 후 10분 이내 투약할 것", "010-8888-8888"));
        pList.add(new PatientInfo("성다옥", "여성", "64세", "파킨슨병", "식사 후 30분 이후 투약할 것", "010-9999-9999"));


        for (PatientInfo i : pList) {
            Log.e("이름", i.name);
        }

        /*item2 = (ListViewItem) listview.getAdapter();
        printStr = item2.getTitle();

        for(PatientInfo pi : pList){
            if(printStr.equals(pi.name)){
                searchPatient = pi;
            }
        }
        searchNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText(searchPatient.name+"");
                genderAgeView.setText(searchPatient.gender + ", " + searchPatient.age+"   ");
                diseaseView.setText(searchPatient.disease+" 환자   ");
                timeView.setText(searchPatient.time+"   ");
                numView.setText("보호자 연락처 : " + searchPatient.phone);
            }
        });*/




        searchNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name123 = searchName.getText().toString();


                for(PatientInfo pi : pList){
                    if(name123.equals(pi.name)){
                        //Log.e("바뀌어라 검색.!", searchTitle.getText().toString());
                        //searchTitle.setText(pi.getName());
                        searchPatient = pi;

                        searchTitle.setText("검색");
                        name.setText(searchPatient.name + "");
                        genderAgeView.setText(searchPatient.gender + ", " + searchPatient.age + "   ");
                        diseaseView.setText(searchPatient.disease + " 환자   ");
                        timeView.setText(searchPatient.time + "   ");
                        numView.setText("보호자 연락처 : " + searchPatient.phone);
                        break;
                    }
                    else {
                        searchTitle.setText("없는 이름입니다. 다시 검색해주세요.");
                    }
                }
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                item = (ListViewItem) parent.getItemAtPosition(position);
                titleStr = item.getTitle();

                //Log.e("당신의 페어런츠", "아이템 타이틀 :" + titleStr);
                //String gender, age, disease, time, phone;
                for (PatientInfo pi : pList) {
                    if (titleStr.equals(pi.name)) {
                        selectParent = pi;
                    }
                }
/*
                    //searchName.getText()
                for(PatientInfo pi : pList){
                    if(searchName.getText().equals(pi.name)){
                        selectParent = pi;

                        searchNameButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                searchTitle.setText(selectParent.name+"");

                            }
                        });
                    }
                }
*/
                //textView.setText(selectParent.gender + ", " + selectParent.age + ", "+ selectParent.disease + ", " + selectParent.time + ", 보호자 연락처 : " + selectParent.phone);

                name.setText(selectParent.name + "");
                genderAgeView.setText(selectParent.gender + ", " + selectParent.age + "   ");
                diseaseView.setText(selectParent.disease + " 환자   ");
                timeView.setText(selectParent.time + "   ");
                numView.setText("보호자 연락처 : " + selectParent.phone);

/*
                if(titleStr == "방호남")
                else if(titleStr == "정승우")
                    textView.setText("남성, 71세, 알츠하이머병, 식사 후 10분 이내 투약할 것, 보호자 연락처: 010-2222-2222");
                else if(titleStr == "최철훈")
                    textView.setText("남성, 69세, 파킨슨병, 식사 후 30분 이후 투약할 것, 보호자 연락처: 010-3333-3333");
                else if(titleStr == "김수자")
                    textView.setText("여성, 69세, 노인성 치매, 식전 투약할 것, 보호자 연락처: 010-4444-4444");
                else if(titleStr == "박민덕")
                    textView.setText("여성, 65세, 혈관성 치매, 식전 투약할 것, 보호자 연락처: 010-5555-5555");
                else if(titleStr == "이예순")
                    textView.setText("여성, 63세, 알츠하이머병, 식사 후 10분 이내 투약할 것, 보호자 연락처: 010-6666-6666");
                else if(titleStr == "길태자")
                    textView.setText("여성, 63세, 혈관성 치매, 식전 투약할 것, 보호자 연락처: 010-7777-7777");
                else if(titleStr == "백정순")
                    textView.setText("여성, 62세, 알츠하이머병, 식사 후 10분 이내 투약할 것, 보호자 연락처: 010-8888-8888");
                else if(titleStr == "성다옥")
                    textView.setText("여성, 64세, 파킨슨병, 식사 후 30분 이후 투약할 것, 보호자 연락처: 010-9999-9999");

*/
            }

        });

    }

}
