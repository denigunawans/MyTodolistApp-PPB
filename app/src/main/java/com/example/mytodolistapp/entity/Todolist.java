package com.example.mytodolistapp.entity;

import java.sql.Time;

import java.util.Date;

public class Todolist {

    private String todolist, category,date, time, strNotif, child, name;
    private boolean showNotif;
    private String repeat;

    public Todolist(String name, String child, String date, String todolist, String category, String time, boolean showNotif, String repeat) {
        this.todolist = todolist;
        this.category = category;
        this.date = date;
        this.time = time;
        this.showNotif = showNotif;
        this.repeat = repeat;
        this.child = child;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public String getTodolist() {
        return todolist;
    }

    public void setTodolist(String todolist) {
        this.todolist = todolist;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isShowNotif() {
        return showNotif;
    }

    public void setShowNotif(boolean showNotif) {
        this.showNotif = showNotif;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }
}
