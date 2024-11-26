package com.example.mypersonalassistant

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mypersonalassistant.databinding.ActivityEventSchedulingBinding
import java.util.Calendar
import java.util.Locale

class EventSchedulingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventSchedulingBinding
    private var selectedDate: String? = null
    private var selectedTime: String? = null
    private val eventsList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize data binding
        binding = ActivityEventSchedulingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use window insets for better edge-to-edge layout
        ViewCompat.setOnApplyWindowInsetsListener(binding.rootLayout) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.rootLayout.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize the adapter for the ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, eventsList)
        binding.lvSavedEvents.adapter = adapter

        // Date picker for choosing event date
        binding.btnChooseDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
                binding.tvSelectedDate.text = String.format(getString(R.string.date_set), selectedDate)
            }, year, month, day).show()
        }

        // Time picker for choosing event time (AM/PM)
        binding.btnChooseTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            TimePickerDialog(this, { _, selectedHour, selectedMinute ->
                val isPM = if (selectedHour >= 12) "PM" else "AM"
                val formattedHour = if (selectedHour > 12) selectedHour - 12 else selectedHour
                selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s", formattedHour, selectedMinute, isPM)
                binding.tvSelectedTime.text = String.format(getString(R.string.time_set), selectedTime)
            }, hour, minute, false).show() // `false` indicates a 12-hour format
        }

        // Save event button click listener
        binding.btnSaveEvent.setOnClickListener {
            val eventTitle = binding.etEventTitle.text.toString().trim()

            if (eventTitle.isEmpty()) {
                Toast.makeText(this, getString(R.string.enter_event_title), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedDate == null) {
                Toast.makeText(this, getString(R.string.select_date_prompt), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedTime == null) {
                Toast.makeText(this, getString(R.string.select_time_prompt), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save the event (adding to the list and updating the adapter)
            val eventDetails = "Event: $eventTitle\nDate: $selectedDate\nTime: $selectedTime"
            eventsList.add(eventDetails)
            adapter.notifyDataSetChanged()

            // Show a message indicating the event was saved
            Toast.makeText(this, String.format(getString(R.string.event_saved), eventTitle), Toast.LENGTH_LONG).show()

            // Clear the input fields for the next event
            binding.etEventTitle.text.clear()
            binding.tvSelectedDate.text = getString(R.string.date_set, "")
            binding.tvSelectedTime.text = getString(R.string.time_set, "")
            selectedDate = null
            selectedTime = null
        }

        // Long press to edit or delete an event
        binding.lvSavedEvents.setOnItemLongClickListener { _, _, position, _ ->
            val options = arrayOf("Edit", "Delete")
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Choose an option")
            builder.setItems(options) { _, which ->
                when (which) {
                    0 -> { // Edit option
                        showEditEventDialog(position)
                    }
                    1 -> { // Delete option
                        eventsList.removeAt(position)
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this, "Event deleted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.show()
            true
        }
    }

    private fun showEditEventDialog(position: Int) {
        val selectedEvent = eventsList[position]

        // Extract the event title only (assuming the format is consistent as: Event: title\nDate: date\nTime: time)
        val eventTitle = selectedEvent.substringAfter("Event: ").substringBefore("\nDate:")

        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_event, null)
        val etEditEvent: EditText = dialogView.findViewById(R.id.etEditEvent)
        etEditEvent.setText(eventTitle)

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Edit Event")
        builder.setView(dialogView)
        builder.setPositiveButton("Save") { _, _ ->
            val editedTitle = etEditEvent.text.toString().trim()
            if (editedTitle.isNotEmpty()) {
                // Update the event details, keeping the original date and time
                val date = selectedEvent.substringAfter("Date: ").substringBefore("\nTime:")
                val time = selectedEvent.substringAfter("Time: ")
                val updatedEvent = "Event: $editedTitle\nDate: $date\nTime: $time"
                eventsList[position] = updatedEvent
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Event updated", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Event cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
