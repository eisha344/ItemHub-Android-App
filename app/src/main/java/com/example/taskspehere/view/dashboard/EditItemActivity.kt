package com.example.taskspehere.view.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskspehere.R
import com.example.taskspehere.model.Item
import com.example.taskspehere.viewmodel.FirestoreViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class EditItemActivity : AppCompatActivity() {

    private lateinit var viewModel: FirestoreViewModel
    private lateinit var titleLayout: TextInputLayout
    private lateinit var descLayout: TextInputLayout
    private lateinit var etTitle: TextInputEditText
    private lateinit var etDesc: TextInputEditText
    private lateinit var btnUpdate: MaterialButton
    private var itemId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        // Initialize views
        titleLayout = findViewById(R.id.title_layout)
        descLayout = findViewById(R.id.desc_layout)
        etTitle = findViewById(R.id.et_title)
        etDesc = findViewById(R.id.et_desc)
        btnUpdate = findViewById(R.id.btn_update)

        // Get data from intent
        itemId = intent.getStringExtra("id") ?: ""
        etTitle.setText(intent.getStringExtra("title") ?: "")
        etDesc.setText(intent.getStringExtra("desc") ?: "")

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]

        btnUpdate.setOnClickListener {
            performUpdate()
        }
    }

    private fun performUpdate() {
        val title = etTitle.text.toString().trim()
        val description = etDesc.text.toString().trim()

        // Reset errors
        titleLayout.error = null
        descLayout.error = null

        // Validation
        if (title.isEmpty()) {
            titleLayout.error = "Title is required"
            etTitle.requestFocus()
            return
        }
        if (description.isEmpty()) {
            descLayout.error = "Description is required"
            etDesc.requestFocus()
            return
        }

        // Disable button during update
        btnUpdate.isEnabled = false
        btnUpdate.text = "Updating..."

        val updatedItem = Item(itemId, title, description)

        viewModel.updateItem(updatedItem) { success ->
            runOnUiThread {
                btnUpdate.isEnabled = true
                btnUpdate.text = "Update"

                if (success) {
                    // ✅ Show update success alert
                    Toast.makeText(this, "Updated successfully!", Toast.LENGTH_LONG).show()
                    finish() // Return to Dashboard
                } else {
                    Toast.makeText(this, "Update failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}