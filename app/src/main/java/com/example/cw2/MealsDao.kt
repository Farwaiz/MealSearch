package com.example.cw2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MealsDao {
    @Query("Select * from meals")
    suspend fun getAll(): List<Meals>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeals(vararg user: Meals)
    @Insert
    suspend fun insertAll(vararg users: Meals)
    @Query("SELECT * FROM meals WHERE Meal LIKE '%' || :mealName || '%' OR ingredients LIKE '%' || :mealName || '%'")
    fun searchMeals(mealName: String): MutableList<Meals>
}