package com.example.doga.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.doga.Dao.AppDatabase;
import com.example.doga.Model.GiogaCourseModel;
import com.example.doga.R;
import com.example.doga.adapter.GiogaCourseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private GiogaCourseAdapter adapter;
    private static final int ADD_COURSE_REQUEST_CODE = 1;
    private CheckBox selectAllCheckBox;
    private ImageView deleteIcon;
    private boolean isSelectAll = false;
    private List<GiogaCourseModel> selectedCourses = new ArrayList<>();
    private ExecutorService executorService;
    private SearchView searchView;
    private List<GiogaCourseModel> giogaCourseModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        executorService = Executors.newSingleThreadExecutor();
        searchView = findViewById(R.id.searchBar);
        appDatabase = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "sqlite_example_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
        recyclerView = findViewById(R.id.yogaCourseRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectAllCheckBox = findViewById(R.id.selectAllCheckBox);
        deleteIcon = findViewById(R.id.delButton);
        Button NewCourseButton = findViewById(R.id.createButton);
        giogaCourseModels = appDatabase.CourseDao().getAllCourse();
        selectAllCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelectAll = selectAllCheckBox.isChecked();
                // Update adapter to select/deselect all items
                adapter.selectAll(isSelectAll);
            }
        });
        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete selected courses
                deleteSelectedCourses();
            }
        });
        NewCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddEditCourseActivity.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST_CODE); // Use startActivityForResult
            }
        });
        List<GiogaCourseModel> initialCourses = appDatabase.CourseDao().getAllCourse();
        adapter = new GiogaCourseAdapter(initialCourses, MainActivity.this);
        recyclerView.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission (if needed)
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter course list based on newText
                filterCourseList(newText);
                return true;
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COURSE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            refreshCourseList();
        }
    }

    private void refreshCourseList() {
        giogaCourseModels = appDatabase.CourseDao().getAllCourse();
        adapter = new GiogaCourseAdapter(giogaCourseModels, MainActivity.this); // Recreate adapter
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshCourseList();
    }
    private void deleteSelectedCourses() {
        // Get selected courses from adapter
        selectedCourses = adapter.getSelectedCourses();

        // Delete from database
        executorService.execute(() -> {
            appDatabase.CourseDao().deleteCourses(selectedCourses); // Use the DAO's delete method

            // Refresh course list on the main thread
            runOnUiThread(() -> {
                refreshCourseList();
            });
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
    private boolean matchesSearchCriteria(GiogaCourseModel course, String query) {
        return String.valueOf(course.price).contains(query) ||
                String.valueOf(course.capacity).contains(query) ||
                course.dayOfWeek.toLowerCase().contains(query.toLowerCase()) ||
                course.typeofClass.toLowerCase().contains(query.toLowerCase())
                || course.description.toLowerCase().contains(query.toLowerCase());
    }

    private void filterCourseList(String query) {
        List<GiogaCourseModel> filteredList = giogaCourseModels.stream()
                .filter(course -> matchesSearchCriteria(course, query))
                .collect(Collectors.toList());

        adapter.updateData(filteredList); // Update the adapter with the filtered data
    }
}