package com.example.doga.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doga.Activity.AddEditClassActivity;
import com.example.doga.Model.GiogaClassModel;
import com.example.doga.R;

import java.util.ArrayList;
import java.util.List;

public class GiogaClassAdapter extends RecyclerView.Adapter<GiogaClassAdapter.GiogaViewHolder> {
    private List<GiogaClassModel> giogaClassModels;
    private final AppCompatActivity activity;
    private boolean isSelectAll = false;
    private List<GiogaClassModel> selectedClasses = new ArrayList<>();

    public GiogaClassAdapter(List<GiogaClassModel> giogaClassModels, AppCompatActivity activity) {
        this.giogaClassModels = giogaClassModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GiogaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_layout, parent, false);
        return new GiogaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GiogaViewHolder holder, int position) {
        GiogaClassModel giogaClassModel = giogaClassModels.get(position);
        Log.d("GiogaClassAdapter", "Binding data for position: " + position + ": " + giogaClassModel.toString());

        // Check for null before setting text
            holder.dateTextView.setText(giogaClassModel.date);
            holder.teacherTextView.setText(giogaClassModel.teacher);
            holder.commentTextView.setText(giogaClassModel.comment);

        holder.bind(giogaClassModel, position); // Pass data to ViewHolder

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(activity, AddEditClassActivity.class);
            intent.putExtra("class_id", giogaClassModel.id);
            intent.putExtra("course_id", giogaClassModel.courseId); // Pass course ID")
            activity.startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(v -> {
            holder.isSelected = !holder.isSelected;
            if (holder.isSelected) {
                selectedClasses.add(giogaClassModel);
            } else {
                selectedClasses.remove(giogaClassModel);
            }
            notifyItemChanged(position);
            return true;
        });
        holder.checkBox.setChecked(holder.isSelected);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            holder.isSelected = isChecked;
            if (isChecked) {
                selectedClasses.add(giogaClassModel);
            } else {
                selectedClasses.remove(giogaClassModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return giogaClassModels.size();
    }

    public void selectAll(boolean selectAll) {
        isSelectAll = selectAll;
        selectedClasses.clear();
        if (selectAll) {
            selectedClasses.addAll(giogaClassModels);
        }
        notifyDataSetChanged();
    }

    public List<GiogaClassModel> getSelectedClasses() {
        return selectedClasses;
    }

    public void updateData(List<GiogaClassModel> filteredList) {
        this.giogaClassModels = filteredList;
        notifyDataSetChanged();
    }

    public static class GiogaViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, teacherTextView, commentTextView;
        CheckBox checkBox;
        public boolean isSelected = false;
        Button viewButton;
        private GiogaClassModel giogaClassModel; // Store the data
        private int position; // Store the position

        public GiogaViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.Date);
            teacherTextView = itemView.findViewById(R.id.Teacher);
            commentTextView = itemView.findViewById(R.id.Comment);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(GiogaClassModel giogaClassModel, int position) {
            this.giogaClassModel = giogaClassModel;
            this.position = position;
            // ... (Set other views using giogaClassModel data)
        }
    }
}