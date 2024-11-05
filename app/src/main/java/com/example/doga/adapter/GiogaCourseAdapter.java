package com.example.doga.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doga.Activity.AddEditCourseActivity;
import com.example.doga.Model.GiogaCourseModel;
import com.example.doga.R;

import java.util.ArrayList;
import java.util.List;

public class GiogaCourseAdapter extends RecyclerView.Adapter<GiogaCourseAdapter.GiogaViewHolder> {
    private List<GiogaCourseModel> giogaModels;
    private final AppCompatActivity activity;
    private boolean isSelectAll = false; // Flag to track select all state
    private List<GiogaCourseModel> selectedCourses = new ArrayList<>(); // List to store selected courses

    public GiogaCourseAdapter(List<GiogaCourseModel> GIogaModels, AppCompatActivity activity) {
        this.giogaModels = GIogaModels;
        this.activity = activity;
    }

    @NonNull
    @Override
    public GiogaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.course_layout, parent, false);
        return new GiogaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GiogaViewHolder holder, int position) {
        GiogaCourseModel giogaModel = giogaModels.get(position);
        Log.d("GiogaCourseAdapter", "Binding data for position: " + position + ": " + giogaModel.toString());
        holder.TypeOfClass.setText(giogaModel.typeofClass);
        holder.TimeofCouse.setText(giogaModel.timeofCourse);
        holder.DayofWeek.setText(giogaModel.dayOfWeek);
        holder.Capacity.setText(String.valueOf(giogaModel.capacity));
        holder.Duration.setText(String.valueOf(giogaModel.duration));
        holder.PriceofClass.setText(String.valueOf(giogaModel.price));
        holder.Description.setText(giogaModel.description);

        // Handle item click to open AddEditCourseActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddEditCourseActivity.class);
                intent.putExtra("course_id", giogaModel.Id);
                activity.startActivity(intent);
            }
        });

        // Handle long-press to select the item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.isSelected = !holder.isSelected;
                if (holder.isSelected) {
                    selectedCourses.add(giogaModel);
                } else {
                    selectedCourses.remove(giogaModel);
                }
                notifyItemChanged(position); // Notify adapter of selection change
                return true;
            }
        });

        // Update UI to reflect selection state
        holder.checkBox.setChecked(holder.isSelected);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.isSelected = isChecked;
                if (isChecked) {
                    selectedCourses.add(giogaModel);
                } else {
                    selectedCourses.remove(giogaModel);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return giogaModels.size();
    }

    // Method to select/deselect all items
    public void selectAll(boolean selectAll) {
        isSelectAll = selectAll;
        selectedCourses.clear(); // Clear previous selections
        if (selectAll) {
            selectedCourses.addAll(giogaModels); // Add all items if selectAll is true
        }
        notifyDataSetChanged(); // Notify adapter of changes
    }

    // Method to get the list of selected courses
    public List<GiogaCourseModel> getSelectedCourses() {
        return selectedCourses;
    }

    public static class GiogaViewHolder extends RecyclerView.ViewHolder {
        TextView TypeOfClass, TimeofCouse, DayofWeek, Capacity, Duration, PriceofClass, Description;
        CheckBox checkBox; // Add a CheckBox for selection
        public boolean isSelected = false; // Add selection state

        public GiogaViewHolder(@NonNull View itemView) {
            super(itemView);
            TypeOfClass = itemView.findViewById(R.id.TypeOfClass);
            TimeofCouse = itemView.findViewById(R.id.TimeofCouse);
            DayofWeek = itemView.findViewById(R.id.DayofWeek);
            Capacity = itemView.findViewById(R.id.Capacity);
            Duration = itemView.findViewById(R.id.Duration);
            PriceofClass = itemView.findViewById(R.id.PriceofClass);
            Description = itemView.findViewById(R.id.Description);
            checkBox = itemView.findViewById(R.id.checkBox); // Initialize the CheckBox
        }
    }
    public void updateData(List<GiogaCourseModel> filteredList) {
        this.giogaModels = filteredList; // Update the adapter's data
        notifyDataSetChanged(); // Notify the adapter of the changes
    }
}