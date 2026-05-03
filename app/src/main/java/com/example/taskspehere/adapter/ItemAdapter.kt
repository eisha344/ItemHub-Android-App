package com.example.taskspehere.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskspehere.R
import com.example.taskspehere.model.Item
import com.google.android.material.button.MaterialButton

class ItemAdapter(
    private var list: MutableList<Item>,
    private val onDelete: (String) -> Unit,
    private val onEdit: (Item) -> Unit,
    private val onView: (Item) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.title)
        val desc: TextView = view.findViewById(R.id.desc)
        val btnDelete: MaterialButton = view.findViewById(R.id.btn_delete)
        val btnEdit: MaterialButton = view.findViewById(R.id.btn_edit)
        val btnView: MaterialButton = view.findViewById(R.id.btn_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.title.text = item.title
        holder.desc.text = item.description

        holder.btnDelete.setOnClickListener { onDelete(item.id) }
        holder.btnEdit.setOnClickListener { onEdit(item) }
        holder.btnView.setOnClickListener { onView(item) }
    }

    override fun getItemCount() = list.size

    // ✅ This method correctly clears the list and adds fresh data
    fun updateList(newList: List<Item>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }
}