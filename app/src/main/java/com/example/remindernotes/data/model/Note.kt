package com.example.remindernotes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val description: String,
    val reminderDate: Long?,
    val createdAt: Long = System.currentTimeMillis()
)