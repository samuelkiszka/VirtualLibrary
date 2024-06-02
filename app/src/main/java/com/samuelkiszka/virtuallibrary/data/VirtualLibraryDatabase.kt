package com.samuelkiszka.virtuallibrary.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.samuelkiszka.virtuallibrary.data.daos.BookDao
import com.samuelkiszka.virtuallibrary.data.daos.CollectionDao
import com.samuelkiszka.virtuallibrary.data.entities.BookEntity
import com.samuelkiszka.virtuallibrary.data.entities.CollectionBookEntity
import com.samuelkiszka.virtuallibrary.data.entities.CollectionEntity
import com.samuelkiszka.virtuallibrary.data.entities.ReadingStatusConverter

@Database(
    entities = [
        BookEntity::class,
        CollectionEntity::class,
        CollectionBookEntity::class
    ],
    version = 1,
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
                    .build()
                    .also { Instance = it }
            }
        }
    }

}