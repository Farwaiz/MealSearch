package com.example.cw2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ViewModel: ViewModel() {
    var stb = StringBuilder()
    var tempMeals = mutableListOf<Meals>()
    var allMeals = java.lang.StringBuilder()
    var mealSearch = mutableListOf<Meals>()

    //function to search the meal using the ingredient entered
    fun search(ingredient: String?) {
        val url_string = "https://www.themealdb.com/api/json/v1/1/filter.php?i=$ingredient"
        println(url_string)
        val url = URL(url_string)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        runBlocking {
            launch {// run the code of the coroutine in a new thread
                withContext(Dispatchers.IO) {
                    val bf = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = bf.readLine()
                    while (line != null) {
                        stb.append(line + "\n")
                        line = bf.readLine()
                    }

                    parseJSON(stb)
                    stb.clear()
                }
            }
        }
    }

    fun parseJSON(stb: java.lang.StringBuilder) {
        allMeals.clear()
        // this contains the full JSON returned by the Web Service
        val json =
            JSONObject(stb.toString())// Information about all the meals extracted by this function
        val mealsArray: JSONArray? = json.optJSONArray("meals")

        if (mealsArray == null) {
            allMeals.append("No meals found for the relevant search")
        }
        // extract all the meals from the JSON array
        else {
            for (i in 0..mealsArray.length() - 1) {
                // this is a json object
                val meals = mealsArray[i] as JSONObject
                //sends to a function to get the meal details
                ingredientSearch(i,meals)
            }
        }
    }

    //function to get all the meal details for the ingredient search
    fun ingredientSearch  (i: Int, meals: JSONObject){
        val url_string =
            "https://www.themealdb.com/api/json/v1/1/search.php?s=${meals["strMeal"]}"
        val url = URL(url_string)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection
        val stringBuild = StringBuilder()

        runBlocking {
            launch {// run the code of the coroutine in a new thread
                withContext(Dispatchers.IO) {
                    val bf = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = bf.readLine()
                    while (line != null) {
                        stringBuild.append(line + "\n")
                        line = bf.readLine()
                    }
                    val currentMeal =
                        JSONObject(stringBuild.toString()) // Information about all the meals extracted by this function

                    val mealTemp: JSONArray = currentMeal.getJSONArray("meals")
                    val meal: JSONObject = mealTemp[0] as JSONObject
                    //adding all information to all meals to display it
                    allMeals.append("${i + 1}) Meal: ${meal["strMeal"]}\n ")
                    allMeals.append("Drink Alternate: ${meal["strDrinkAlternate"]}\n ")
                    allMeals.append("Category: ${meal["strCategory"]}\n ")
                    allMeals.append("Area: ${meal["strArea"]}\n ")
                    allMeals.append("Instructions: ${meal["strInstructions"]}\n ")
                    allMeals.append("Meal Thumb: ${meal["strMealThumb"]}\n ")
                    allMeals.append("Tags: ${meal["strTags"]}\n ")
                    allMeals.append("Youtube: ${meal["strYoutube"]}\n ")
                    val ingredients = mutableListOf<String>()
                    val measures = mutableListOf<String>()
                    //gets only the ingredients and measures which are not null
                    for (j in 1..20) {
                        val ingredient = meal["strIngredient$j"]
                        if (ingredient != "") {
                            allMeals.append("Ingredient$j: $ingredient\n")
                            ingredients.add(ingredient.toString())
                        } else {
                            break
                        }
                    }
                    for (j in 1..20) {
                        val measure = meal["strMeasure$j"]
                        if (measure != "") {
                            allMeals.append("Measure$j: $measure\n")
                            measures.add(measure.toString())
                        } else {
                            break
                        }
                    }
                    allMeals.append("\n\n")
                    //add it to a list so that if user wants to save the meals, we can save by accessing the list
                    tempMeals.add(
                        Meals(
                            meal["idMeal"].toString().toInt(),
                            meal["strMeal"].toString(),
                            meal["strDrinkAlternate"].toString(),
                            meal["strCategory"].toString(),
                            meal["strArea"].toString(),
                            meal["strInstructions"].toString(),
                            meal["strMealThumb"].toString(),
                            meal["strTags"].toString(),
                            meal["strYoutube"].toString(),
                            ingredients,
                            measures,
                            meal["strSource"].toString(),
                            meal["strImageSource"].toString(),
                            meal["strCreativeCommonsConfirmed"].toString(),
                            meal["dateModified"].toString()
                        )
                    )
                }
            }
        }
    }

    //function to get meals from the database
    fun searchMealDao(mealDao: MealsDao, searchQuery: String) {
        mealSearch.clear()
        runBlocking {
            launch {
                mealSearch = getMeal(mealDao, searchQuery)
            }
        }
    }

    //function to get meals all meals from the database using search meals query
    suspend fun getMeal(mealsDao: MealsDao, searchQuery: String): MutableList<Meals> = withContext(
        Dispatchers.IO
    ) {
        // perform database operation here
        return@withContext mealsDao.searchMeals(searchQuery)
    }



    //function to search the given the meals to the relevant text entered
    fun webSearch(meal: String?) {
        val url_string = "https://www.themealdb.com/api/json/v1/1/search.php?s=$meal"
        val url = URL(url_string)
        val con: HttpURLConnection = url.openConnection() as HttpURLConnection

        runBlocking {
            launch {// run the code of the coroutine in a new thread
                withContext(Dispatchers.IO) {
                    val bf = BufferedReader(InputStreamReader(con.inputStream))
                    var line: String? = bf.readLine()
                    while (line != null) {
                        stb.append(line + "\n")
                        line = bf.readLine()
                    }

                    mealSearch.clear()
                    val json =
                        JSONObject(stb.toString())// Information about all the meals extracted by this function
                    val mealsArray: JSONArray? = json.optJSONArray("meals")
                    if (mealsArray == null) {
                        allMeals.append("No meals found for the relevant search")
                    }
                    // extract all the meals from the JSON array
                    else {
                        for (i in 0..mealsArray.length() - 1) {
                            // this is a json object
                            val meals = mealsArray[i] as JSONObject
                            mealWebSearch(meals)
                        }
                    }
                    stb.clear()
                }
            }
        }
    }

    //function which adds the meals details to a list for the web search
    fun mealWebSearch  (meal: JSONObject){
        val ingredients = mutableListOf<String>()
        val measures = mutableListOf<String>()
        //gets only the ingredients and measures which are not null
        for (j in 1..20) {
            val ingredient = meal["strIngredient$j"]
            if (ingredient != "") {
                allMeals.append("Ingredient$j: $ingredient\n")
                ingredients.add(ingredient.toString())
            } else {
                break
            }
        }
        for (j in 1..20) {
            val measure = meal["strMeasure$j"]
            if (measure != "") {
                allMeals.append("Measure$j: $measure\n")
                measures.add(measure.toString())
            } else {
                break
            }
        }

        //add it to a list to display it in the application
        tempMeals.add(
            Meals(
                meal["idMeal"].toString().toInt(),
                meal["strMeal"].toString(),
                meal["strDrinkAlternate"].toString(),
                meal["strCategory"].toString(),
                meal["strArea"].toString(),
                meal["strInstructions"].toString(),
                meal["strMealThumb"].toString(),
                meal["strTags"].toString(),
                meal["strYoutube"].toString(),
                ingredients,
                measures,
                meal["strSource"].toString(),
                meal["strImageSource"].toString(),
                meal["strCreativeCommonsConfirmed"].toString(),
                meal["dateModified"].toString()
            )
        )
    }
}