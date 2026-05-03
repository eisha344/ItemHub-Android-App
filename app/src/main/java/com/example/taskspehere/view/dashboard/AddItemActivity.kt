package com.example.taskspehere.view.dashboard

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.taskspehere.R
import com.example.taskspehere.viewmodel.FirestoreViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class AddItemActivity : AppCompatActivity() {

    private lateinit var viewModel: FirestoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        viewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]

        val etTitle = findViewById<TextInputEditText>(R.id.et_title)
        val etDesc = findViewById<TextInputEditText>(R.id.et_desc)
        val btnSave = findViewById<MaterialButton>(R.id.btn_save)

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.addItem(title, desc) { success ->
                if (success) {
                    Toast.makeText(this, "Item added", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add item", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}