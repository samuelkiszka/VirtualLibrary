package com.samuelkiszka.virtuallibrary.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.samuelkiszka.virtuallibrary.data.database.daos.BookDao
import com.samuelkiszka.virtuallibrary.data.database.daos.CollectionDao
import com.samuelkiszka.virtuallibrary.data.database.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.database.entities.CollectionBookEntity
import com.samuelkiszka.virtuallibrary.data.database.entities.CollectionEntity
import com.samuelkiszka.virtuallibrary.data.database.entities.ReadingStatusConverter

@Database(
    entities = [
        BookEntity::class,
        CollectionEntity::class,
        CollectionBookEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(ReadingStatusConverter::class)
abstract class VirtualLibraryDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun collectionDao(): CollectionDao

    companion object {
        @Volatile
        private var Instance: VirtualLibraryDatabase? = null

        fun getDatabase(context: Context): VirtualLibraryDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    VirtualLibraryDatabase::class.java,
                    "virtual_library_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}