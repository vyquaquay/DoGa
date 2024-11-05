package com.example.doga.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.doga.Model.GiogaClassModel;
import com.example.doga.Model.GiogaCourseModel;

import java.util.List;
@Dao
public interface ClassDao {
    @Insert
    long insertCourse(GiogaClassModel GiogaClassModel);

    @Query("SELECT * FROM classes")
    List<GiogaClassModel> getAllClasses();
}
