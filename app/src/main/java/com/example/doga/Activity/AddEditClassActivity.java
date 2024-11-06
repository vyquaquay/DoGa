package com.example.doga.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.room.Room;

import com.example.doga.Dao.AppDatabase;
import com.example.doga.Model.GiogaClassModel;
import com.example.doga.Model.GiogaCourseModel;
import com.example.doga.R;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddEditClassActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private Button saveButton;
    private TextView dateEditText;
    private EditText teacherEditText;
    private EditText commentEditText;
    long courseId = -1L;
    private DayOfWeek courseDayOfWeek;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private GiogaCourseModel giogaCourseModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_class); // Assuming this is your layout for editing classes

        // Initialize database and UI elements
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "sqlite_example_db").build();
        saveButton = findViewById(R.id.saveButton);
        dateEditText = findViewById(R.id.dateEditText);
        teacherEditText = findViewById(R.id.teacherEditText);
        commentEditText = findViewById(R.id.commentEditText);
        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            long classId = intent.getLongExtra("class_id", -1L);
            courseId = intent.getLongExtra("course_id", -1L);
            executorService.execute(() ->{
                giogaCourseModel = appDatabase.CourseDao().getCourseById(courseId);
            });
            runOnUiThread(() -> {
                dateEditText.setOnClickListener(view -> {
                    DatePickerFragment newFragment = new DatePickerFragment();
                    newFragment.setGiogaCourseModel(giogaCourseModel);
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                });
            });
        // Set click listener for save button
        saveButton.setOnClickListener(view -> {

                if (classId != -1L) {
                    // Update existing class
                    executorService.execute(() -> {
                        GiogaClassModel giogaClassModel = appDatabase.ClassDao().getClassById(classId);
                        giogaClassModel.date = dateEditText.getText().toString();
                        giogaClassModel.teacher = teacherEditText.getText().toString();
                        giogaClassModel.comment = commentEditText.getText().toString();
                        appDatabase.ClassDao().updateClass(giogaClassModel);

                        // Set result and finish after updating
                        runOnUiThread(() -> {
                            setResult(Activity.RESULT_OK);
                            finish();
                        });
                    });

                } else {
                    // Add new class
                    onSaveButtonClicked(view);
                }

        });
    }
        // Check for existing class ID for editing
         intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            long classId = intent.getLongExtra("class_id", -1L);
            if (classId != -1L) {
                executorService.execute(() -> {
                    GiogaClassModel giogaClassModel = appDatabase.ClassDao().getClassById(classId);
                    // Update UI with existing class data
                    runOnUiThread(() -> {
                        dateEditText.setText(giogaClassModel.date);
                        teacherEditText.setText(giogaClassModel.teacher);
                        commentEditText.setText(giogaClassModel.comment);
                    });
                });
            }
        }
    }

    // Method to handle saving a new class
    public void onSaveButtonClicked(View view) {
        GiogaClassModel newClass = new GiogaClassModel(courseId, dateEditText.getText().toString(), teacherEditText.getText().toString(), commentEditText.getText().toString()); // Assuming courseId is 0 for new classes

        executorService.execute(() -> {

            appDatabase.ClassDao().insertClass(newClass);
        });
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executorService.shutdown();
    }

    // DatePicker Fragment inside MainActivity
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        private GiogaCourseModel giogaCourseModel;
        public void setGiogaCourseModel(GiogaCourseModel giogaCourseModel) {
            this.giogaCourseModel = giogaCourseModel;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
        {
            LocalDate d = LocalDate.now();
            int year = d.getYear();
            int month = d.getMonthValue();
            int day = d.getDayOfMonth();
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, --month, day);
            if (giogaCourseModel != null) {
                DayOfWeek courseDayOfWeek = DayOfWeek.valueOf(giogaCourseModel.dayOfWeek.toUpperCase()); // Get course day

                datePickerDialog.getDatePicker().setOnDateChangedListener((view, year1, monthOfYear, dayOfMonth) -> {
                    LocalDate selectedDate = LocalDate.of(year1, monthOfYear + 1, dayOfMonth);
                    if (selectedDate.getDayOfWeek() != courseDayOfWeek) {
                        // If the selected date is not the course day, reset to the nearest course day
                        LocalDate nearestCourseDay = selectedDate.with(TemporalAdjusters.nextOrSame(courseDayOfWeek));
                        datePickerDialog.updateDate(nearestCourseDay.getYear(), nearestCourseDay.getMonthValue() - 1, nearestCourseDay.getDayOfMonth());
                    }
                });
            }
            return datePickerDialog;}
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day){
            LocalDate date = LocalDate.of(year, ++month, day);
            ((AddEditClassActivity)getActivity()).updateDate(date);
        }
    }
    public void updateDate(LocalDate date){
        TextView dateEditText = findViewById(R.id.dateEditText);
        dateEditText.setText(date.toString());
    }
}