package com.amir.dunifood.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// after adding new column you have to +1 version
// exportScheme show a version file of data base
@Database(entities = [Food::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    //one table
    abstract val FoodDao: foodDao

    companion object {
        private var database: MyDatabase? = null

        fun getDatabase(context: Context): MyDatabase {
            if (database == null) {
                database = Room.databaseBuilder(
                    // its for all Activity
                    context.applicationContext,
                    // class that we want to get object from for data base
                    MyDatabase::class.java,
                    "myDatabase.db"
                )
                    .allowMainThreadQueries() // do not do this for real project -> it will run in main thread
                    .build()
            }
            return database!!
            // single ton -> one object you have from database
        }
    }

}