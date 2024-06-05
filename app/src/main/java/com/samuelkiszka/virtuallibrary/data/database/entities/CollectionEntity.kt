package com.samuelkiszka.virtuallibrary.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.samuelkiszka.virtuallibrary.ui.screens.collection.CollectionDetailUiState
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Entity(tableName = "collections")
data class CollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String = "",
    val description: String = ""
) {
    fun toCollectionDetailUiState(): CollectionDetailUiState {
        return CollectionDetailUiState(
            collection = this,
            books = listOf()
        )
    }

    fun getJsonUrlEncoded(): String {
        val data = """
            {
                "id": $id,
                "name": "$name",
                "description": "$description"
            }
        """
        return URLEncoder.encode(data, StandardCharsets.UTF_8.toString())
    }
}
