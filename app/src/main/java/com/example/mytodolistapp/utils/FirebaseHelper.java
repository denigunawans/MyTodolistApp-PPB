package com.example.mytodolistapp.utils;

import android.app.Application;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper extends Application {
    String username, referenceName, todoChild, todoId;
    DatabaseReference todolistDb;
    int id = 1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public DatabaseReference getReference(){
        todolistDb = FirebaseDatabase.getInstance().getReference();
        return todolistDb;
    }

    public DatabaseReference getReferenceWithName(String referenceName){
        this.referenceName = referenceName;
        todolistDb = FirebaseDatabase.getInstance().getReference(referenceName);
        return todolistDb;
    }

    public DatabaseReference getReferenceChild(String referenceName, String todoChild){
        this.referenceName = referenceName;
        this.todoChild = todoChild;

        todolistDb = FirebaseDatabase.getInstance().getReference(referenceName).child(todoChild);
        return todolistDb;
    }

    public DatabaseReference getReferenceChildTodo(String referenceName, String todoChild, String todoId){
        this.referenceName = referenceName;
        this.todoChild = todoChild;
        this.todoId = todoId;

        todolistDb = FirebaseDatabase.getInstance().getReference(referenceName).child(todoChild).child(todoId);
        return todolistDb;
    }
}
