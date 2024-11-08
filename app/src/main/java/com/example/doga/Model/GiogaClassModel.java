package com.example.doga.Model;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import java.util.List;

@Entity(tableName = "classes", foreignKeys = @ForeignKey(
        entity = GiogaCourseModel.class,
        parentColumns = "Id",
        childColumns = "courseId",
        onDelete = ForeignKey.CASCADE
))
public class GiogaClassModel {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long courseId;
    public String date;
    public String teacher;
    public String comment;

    // No-argument constructor
    public GiogaClassModel() {
        // Initialize properties with default values if needed
        this.courseId = 0; // Or any other default value
        this.date = "";
        this.teacher = "";
        this.comment = "";
    }

    public GiogaClassModel(long courseId, String date, String teacher, String comment) {
        this.courseId = courseId;
        this.date = date;
        this.teacher = teacher;
        this.comment = comment;
    }
}