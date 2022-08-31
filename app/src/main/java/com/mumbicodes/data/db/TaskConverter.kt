package com.mumbicodes.data.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.mumbicodes.domain.model.Task
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Tells Room how to convert task data class
 * */
@ProvidedTypeConverter
class TaskConverter {

    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    private val tasksType = Types.newParameterizedType(List::class.java, Task::class.java)
    private val tasksAdapter = moshi.adapter<List<Task>>(tasksType)

    @TypeConverter
    fun fromStringToTasks(value: String): List<Task> =
        tasksAdapter.fromJson(value)!!

    @TypeConverter
    fun fromTasksToString(tasks: List<Task>): String =
        tasksAdapter.toJson(tasks)
}
