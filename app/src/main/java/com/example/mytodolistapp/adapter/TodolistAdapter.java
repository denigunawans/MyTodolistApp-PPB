package com.example.mytodolistapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytodolistapp.R;
import com.example.mytodolistapp.entity.Todolist;
import com.example.mytodolistapp.service.MainActivity;
import com.example.mytodolistapp.service.TodolistUpdateActivity;
import com.example.mytodolistapp.utils.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class TodolistAdapter extends RecyclerView.Adapter<TodolistAdapter.ViewHolder> {

    private ArrayList<Todolist> todolistArrayList;

    public TodolistAdapter(ArrayList<Todolist> todolistArrayList) {
        this.todolistArrayList = todolistArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.todolist_item, parent, false));

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String posisi = String.valueOf(position+1);
        Todolist todolist = todolistArrayList.get(position);
        holder.name.setText(todolist.getName());
        holder.child.setText(todolist.getChild());
        holder.date.setText(todolist.getDate().toString());
        holder.todolist.setText(todolist.getTodolist());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Context context = holder.itemView.getContext();
                    Intent intent = new Intent(context, TodolistUpdateActivity.class);

                    intent.putExtra("keyTodo", todolist.getTodolist());
                    intent.putExtra("keyDate", todolist.getDate());
                    intent.putExtra("keyTime", todolist.getTime());
                    intent.putExtra("keyCategory", todolist.getCategory());
                    intent.putExtra("keyRepeatDay", todolist.getRepeat());
                    intent.putExtra("keyPosition", String.valueOf(posisi));
                    intent.putExtra("keyChild", todolist.getChild());

                    context.startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(view.getContext(), "gagal intent",Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todolistArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView date, todolist, child, name;
        Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            int p = getLayoutPosition();

            date = itemView.findViewById(R.id.txDateTodo);
            todolist = itemView.findViewById(R.id.txTodo);
            child = itemView.findViewById(R.id.txChild);
            btnDelete = itemView.findViewById(R.id.btnDeleteTodo);
            name = itemView.findViewById(R.id.txNama);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //delete todolist
                    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(name.getText().toString()).child("todolist").child(child.getText().toString());
                    dbRef.removeValue();
                    System.out.println("sukses menghapus");
                }
            });
        }
    }
}
