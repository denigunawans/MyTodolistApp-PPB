package com.example.mytodolistapp.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.mytodolistapp.R;
import com.example.mytodolistapp.entity.Todolist;
import com.example.mytodolistapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class TodolistUpdateActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    TextView edtTodo, edtDate, edtTime, edtRepeatDay, edtCategory;
    CheckBox notif;
    ImageButton btnUpdate;
    String childName;

    int hour, minute, id=1;
    boolean[] selectedDay;
    ArrayList<Integer> dayList = new ArrayList<>();
    String[] dayArray = {"Senin", "Selasa", "Rabu"
            ,"Kamis", "Jumat", "Sabtu", "Minggu"};

    DatabaseReference todolistDb;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist_update);
        setData();

        firebaseHelper = (FirebaseHelper) getApplication();
        todolistDb = firebaseHelper.getReferenceChild(firebaseHelper.getUsername(), "todolist");
        todolistDb.keepSynced(true);

        //btn update
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String todolist = edtTodo.getText().toString();
                String tanggal = edtDate.getText().toString();
                String kategori = edtCategory.getText().toString();
                String jam = edtTime.getText().toString();
                boolean tampilNotif = false;
                String repeatDay = edtRepeatDay.getText().toString();

                if (notif.isChecked()){
                    tampilNotif = true;
                }
                try {
                    updateData(firebaseHelper.getUsername(),"todo"+childName, tanggal, todolist, kategori, jam, tampilNotif, repeatDay);
                    startActivity(new Intent(TodolistUpdateActivity.this, MainActivity.class));
                }catch (Exception e){
                    Toast.makeText(TodolistUpdateActivity.this, "gagal update", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //set id
        todolistDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data: snapshot.getChildren()){
                    id++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //show calendar dialog
        findViewById(R.id.btnCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarDialog();
            }
        });

        // show time dialog
        findViewById(R.id.btnTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

        //set repeat
        selectedDay = new boolean[dayArray.length];
        edtRepeatDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRepeatDialog();
            }
        });
    }

    private void setData(){
        edtTodo = findViewById(R.id.edtTodo);
        edtDate = findViewById(R.id.edtDate);
        edtTime = findViewById(R.id.edtTime);
        edtRepeatDay = findViewById(R.id.edtSelectDay);
        edtCategory = findViewById(R.id.edtCategory);
        notif = findViewById(R.id.checkNotif);
        btnUpdate = findViewById(R.id.btnUpdate);

        String todolist,date,kategori,time,strNotif,repeatDay;
        Bundle eks = getIntent().getExtras();

        todolist = eks.getString("keyTodo", "");
        date = eks.getString("keyDate", "");
        time = eks.getString("keyTime", "");
        kategori = eks.getString("keyCategory", "");
        repeatDay = eks.getString("keyRepeatDay", "");
        childName = eks.getString("keyPosition", "");

        try {
            edtTodo.setText(todolist);
            edtDate.setText(date);
            edtTime.setText(time);
            edtRepeatDay.setText(repeatDay);
            edtCategory.setText(kategori);
        }catch (Exception e){
            Toast.makeText(this, "gagal set", Toast.LENGTH_SHORT);
        }
    }

    private void showRepeatDialog(){
        //initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(TodolistUpdateActivity.this);
        //set tittle
        builder.setTitle("Select Day");
        //set dialog non calncellable
        builder.setCancelable(false);

        builder.setMultiChoiceItems(dayArray, selectedDay, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                if (b){
                    //when checkbox selected, add position in day list
                    dayList.add(i);
                    //sort day list
                    Collections.sort(dayList);
                }else{
                    //when checkboc unselected, remove position from day list
                    dayList.remove(i);
                }
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < dayList.size(); j++){
                    stringBuilder.append(dayArray[dayList.get(j)]);
                    if (j != dayList.size()-1){
                        stringBuilder.append(", ");
                    }
                }
                if (dayList.size() == 7){
                    edtRepeatDay.setText("Setiap hari");
                }else {
                    edtRepeatDay.setText("Setiap hari " + stringBuilder.toString());
                }
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNeutralButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (int j = 0; j < selectedDay.length; j++){
                    //remove selection
                    selectedDay[j] = false;
                    // clear day list
                    dayList.clear();
                    //clear text
                    edtRepeatDay.setText("");
                }
            }
        });
        builder.show();
    }

    private void showTimeDialog(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                edtTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,style, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void showCalendarDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = format.parse(day+"-"+month+"-"+year);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE");
            String dayName = simpleDateFormat.format(date);
            edtDate.setText(dayName +"," + day + "/" + month+1 + "/" + year);
        }catch (Exception e){
            System.out.println("gagalll" +e);
        }
    }

    // update data
    public void updateData(String name, String child,String todo, String date, String time, String category, boolean showNotif, String repeat){
        DatabaseReference dbRef = firebaseHelper.getReferenceChildTodo(firebaseHelper.getUsername(), "todolist", "todo"+childName);

        dbRef.setValue(new Todolist(name, child,todo, date, time, category, showNotif, repeat));
    }
}






















