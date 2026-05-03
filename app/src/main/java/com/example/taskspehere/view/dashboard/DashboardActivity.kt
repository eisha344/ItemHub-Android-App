package com.example.taskspehere.view.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskspehere.R
import com.example.taskspehere.adapter.ItemAdapter
import com.example.taskspehere.model.Item
import com.example.taskspehere.viewmodel.FirestoreViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var viewModel: FirestoreViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        viewModel = ViewModelProvider(this)[FirestoreViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        adapter = ItemAdapter(
            mutableListOf(),
            onDelete = { id ->
                viewModel.deleteItem(id) { success ->
                    if (success) {
                        Toast.makeText(this, "Item deleted", Toast.LENGTH_SHORT).show()
                        // ✅ Refresh the list immediately on the main thread
                        loadItems()
                    } else {
                        Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            onEdit = { item ->
                val intent = Intent(this, EditItemActivity::class.java)
                intent.putExtra("id", item.id)
                intent.putExtra("title", item.title)
                intent.putExtra("desc", item.description)
                startActivity(intent)
            },
            onView = { item ->
                val intent = Intent(this, ViewItemActivity::class.java)
                intent.putExtra("title", item.title)
                intent.putExtra("desc", item.description)
                startActivity(intent)
            }
        )
        recyclerView.adapter = adapter

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddItemActivity::class.java))
        }

        loadItems()
    }

    private fun loadItems() {
        viewModel.getItems { list ->
            // Always run on UI thread
            runOnUiThread {
                adapter.updateList(list)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadItems()
    }
}