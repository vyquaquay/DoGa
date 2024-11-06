package com.example.doga.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.doga.Model.GiogaClassModel;

import java.util.List;
@Dao
public interface ClassDao {
    @Insert
    long insertClass(GiogaClassModel GiogaClassModel);

    @Query("SELECT * FROM classes")
    List<GiogaClassModel> getAllClasses();
    @Query("SELECT * FROM classes WHERE id = :id")
    GiogaClassModel getClassById(long id);
    @Query("SELECT * FROM classes WHERE courseId = :courseId")
    List <GiogaClassModel> getClassBycourseId(long courseId);
    @Update
    void updateClass(GiogaClassModel giogaClassModel);
    @Delete
    void deleteClasse(GiogaClassModel classe);
}
