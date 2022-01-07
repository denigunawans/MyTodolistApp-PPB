package com.example.mytodolistapp.service;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytodolistapp.R;
import com.example.mytodolistapp.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;

public class DeleteTodo extends AppCompatActivity {

    Button btnDelete;
    CheckBox check;

    TextView childName;

    String child;

    DatabaseReference dbRef;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todolist_item);

        firebaseHelper = (FirebaseHelper) getApplication();

        btnDelete = findViewById(R.id.btnDeleteTodo);
        check = findViewById(R.id.checkTodo);
        childName = findViewById(R.id.txChild);
        child = childName.getText().toString();
    }
}
