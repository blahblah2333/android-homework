package com.example.helloworld;

import android.os.Bundle;
import android.widget.Adapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.Nullable;

import com.example.helloworld.recycler_view.Data;
import com.example.helloworld.recycler_view.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView myRecyclerView;
    private RecyclerViewAdapter myRecyclerViewAdapter;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        myRecyclerView = findViewById(R.id.rv_list);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerViewAdapter = new RecyclerViewAdapter();
        myRecyclerView.setAdapter(myRecyclerViewAdapter);
        List<Data> list = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Data data = new Data(i + ".", "咕咕咕" + " " + i, 100000 - i);
            list.add(data);
        }
        myRecyclerViewAdapter.setData(list);
        myRecyclerViewAdapter.notifyDataSetChanged();
    }
}
