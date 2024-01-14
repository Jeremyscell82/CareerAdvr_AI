package com.lloydsbyte.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.lloydsbyte.database.models.ChatHeaderModel
import com.lloydsbyte.database.models.ChatModel


@Database(entities = [ChatHeaderModel::class, ChatModel::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
//    abstract fun countryDao(): CountryDataDao
    abstract fun chatHistoryDao(): ChatHistoryDao

    companion object {
        private var instance: AppDatabase? = null
        private val DB_NAME = "com.lloydsbyte.chape.db"

        //Example of migration and adding a new column
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //Example of adding a new column
                database.execSQL(
                    "ALTER TABLE users "
                            + " ADD COLUMN last_update INTEGER"
                )
            }
        }
        fun getDatabase(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context, AppDatabase::class.java,
                    DB_NAME
                )
//                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return instance!!
        }
    }
}