package com.example.doga.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.doga.Dao.AppDatabase;
import com.example.doga.Model.GiogaCourseModel;
import com.example.doga.R;
import com.example.doga.adapter.GiogaCourseAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private GiogaCourseAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appDatabase = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "sqlite_example_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();

        recyclerView = findViewById(R.id.yogaCourseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Button NewCourseButton = findViewById(R.id.createButton);
        NewCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,  AddEditCourseActivity.class);
                startActivity(intent);
            }
        });
        List<GiogaCourseModel> Courses = appDatabase.CourseDao().getAllCourse();

        adapter = new GiogaCourseAdapter(Courses, MainActivity.this);
        recyclerView.setAdapter(adapter);

    }
}