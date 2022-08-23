package kr.re.keti.mobiussampleapp_v25;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

//import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class scd_search2 extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Main";

    Button btn_Update, btn_Insert, btn_Select, btn_Return;
    EditText edit_ID, edit_Name, edit_Age, edit_Info, edit_Medicine, edit_Number;
    TextView text_ID, text_Name, text_Age, text_Gender, text_Info, text_Medicine, text_Number;
    CheckBox check_Man, check_Woman, check_ID, check_Name, check_Age;

    long nowIndex;
    String ID, name, Info, Medicine;
    long age, Number;
    String gender = "";
    String sort = "userid";

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();
    private DbOpenHelper mDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scd_search2);

        btn_Insert = (Button) findViewById(R.id.btn_insert);
        btn_Insert.setOnClickListener(this);
        btn_Update = (Button) findViewById(R.id.btn_update);
        btn_Update.setOnClickListener(this);
        btn_Select = (Button) findViewById(R.id.btn_select);
        btn_Select.setOnClickListener(this);
        btn_Return = (Button)findViewById(R.id.btn_return);
        btn_Return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), newscheduler.class);
                startActivity(i);
            }
        });

        edit_ID = (EditText) findViewById(R.id.edit_id);
        edit_Name = (EditText) findViewById(R.id.edit_name);
        edit_Age = (EditText) findViewById(R.id.edit_age);
        edit_Info = (EditText) findViewById(R.id.edit_info);
        edit_Medicine = (EditText)findViewById(R.id.edit_medicine);
        edit_Number = (EditText)findViewById(R.id.edit_number);

        text_ID = (TextView) findViewById(R.id.text_id);
        text_Name = (TextView) findViewById(R.id.text_name);
        text_Age = (TextView) findViewById(R.id.text_age);
        text_Gender = (TextView) findViewById(R.id.text_gender);
        text_Info = (TextView) findViewById(R.id.text_info);
        text_Medicine = (TextView) findViewById(R.id.text_medicine);
        text_Number = (TextView) findViewById(R.id.text_number);

        check_Man = (CheckBox) findViewById(R.id.check_man);
        check_Man.setOnClickListener(this);
        check_Woman = (CheckBox) findViewById(R.id.check_woman);
        check_Woman.setOnClickListener(this);
        check_ID = (CheckBox) findViewById(R.id.check_userid);
        check_ID.setOnClickListener(this);
        check_Name = (CheckBox) findViewById(R.id.check_name);
        check_Name.setOnClickListener(this);
        check_Age = (CheckBox) findViewById(R.id.check_age);
        check_Age.setOnClickListener(this);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        ListView listView = (ListView) findViewById(R.id.db_list_view);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(onClickListener);
        listView.setOnItemLongClickListener(longClickListener);

        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        check_ID.setChecked(true);
        showDatabase(sort);

        btn_Insert.setEnabled(true);
        btn_Update.setEnabled(false);
    }

    public void setInsertMode(){
        edit_ID.setText("");
        edit_Name.setText("");
        edit_Age.setText("");
        edit_Info.setText("");
        edit_Medicine.setText("");
        edit_Number.setText("");

        check_Man.setChecked(false);
        check_Woman.setChecked(false);
        btn_Insert.setEnabled(true);
        btn_Update.setEnabled(false);
    }

    private AdapterView.OnItemClickListener onClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("On Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            Log.e("On Click", "nowIndex = " + nowIndex);
            Log.e("On Click", "Data: " + arrayData.get(position));
            String[] tempData = arrayData.get(position).split("\\s+");
            Log.e("On Click", "Split Result = " + tempData);

            edit_ID.setText(tempData[0].trim());
            edit_Name.setText(tempData[1].trim());
            edit_Age.setText(tempData[2].trim());
            edit_Info.setText(tempData[3].trim());
            if(tempData[4].trim().equals("Man")){
                check_Man.setChecked(true);
                gender = "Man";
            }else{
                check_Woman.setChecked(true);
                gender = "Woman";
            }
            edit_Medicine.setText(tempData[5].trim());
            edit_Number.setText(tempData[6].trim());

            btn_Insert.setEnabled(false);
            btn_Update.setEnabled(true);
        }
    };

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            nowIndex = Long.parseLong(arrayIndex.get(position));
            String[] nowData = arrayData.get(position).split("\\s+");
            String viewData = nowData[0] + ", " + nowData[1] + ", " + nowData[2] + ", " + nowData[3] + ", " + nowData[4] + ", " + nowData[5] + ", " + nowData[6];
            AlertDialog.Builder dialog = new AlertDialog.Builder(scd_search2.this);
            dialog.setTitle("데이터 삭제")
                    .setMessage("해당 데이터를 삭제 하시겠습니까?" + "\n" + viewData)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(scd_search2.this, "데이터를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                            mDbOpenHelper.deleteColumn(nowIndex);
                            showDatabase(sort);
                            setInsertMode();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(scd_search2.this, "삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                            setInsertMode();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    public void showDatabase(String sort){
        Cursor iCursor = mDbOpenHelper.sortColumn(sort);
        Log.d("showDatabase", "DB Size: " + iCursor.getCount());
        arrayData.clear();
        arrayIndex.clear();
        while(iCursor.moveToNext()){

            String tempIndex = iCursor.getString(iCursor.getColumnIndex("_id"));
            String tempID = iCursor.getString(iCursor.getColumnIndex("userid"));
            tempID = setTextLength(tempID,10);
            String tempName = iCursor.getString(iCursor.getColumnIndex("name"));
            tempName = setTextLength(tempName,10);
            String tempAge = iCursor.getString(iCursor.getColumnIndex("age"));
            tempAge = setTextLength(tempAge,10);
            String tempInfo = iCursor.getString(iCursor.getColumnIndex("info"));
            tempInfo = setTextLength(tempInfo,10);
            String tempGender = iCursor.getString(iCursor.getColumnIndex("gender"));
            tempGender = setTextLength(tempGender,10);

            String tempMedicine = iCursor.getString(iCursor.getColumnIndex("medicine"));
            tempMedicine = setTextLength(tempMedicine,10);
            String tempNumber = iCursor.getString(iCursor.getColumnIndex("number"));
            tempNumber = setTextLength(tempNumber,10);

            String Result = tempID + tempName + tempAge + tempGender + "0" +tempNumber  + "     "+ tempMedicine+ "     " + tempInfo;
            arrayData.add(Result);
            arrayIndex.add(tempIndex);
        }
        arrayAdapter.clear();
        arrayAdapter.addAll(arrayData);
        arrayAdapter.notifyDataSetChanged();
    }

    public String setTextLength(String text, int length){
        if(text.length()<length){
            int gap = length - text.length();
            for (int i=0; i<gap; i++){
                text = text + " ";
            }
        }
        return text;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                ID = edit_ID.getText().toString();
                name = edit_Name.getText().toString();
                age = Long.parseLong(edit_Age.getText().toString());
                Info = edit_Info.getText().toString();
                Medicine = edit_Medicine.getText().toString();
                Number = Long.parseLong(edit_Number.getText().toString());

                mDbOpenHelper.open();
                mDbOpenHelper.insertColumn(ID, name, age, gender, Number, Medicine, Info);
                showDatabase(sort);
                setInsertMode();
                edit_ID.requestFocus();
                edit_ID.setCursorVisible(true);
                break;

            case R.id.btn_update:
                ID = edit_ID.getText().toString();
                name = edit_Name.getText().toString();
                age = Long.parseLong(edit_Age.getText().toString());
                Info = edit_Info.getText().toString();
                Medicine = edit_Medicine.getText().toString();
                Number = Long.parseLong(edit_Number.getText().toString());

                mDbOpenHelper.updateColumn(nowIndex, ID, name, age, gender, Number, Medicine, Info);
                showDatabase(sort);
                setInsertMode();
                edit_ID.requestFocus();
                edit_ID.setCursorVisible(true);
                break;

            case R.id.btn_select:
                showDatabase(sort);
                break;

            case R.id.check_man:
                check_Woman.setChecked(false);
                gender = "Man";
                break;

            case R.id.check_woman:
                check_Man.setChecked(false);
                gender = "Woman";
                break;

            case R.id.check_userid:
                check_Name.setChecked(false);
                check_Age.setChecked(false);
                sort = "userid";
                break;

            case R.id.check_name:
                check_ID.setChecked(false);
                check_Age.setChecked(false);
                sort = "name";
                break;

            case R.id.check_age:
                check_ID.setChecked(false);
                check_Name.setChecked(false);
                sort = "age";
                break;
        }
    }

}
