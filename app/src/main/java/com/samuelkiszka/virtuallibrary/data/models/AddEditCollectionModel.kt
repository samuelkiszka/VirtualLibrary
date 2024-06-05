package com.samuelkiszka.virtuallibrary.data.models

import com.google.gson.Gson
import com.samuelkiszka.virtuallibrary.data.database.entities.CollectionEntity
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

data class AddEditCollectionModel(
    val id: Int = 0,
    val name: String = "",
    val description: String = ""
) {
    fun toCollectionEntity(): CollectionEntity {
        return CollectionEntity(
            id = id.toLong(),
            name = name,
            description = description
        )
    }

    fun fromJson(data: String): AddEditCollectionModel {
        val json = URLDecoder.decode(data, StandardCharsets.UTF_8.toString())
        return Gson().fromJson(json, AddEditCollectionModel::class.java)
    }
}
