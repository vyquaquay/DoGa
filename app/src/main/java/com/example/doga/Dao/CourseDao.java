package com.example.doga.Dao;

// /dao/PersonDao.java
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doga.Model.GiogaCourseModel;

import java.util.List;

@Dao
public interface CourseDao {
    @Insert
    long insertCourse(GiogaCourseModel GioCourseModel);

    @Query("SELECT * FROM courses")
    List<GiogaCourseModel> getAllCourse();

    @Query("SELECT * FROM courses WHERE Id = :courseId")
    GiogaCourseModel getCourseById(long courseId);
    @Update
    void updateCourse(GiogaCourseModel course);
    @Delete
    void deleteCourse(GiogaCourseModel course); // Delete a single course

    @Delete
    void deleteCourses(List<GiogaCourseModel> courses); // Delete multiple courses

}

