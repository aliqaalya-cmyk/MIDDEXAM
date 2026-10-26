package com.example.middexam;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText etStudentName;
    EditText etStudentId;

    Spinner spinnerMajor;

    Button btnAdd;
    Button btnEdit;
    Button btnDelete;
    Button btnView;

    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Connect XML components
        etStudentName = findViewById(R.id.etStudentName);
        etStudentId = findViewById(R.id.etStudentId);

        spinnerMajor = findViewById(R.id.spinnerMajor);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);

        // Database
        databaseHelper = new DatabaseHelper(this);

        // Major Spinner
        String[] majors = {
                "Informatics",
                "Information Systems"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                majors
        );

        spinnerMajor.setAdapter(adapter);

        // ADD
        btnAdd.setOnClickListener(v -> addStudent());

        // EDIT
        btnEdit.setOnClickListener(v -> editStudent());

        // DELETE
        btnDelete.setOnClickListener(v -> deleteStudent());

        // VIEW
        btnView.setOnClickListener(v -> viewStudents());
    }

    private void addStudent() {

        String name = etStudentName.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();
        String major = spinnerMajor.getSelectedItem().toString();

        if (name.isEmpty() || studentId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean result = databaseHelper.addStudent(
                name,
                studentId,
                major
        );

        if (result) {

            Toast.makeText(
                    this,
                    "Student added successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

        } else {

            Toast.makeText(
                    this,
                    "Student ID already exists",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void editStudent() {

        String name = etStudentName.getText().toString().trim();
        String studentId = etStudentId.getText().toString().trim();
        String major = spinnerMajor.getSelectedItem().toString();

        if (name.isEmpty() || studentId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean result = databaseHelper.updateStudent(
                studentId,
                name,
                major
        );

        if (result) {

            Toast.makeText(
                    this,
                    "Student updated successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

        } else {

            Toast.makeText(
                    this,
                    "Student ID not found",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void deleteStudent() {

        String studentId = etStudentId.getText().toString().trim();

        if (studentId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Enter Student ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        boolean result = databaseHelper.deleteStudent(studentId);

        if (result) {

            Toast.makeText(
                    this,
                    "Student deleted successfully",
                    Toast.LENGTH_SHORT
            ).show();

            clearFields();

        } else {

            Toast.makeText(
                    this,
                    "Student ID not found",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void viewStudents() {

        Cursor cursor = databaseHelper.getAllStudents();

        if (cursor.getCount() == 0) {

            Toast.makeText(
                    this,
                    "No student data found",
                    Toast.LENGTH_SHORT
            ).show();

            cursor.close();
            return;
        }

        StringBuilder data = new StringBuilder();

        while (cursor.moveToNext()) {

            int id = cursor.getInt(
                    cursor.getColumnIndexOrThrow("id")
            );

            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow("student_name")
            );

            String studentId = cursor.getString(
                    cursor.getColumnIndexOrThrow("student_id")
            );

            String major = cursor.getString(
                    cursor.getColumnIndexOrThrow("major")
            );

            data.append("ID: ")
                    .append(id)
                    .append("\n");

            data.append("Name: ")
                    .append(name)
                    .append("\n");

            data.append("Student ID: ")
                    .append(studentId)
                    .append("\n");

            data.append("Major: ")
                    .append(major)
                    .append("\n");

            data.append("--------------------\n\n");
        }

        cursor.close();

        new AlertDialog.Builder(this)
                .setTitle("Student List")
                .setMessage(data.toString())
                .setPositiveButton("OK", null)
                .show();
    }

    private void clearFields() {

        etStudentName.setText("");
        etStudentId.setText("");

        spinnerMajor.setSelection(0);
    }
}