package com.example.priorityclick

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Task(
    val title: String,
    val desc: String,
    val date: MyDate
)

@Serializable
data class MyDate(
    val day : Int,
    val month : Int,
    val year : Int
) {
    private val months : List<String> = immutableListOf("Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec")
    override fun toString(): String {
        return "$day ${months[month-1]} $year"
    }
}
@Serializable
data class TaskList(
    val list : List<Task> = mutableListOf()
)