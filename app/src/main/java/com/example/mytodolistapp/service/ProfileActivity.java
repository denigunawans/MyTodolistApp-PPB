package com.example.mytodolistapp.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mytodolistapp.R;
import com.example.mytodolistapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    TextView username, totalTodo;
    int banyakTodo;

    DatabaseReference todolistDb;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseHelper = (FirebaseHelper) getApplication();

        username = findViewById(R.id.txName);
        totalTodo = findViewById(R.id.txBanyakTodo);

        setData();
    }

    private void setData(){
        todolistDb = firebaseHelper.getReferenceChild(firebaseHelper.getUsername(), "todolist");
        todolistDb.keepSynced(true);
        todolistDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int size = (int) snapshot.getChildrenCount();
                System.out.println(String.valueOf(size));
                banyakTodo = size;
                totalTodo.setText("total to"+String.valueOf(banyakTodo));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        username.setText(firebaseHelper.getUsername());
    }

    //delete account
    public void deleteAccount(View v){
        DatabaseReference dbTemp = firebaseHelper.getReferenceWithName(firebaseHelper.getUsername());
        dbTemp.removeValue();

        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}