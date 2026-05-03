package com.example.taskspehere.repository

import android.util.Log
import com.example.taskspehere.model.Item
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("items")

    // CREATE ITEM
    fun addItem(title: String, description: String, callback: (Boolean) -> Unit) {
        val id = collection.document().id
        val item = Item(id, title, description)
        collection.document(id)
            .set(item)
            .addOnSuccessListener {
                Log.d("FirestoreRepo", "Item added: $id")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepo", "Add failed", e)
                callback(false)
            }
    }

    // READ ALL ITEMS
    fun getItems(callback: (List<Item>) -> Unit) {
        collection.get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { it.toObject(Item::class.java) }
                Log.d("FirestoreRepo", "Fetched ${list.size} items")
                callback(list)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepo", "Get failed", e)
                callback(emptyList())
            }
    }

    // UPDATE ITEM
    fun updateItem(item: Item, callback: (Boolean) -> Unit) {
        collection.document(item.id)
            .set(item)
            .addOnSuccessListener {
                Log.d("FirestoreRepo", "Item updated: ${item.id}")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepo", "Update failed", e)
                callback(false)
            }
    }

    // DELETE ITEM – FIXED with proper error handling
    fun deleteItem(id: String, callback: (Boolean) -> Unit) {
        if (id.isEmpty()) {
            Log.e("FirestoreRepo", "Delete failed: empty ID")
            callback(false)
            return
        }
        collection.document(id)
            .delete()
            .addOnSuccessListener {
                Log.d("FirestoreRepo", "Item deleted: $id")
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreRepo", "Delete failed for id $id", e)
                callback(false)
            }
    }
}