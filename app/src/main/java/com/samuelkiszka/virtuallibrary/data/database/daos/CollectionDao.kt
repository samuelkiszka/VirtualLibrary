package com.samuelkiszka.virtuallibrary.data.database.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.database.entities.CollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: CollectionEntity): Long

    @Query("""
        UPDATE collections
        SET name = :name, description = :description
        WHERE id = :id
    """)
    suspend fun updateCollection(id: Long, name: String, description: String)

    @Query("""
        DELETE FROM collections
        WHERE id = :id
    """)
    suspend fun deleteCollection(id: Long)

    @Query("""
        SELECT * 
        FROM collections 
        WHERE id = :id
    """)
    fun getCollectionById(id: Long): Flow<CollectionEntity?>

    @Query("""
        SELECT * 
        FROM collections
    """)
    fun getCollectionList(): Flow<List<CollectionEntity>>
}