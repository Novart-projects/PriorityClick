package com.example.priorityclick

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Suppress("BlockingMethodInNonBlockingContext")
object TaskSerializer : Serializer<TaskList> {

    override val defaultValue: TaskList
        get() = TaskList()

    override suspend fun readFrom(input: InputStream): TaskList {
        return try {
            Json.decodeFromString(
                deserializer = TaskList.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: TaskList, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = TaskList.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}