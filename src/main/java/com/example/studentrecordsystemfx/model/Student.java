package com.example.studentrecordsystemfx.model;

import javafx.beans.property.*;

public class Student {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty course = new SimpleStringProperty();
    private final StringProperty yearLevel = new SimpleStringProperty();

    public Student(int id, String name, String course, String yearLevel) {
        setId(id);
        setName(name);
        setCourse(course);
        setYearLevel(yearLevel);
    }

    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getCourse() { return course.get(); }
    public String getYearLevel() { return yearLevel.get(); }

    public void setId(int value) { id.set(value); }
    public void setName(String value) { name.set(value); }
    public void setCourse(String value) { course.set(value); }
    public void setYearLevel(String value) { yearLevel.set(value); }

    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty courseProperty() { return course; }
    public StringProperty yearLevelProperty() { return yearLevel; }
}