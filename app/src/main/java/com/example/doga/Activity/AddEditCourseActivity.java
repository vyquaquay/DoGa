package com.example.doga.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.doga.Dao.AppDatabase;
import com.example.doga.Model.GiogaCourseModel;
import com.example.doga.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditCourseActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private Spinner dayOfWeekSpinner;
    private Button saveButton;
    private EditText timeEditText;
    private EditText capacityEditText;
    private EditText durationEditText;
    private EditText priceEditText;
    private Spinner typeSpinner;
    private EditText descriptionEditText;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private String[] DayofWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
    private String[] TypeofClass = {"Flow Yoga", "Aeril Yoga", "Family Yoga"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_course);

        // Initialize database and UI elements
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sqlite_example_db").build();
        dayOfWeekSpinner = findViewById(R.id.dayOfWeekSpinner);
        saveButton = findViewById(R.id.saveButton);
        timeEditText = findViewById(R.id.timeEditText);
        capacityEditText = findViewById(R.id.capacityEditText);
        priceEditText = findViewById(R.id.priceEditText);
        typeSpinner = findViewById(R.id.typeSpinner);
        durationEditText = findViewById(R.id.durationEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);

        // Set up spinners
        ArrayAdapter<String> DayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, DayofWeek);
        ArrayAdapter<String> TypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, TypeofClass);
        dayOfWeekSpinner.setAdapter(DayAdapter);
        typeSpinner.setAdapter(TypeAdapter);

        // Set click listener for save button
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                if (intent != null && intent.getExtras() != null) {
                    long courseId = intent.getLongExtra("course_id", -1L);
                    if (courseId != -1L) {
                        // Update existing course
                        executorService.execute(() -> {
                            GiogaCourseModel giogaCourseModel = appDatabase.CourseDao().getCourseById(courseId);
                            giogaCourseModel.dayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
                            giogaCourseModel.timeofCourse = timeEditText.getText().toString();
                            giogaCourseModel.capacity = Integer.parseInt(capacityEditText.getText().toString());
                            giogaCourseModel.price = Double.parseDouble(priceEditText.getText().toString());
                            giogaCourseModel.typeofClass = typeSpinner.getSelectedItem().toString();
                            giogaCourseModel.description = descriptionEditText.getText().toString();
                            giogaCourseModel.duration = Integer.parseInt(durationEditText.getText().toString());
                            appDatabase.CourseDao().updateCourse(giogaCourseModel);

                            // Set result and finish after updating
                            runOnUiThread(() -> {
                                setResult(Activity.RESULT_OK);
                                finish();
                            });
                        });
                    } else {
                        // Add new course
                        onSaveButtonClicked(view);
                    }
                } else {
                    // Add new course (if no intent extras)
                    onSaveButtonClicked(view);
                }
            }
        });
        // Check for existing course ID for editing
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            long courseId = intent.getLongExtra("course_id", -1L);
            if (courseId != -1L) {
                executorService.execute(() -> {
                    GiogaCourseModel giogaCourseModel = appDatabase.CourseDao().getCourseById(courseId);
                    // Update UI with existing course data
                    runOnUiThread(() -> {
                        timeEditText.setText(giogaCourseModel.timeofCourse);
                        capacityEditText.setText(String.valueOf(giogaCourseModel.capacity));
                        durationEditText.setText(String.valueOf(giogaCourseModel.duration));
                        priceEditText.setText(String.valueOf(giogaCourseModel.price));
                        descriptionEditText.setText(giogaCourseModel.description);

                        int dayOfWeekPosition = findPositionInArray(DayofWeek, giogaCourseModel.dayOfWeek);
                        if (dayOfWeekPosition != -1) {
                            dayOfWeekSpinner.setSelection(dayOfWeekPosition);
                        }

                        int typePosition = findPositionInArray(TypeofClass, giogaCourseModel.typeofClass);
                        if (typePosition != -1) {
                            typeSpinner.setSelection(typePosition);
                        }
                    });
                });
            }
        }
    }

    // Method to handle saving a new course
    public void onSaveButtonClicked(View view) {
        GiogaCourseModel newCourse = new GiogaCourseModel();
        newCourse.dayOfWeek = dayOfWeekSpinner.getSelectedItem().toString();
        newCourse.timeofCourse = timeEditText.getText().toString();
        newCourse.capacity = Integer.parseInt(capacityEditText.getText().toString());
        newCourse.price = Double.parseDouble(priceEditText.getText().toString());
        newCourse.typeofClass = typeSpinner.getSelectedItem().toString();
        newCourse.description = descriptionEditText.getText().toString();
        newCourse.duration = Integer.parseInt(durationEditText.getText().toString());

        executorService.execute(() -> {
            appDatabase.CourseDao().insertCourse(newCourse);
        });
        setResult(Activity.RESULT_OK);
        finish();
    }

    // Helper method to find position of an item in an array
    private int findPositionInArray(String[] array, String value) {
        for (int i = 0; i < array.length; i++) {
            if (array[i].equals(value)) {
                return i;
            }
        }
        return -1; // Not found
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }
}