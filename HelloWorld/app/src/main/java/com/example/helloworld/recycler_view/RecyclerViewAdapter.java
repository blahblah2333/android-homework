package com.example.helloworld.recycler_view;

import android.view.LayoutInflater;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.view.View;
//import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.helloworld.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private List<Data> myList;

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        //View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_data, viewGroup, false);
        //return new RecyclerViewHolder(root);
        if (viewType == 1) {
            View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_data_, viewGroup, false);
            return new RecyclerViewHolder(root);
        }
        else {
            View root = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_data, viewGroup, false);
            return new RecyclerViewHolder(root);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 3) return 2;
        else return 1;
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((RecyclerViewHolder)viewHolder).bind(myList.get(position));
    }

    @Override public int getItemCount() { return myList.size(); }

    public void setData(List<Data> list) { myList = list; }
}
