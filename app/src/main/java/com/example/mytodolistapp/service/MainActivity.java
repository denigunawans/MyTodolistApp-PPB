package com.example.mytodolistapp.service;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.mytodolistapp.R;
import com.example.mytodolistapp.adapter.TodolistAdapter;
import com.example.mytodolistapp.entity.Todolist;
import com.example.mytodolistapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recTodo;
    private ArrayList<Todolist> arrayTodo;
    private ArrayList<Todolist> todoTemp;
    private TextView usernameTodo;

    DatabaseReference todolistDb;
    FirebaseHelper firebaseHelper;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recTodo = findViewById(R.id.rec_todo);
        usernameTodo = findViewById(R.id.txUsername);
        arrayTodo = new ArrayList<>();
        firebaseHelper= (FirebaseHelper) getApplication();
        todolistDb = firebaseHelper.getReferenceChild(firebaseHelper.getUsername(), "todolist");
        todolistDb.keepSynced(true);

        usernameTodo.setText("Haloo, "+firebaseHelper.getUsername());

        // add todolist
        ImageButton add = findViewById(R.id.btnAdd);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddTodolist.class));
            }
        });

        // get todo show on recycler view
        todolistDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoTemp = new ArrayList<>();
                boolean showNotif = false;
                for (DataSnapshot data : snapshot.getChildren()){
                    String nama = data.child("name").getValue().toString();
                    String child = data.child("child").getValue().toString();
                    String date = data.child("date").getValue().toString();
                    String todo = data.child("todolist").getValue().toString();
                    String category = data.child("category").getValue().toString();
                    String time = data.child("time").getValue().toString();
                    String repeat = data.child("repeat").getValue().toString();
                    String notif = data.child("showNotif").getValue().toString();
                    if (notif.equals("true")){
                        showNotif = true;
                    }

                    arrayTodo.add(new Todolist(firebaseHelper.getUsername(), child, date,todo ,category,time,showNotif,repeat));
                }
                System.out.println("todo"+arrayTodo.size());
                recTodo.setAdapter(new TodolistAdapter(arrayTodo));
                recTodo.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // intent to profile
    public void gotoProfil(View v){
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

}