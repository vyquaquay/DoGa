package com.example.doga.Model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "courses")

public class GiogaCourseModel {
@PrimaryKey(autoGenerate = true)
public long Id;
public int capacity, duration;
public String description, typeofClass, dayOfWeek,timeofCourse;
public double price;
}
