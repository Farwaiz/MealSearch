package com.example.cw2

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.security.CodeSource

@Entity
data class Meals (
    @PrimaryKey val id : Int,
    val Meal: String,
    val DrinkAlternate: String?,
    val Category: String?,
    val Area: String?,
    val Instructions: String?,
    val MealThumb: String?,
    val Tags: String?,
    val Youtube: String?,
    val Ingredients: List<String>,
    val Measure: List <String>,
    val Source: String?,
    val ImageSource: String?,
    val CreativeCommonsConfirmed: String?,
    val dateModified: String?
)