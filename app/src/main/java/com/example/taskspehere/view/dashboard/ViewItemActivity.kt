package com.example.taskspehere.view.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskspehere.R

class ViewItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val tvDesc = findViewById<TextView>(R.id.tv_desc)

        tvTitle.text = intent.getStringExtra("title") ?: "No Title"
        tvDesc.text = intent.getStringExtra("desc") ?: "No Description"
    }
}