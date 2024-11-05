package com.example.doga.Model;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.Relation;
import java.util.List;
@Entity(tableName = "classes", foreignKeys = @ForeignKey
        (entity = GiogaCourseModel.class,
                parentColumns = "Id",
                childColumns = "courseId",
                onDelete = ForeignKey.CASCADE
        ))
public class GiogaClassModel {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public int courseId;
    public String date;
    public String teacher;
    public String comment;

    public GiogaClassModel(int courseId, String date, String teacher, String comment) {
        this.courseId = courseId;
        this.date = date;
        this.teacher = teacher;
        this.comment = comment;
    }
}