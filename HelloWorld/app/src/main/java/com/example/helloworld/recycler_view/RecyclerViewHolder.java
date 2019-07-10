package com.example.helloworld.recycler_view;

import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.example.helloworld.R;

public class RecyclerViewHolder extends RecyclerView.ViewHolder {

    private TextView myTextView;
    private TextView title;
    private TextView hotValue;

    public RecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        myTextView = itemView.findViewById(R.id.tv_data1);
        title = itemView.findViewById(R.id.tv_data2);
        hotValue = itemView.findViewById(R.id.tv_data3);
    }

    public void bind(Data data) {
        myTextView.setText(data.getInfo());
        title.setText(data.getTitle());
        hotValue.setText(data.getHotValue() + "");
    }
}
