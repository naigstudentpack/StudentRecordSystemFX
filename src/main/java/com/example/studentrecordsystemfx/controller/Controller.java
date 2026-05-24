package com.example.studentrecordsystemfx.controller;

import com.example.studentrecordsystemfx.db.DBConnection;
import com.example.studentrecordsystemfx.model.Student;
import com.example.studentrecordsystemfx.model.YearLevel;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;

public class Controller {

    @FXML private TextField txtName;
    @FXML private TextField txtCourse;
    @FXML private ChoiceBox<YearLevel> cbYear;
    @FXML private TableView<Student> table;
    @FXML private TableColumn<Student, Integer> colId;
    @FXML private TableColumn<Student, String> colName;
    @FXML private TableColumn<Student, String> colCourse;
    @FXML private TableColumn<Student, String> colYear;

    private ObservableList<Student> list = FXCollections.observableArrayList();
    private Connection conn;
    private int selectedId = -1;

    @FXML
    public void initialize() {
        conn = DBConnection.getConnection();

        cbYear.getItems().setAll(YearLevel.values());

        colId.setCellValueFactory(data -> data.getValue().idProperty().asObject());
        colName.setCellValueFactory(data -> data.getValue().nameProperty());
        colCourse.setCellValueFactory(data -> data.getValue().courseProperty());
        colYear.setCellValueFactory(data -> data.getValue().yearLevelProperty());

        loadData();

        table.setOnMouseClicked(event -> {
            Student selected = table.getSelectionModel().getSelectedItem();
            if (selected != null) {
                selectedId = selected.getId();
                txtName.setText(selected.getName());
                txtCourse.setText(selected.getCourse());
                for (YearLevel y : YearLevel.values()) {
                    if (y.toString().equals(selected.getYearLevel())) {
                        cbYear.setValue(y);
                        break;
                    }
                }
            }
        });
    }

    private void loadData() {
        list.clear();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM students ORDER BY id")) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("year_level")
                ));
            }
            table.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addStudent() {
        if (txtName.getText().isEmpty() || txtCourse.getText().isEmpty() || cbYear.getValue() == null) {
            showAlert("Please fill all fields!");
            return;
        }
        try (PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO students (name, course, year_level) VALUES (?, ?, ?)")) {
            pst.setString(1, txtName.getText());
            pst.setString(2, txtCourse.getText());
            pst.setString(3, cbYear.getValue().toString());
            pst.executeUpdate();
            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateStudent() {
        if (selectedId == -1) {
            showAlert("Please select a student first!");
            return;
        }
        if (txtName.getText().isEmpty() || txtCourse.getText().isEmpty() || cbYear.getValue() == null) {
            showAlert("Please fill all fields!");
            return;
        }
        try (PreparedStatement pst = conn.prepareStatement(
                "UPDATE students SET name=?, course=?, year_level=? WHERE id=?")) {
            pst.setString(1, txtName.getText());
            pst.setString(2, txtCourse.getText());
            pst.setString(3, cbYear.getValue().toString());
            pst.setInt(4, selectedId);
            pst.executeUpdate();
            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteStudent() {
        if (selectedId == -1) {
            showAlert("Please select a student first!");
            return;
        }
        try (PreparedStatement pst = conn.prepareStatement("DELETE FROM students WHERE id=?")) {
            pst.setInt(1, selectedId);
            pst.executeUpdate();
            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clearFields() {
        txtName.clear();
        txtCourse.clear();
        cbYear.setValue(null);
        selectedId = -1;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}