package com.example.mytodolistapp.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mytodolistapp.R;
import com.example.mytodolistapp.entity.User;
import com.example.mytodolistapp.utils.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText edtusername, edtPassword;

    DatabaseReference todolistDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtusername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);

        FirebaseHelper firebaseHelper = (FirebaseHelper) getApplication();

        todolistDb = firebaseHelper.getReference();
        todolistDb.keepSynced(true);
    }

    public void login(View v){
        try {
            todolistDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nameFromInput = edtusername.getText().toString();
                    String passFromInput = edtPassword.getText().toString();
                    if (TextUtils.isEmpty(edtusername.getText().toString())){
                        Toast.makeText(LoginActivity.this, "username harus diisi", Toast.LENGTH_SHORT).show();
                    }else{
                        if (snapshot.hasChild(nameFromInput)){
                            for (DataSnapshot data : snapshot.getChildren()){
                                String nama = data.getKey();
                                String pass = data.child("account").child("password").getValue().toString();
                                if (nama.equals(nameFromInput)){
                                    if (pass.equals(passFromInput)){
                                        FirebaseHelper firebaseHelper = (FirebaseHelper) getApplication();
                                        firebaseHelper.setUsername(edtusername.getText().toString());

                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else {
                                        Toast.makeText(LoginActivity.this, "password salah", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }else {
                            Toast.makeText(LoginActivity.this, "username tidak tersedia", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){
            Toast.makeText(LoginActivity.this, "gagal, "+e, Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View v){
        try {
            todolistDb.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(edtusername.getText().toString())){
                        Toast.makeText(LoginActivity.this, "gagal, username sudah ada", Toast.LENGTH_SHORT).show();
                    }else{
                        todolistDb = FirebaseDatabase.getInstance().getReference(edtusername.getText().toString());
                        String username = edtusername.getText().toString();
                        String password = edtPassword.getText().toString();

                        todolistDb.child("account").setValue(new User(username, password));

                        Toast.makeText(LoginActivity.this, "berhasil, silahkan login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    throw error.toException();
                }
            });

        }catch (Exception e){
            Toast.makeText(LoginActivity.this, "gagal, "+e, Toast.LENGTH_SHORT).show();
        }

    }
}