package com.example.mypersonalassistant

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TaskManagementActivity : AppCompatActivity() {

    // List to hold tasks
    private val tasksList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_management)

        // Initialize the UI components
        val rootView: ConstraintLayout = findViewById(R.id.rootLayout)
        val etTask: EditText = findViewById(R.id.etTask)
        val btnAddTask: Button = findViewById(R.id.btnAddTask)
        val lvTasks: ListView = findViewById(R.id.lvTasks)

        // Set up the adapter for the ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, tasksList)
        lvTasks.adapter = adapter

        // Set the click listener for the "Add Task" button
        btnAddTask.setOnClickListener {
            val task = etTask.text.toString().trim()

            if (task.isNotEmpty()) {
                // Add the task to the list and notify the adapter
                tasksList.add(task)
                adapter.notifyDataSetChanged()

                // Clear the input field
                etTask.text.clear()

                // Show a success message
                Toast.makeText(this, "Task added successfully", Toast.LENGTH_SHORT).show()
            } else {
                // Show an error message if input is empty
                Toast.makeText(this, "Please enter a task", Toast.LENGTH_SHORT).show()
            }
        }

        // Set a long click listener for editing or deleting tasks from the list
        lvTasks.setOnItemLongClickListener { _, _, position, _ ->
            // Show a dialog to edit or delete the task
            AlertDialog.Builder(this)
                .setTitle("Task Options")
                .setMessage("What would you like to do with this task?")
                .setPositiveButton("Edit") { _, _ ->
                    // Edit the task
                    showEditTaskDialog(position)
                }
                .setNegativeButton("Delete") { _, _ ->
                    // Delete the task
                    tasksList.removeAt(position)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Task deleted successfully", Toast.LENGTH_SHORT).show()
                }
                .setNeutralButton("Cancel", null)
                .show()

            true
        }

        // Apply window insets to adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Function to show an edit task dialog
    private fun showEditTaskDialog(position: Int) {
        val task = tasksList[position]
        val editTaskEditText = EditText(this)
        editTaskEditText.setText(task)

        AlertDialog.Builder(this)
            .setTitle("Edit Task")
            .setView(editTaskEditText)
            .setPositiveButton("Save") { _, _ ->
                val updatedTask = editTaskEditText.text.toString().trim()
                if (updatedTask.isNotEmpty()) {
                    tasksList[position] = updatedTask
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Task updated successfully", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
