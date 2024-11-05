
package com.example.doga.adapter;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doga.Activity.AddEditCourseActivity;
import com.example.doga.Activity.MainActivity;
import com.example.doga.Model.GiogaCourseModel;
import com.example.doga.R;

import java.util.List;

// ContactAdapter.java
public class GiogaCourseAdapter extends RecyclerView.Adapter<GiogaCourseAdapter.GiogaViewHolder> {
    private List<GiogaCourseModel> giogaModels;
    private final AppCompatActivity activity;

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
        Log.d("GiogaCourseAdapter", "Binding data for position: " + position + ": "+ giogaModel.toString());
        holder.TypeOfClass.setText(giogaModel.typeofClass);
        holder.TimeofCouse.setText(giogaModel.timeofCourse);
        holder.DayofWeek.setText(giogaModel.dayOfWeek);
        holder.Capacity.setText(String.valueOf(giogaModel.capacity));
        holder.Duration.setText(String.valueOf(giogaModel.duration));
        holder.PriceofClass.setText(String.valueOf(giogaModel.price));
        holder.Description.setText(giogaModel.description);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(activity, AddEditCourseActivity.class);
                intent.putExtra("course_id", giogaModel.Id);
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return giogaModels.size();
    }

    public static class GiogaViewHolder extends RecyclerView.ViewHolder {
        TextView TypeOfClass, TimeofCouse, DayofWeek, Capacity, Duration, PriceofClass, Description;;

        public GiogaViewHolder(@NonNull View itemView) {
            super(itemView);
            TypeOfClass = itemView.findViewById(R.id.TypeOfClass);
            TimeofCouse = itemView.findViewById(R.id.TimeofCouse);
            DayofWeek = itemView.findViewById(R.id.DayofWeek);
            Capacity = itemView.findViewById(R.id.Capacity);
            Duration = itemView.findViewById(R.id.Duration);
            PriceofClass = itemView.findViewById(R.id.PriceofClass);
            Description = itemView.findViewById(R.id.Description);
        }
    }

}
