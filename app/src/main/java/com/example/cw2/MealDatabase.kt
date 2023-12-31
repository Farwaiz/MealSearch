package com.example.cw2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Meals::class], version=1)
@TypeConverters(Converter::class)
abstract class MealDatabase: RoomDatabase() {
    abstract fun MealsDao(): MealsDao

    companion object{
        @Volatile
        private var INSTANCE: MealDatabase? =null

        fun getDatabase(context: Context): MealDatabase{
            val tempInstance = INSTANCE
            if (tempInstance!=null){
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MealDatabase::class.java,
                    "meal_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}