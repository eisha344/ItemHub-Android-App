package com.example.taskspehere.viewmodel

import androidx.lifecycle.ViewModel
import com.example.taskspehere.model.Item
import com.example.taskspehere.repository.FirestoreRepository

class FirestoreViewModel : ViewModel() {
    private val repo = FirestoreRepository()

    fun addItem(title: String, description: String, callback: (Boolean) -> Unit) {
        repo.addItem(title, description, callback)
    }

    fun getItems(callback: (List<Item>) -> Unit) {
        repo.getItems(callback)
    }

    fun updateItem(item: Item, callback: (Boolean) -> Unit) {
        repo.updateItem(item, callback)
    }

    fun deleteItem(id: String, callback: (Boolean) -> Unit) {
        repo.deleteItem(id, callback)
    }
}