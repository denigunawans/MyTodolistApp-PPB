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

public class AddTodolist extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    DatabaseReference todolistDb;

    private TextView todoText, dateText, timeText, selectDay, category;
    int hour, minute, id=1;
    boolean[] selectedDay;
    private ImageButton btnsave;
    ArrayList<Integer> dayList = new ArrayList<>();
    String[] dayArray = {"Senin", "Selasa", "Rabu"
            ,"Kamis", "Jumat", "Sabtu", "Minggu"};
    CheckBox checkBox;

    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addtodo_layout);

        firebaseHelper = (FirebaseHelper) getApplication();
        todolistDb = firebaseHelper.getReferenceChild(firebaseHelper.getUsername(), "todolist");
        todolistDb.keepSynced(true);

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

        todoText = findViewById(R.id.edtTodo);
        dateText = findViewById(R.id.edtDate);
        timeText = findViewById(R.id.edtTime);
        selectDay = findViewById(R.id.edtSelectDay);
        category = findViewById(R.id.edtCategory);
        checkBox = findViewById(R.id.checkNotif);

        btnsave = findViewById(R.id.btnAdd);

        findViewById(R.id.btnCalendar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarDialog();
            }
        });
        findViewById(R.id.btnTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog();
            }
        });

        selectedDay = new boolean[dayArray.length];
        selectDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRepeatDialog();
            }
        });
    }

    private void showRepeatDialog(){
        //initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(AddTodolist.this);
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
                    selectDay.setText("Setiap hari");
                }else {
                    selectDay.setText("Setiap hari " + stringBuilder.toString());
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
                    selectDay.setText("");
                }
            }
        });
        builder.show();
    }

    public void saveTodo(View v){
        try {
            String todolist = todoText.getText().toString();
            String date = dateText.getText().toString();
            String kategori = category.getText().toString();
            String time = timeText.getText().toString();
            boolean showNotif;
            String repeatDay = selectDay.getText().toString();

            if (checkBox.isChecked()){
                showNotif = true;
            }else{
                showNotif = false;
            }

            todolistDb.child("todo"+id).setValue(new Todolist(firebaseHelper.getUsername(), "todo"+id, date, todolist, kategori, time, showNotif, repeatDay));

            try {
                Intent intent = new Intent(AddTodolist.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }catch (Exception e){
                Toast.makeText(AddTodolist.this, "gagal intent"+e, Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(AddTodolist.this, "gagal menambah todo"+e, Toast.LENGTH_SHORT).show();
        }
    }

    private void showTimeDialog(){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                timeText.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
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
            dateText.setText(dayName +"," + day + "/" + month+1 + "/" + year);
        }catch (Exception e){
            System.out.println("gagalll" +e);
        }
    }
}