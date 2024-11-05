package com.example.doga.Dao;

// /database/AppDatabase.java
import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.doga.Model.GiogaClassModel;
import com.example.doga.Model.GiogaCourseModel;

@Database(entities = {GiogaCourseModel.class, GiogaClassModel.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CourseDao CourseDao();
    public abstract ClassDao ClassDao();
}
