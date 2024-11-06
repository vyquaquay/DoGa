package com.example.doga.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.doga.Dao.AppDatabase;
import com.example.doga.Model.GiogaClassModel;
import com.example.doga.Model.GiogaCourseModel;
import com.example.doga.R;
import com.example.doga.adapter.GiogaClassAdapter;
import com.example.doga.adapter.GiogaCourseAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class ClassActivity extends AppCompatActivity {

    private AppDatabase appDatabase;
    private RecyclerView recyclerView;
    private GiogaClassAdapter adapter;
    private static final int ADD_COURSE_REQUEST_CODE = 1;
    private CheckBox selectAllCheckBox;
    private ImageView deleteIcon;
    private boolean isSelectAll = false;
    private List<GiogaClassModel> selectedClass = new ArrayList<>();
    private ExecutorService executorService;
    private SearchView searchView;
    private List<GiogaClassModel> giogaClassModels;
    private long courseId = -1L;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_activity);
        executorService = Executors.newSingleThreadExecutor();
        searchView = findViewById(R.id.searchBar);
        appDatabase = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "sqlite_example_db")
                .allowMainThreadQueries() // For simplicity, don't use this in production
                .build();
        recyclerView = findViewById(R.id.yogaClassRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectAllCheckBox = findViewById(R.id.selectAllCheckBox);
        deleteIcon = findViewById(R.id.delButton);
        Button NewClassButton = findViewById(R.id.createButton);
        giogaClassModels = appDatabase.ClassDao().getAllClasses();

        Intent intent = getIntent();
        courseId = intent.getLongExtra("course_id", -1L);
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
        NewClassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClassActivity.this, AddEditClassActivity.class);
                intent.putExtra("course_id", courseId);
                startActivityForResult(intent, ADD_COURSE_REQUEST_CODE); // Use startActivityForResult
            }
        });
        List<GiogaClassModel> initialClasss = appDatabase.ClassDao().getAllClasses();
        adapter = new GiogaClassAdapter(initialClasss, ClassActivity.this);
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
                filterClassList(newText);
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
       List<GiogaClassModel> giogaClassModels = appDatabase.ClassDao().getClassBycourseId(courseId);
        adapter = new GiogaClassAdapter(giogaClassModels, ClassActivity.this); // Recreate adapter
        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshCourseList();
    }
    private void deleteSelectedCourses() {
        // Get selected courses from adapter
        List<GiogaClassModel> selectedClass = adapter.getSelectedClasses();

// Delete the selected classes
        for (GiogaClassModel classModel : selectedClass) {
            appDatabase.ClassDao().deleteClasse(classModel);
        }// Use the DAO's delete method

            // Refresh course list on the main thread
            runOnUiThread(() -> {
                refreshCourseList();
            });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
    private boolean matchesSearchCriteria(GiogaClassModel classe, String query) {
        return String.valueOf(classe.date).contains(query) ||
                classe.teacher.toLowerCase().contains(query.toLowerCase()) ||
                classe.comment.toLowerCase().contains(query.toLowerCase());
    }

    private void filterClassList(String query) {
        List<GiogaClassModel> filteredList = giogaClassModels.stream()
                .filter(classe -> matchesSearchCriteria(classe, query))
                .collect(Collectors.toList());

        adapter.updateData(filteredList); // Update the adapter with the filtered data
    }
}