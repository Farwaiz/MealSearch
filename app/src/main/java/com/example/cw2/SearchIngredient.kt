package com.example.cw2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SearchIngredient: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ingredient_search)
        val retrieveMeals: Button = findViewById(R.id.retrieve)
        val editText: EditText = findViewById(R.id.getIngredient)
        val details=findViewById<TextView>(R.id.meal_details)
        val saveButton=findViewById<Button>(R.id.save)
        var viewModel= ViewModelProvider(this).get(ViewModel::class.java)
        details.text=viewModel.allMeals.toString()
        //retrieves the meals and displays once the retrieve meals button is clicked
        retrieveMeals.setOnClickListener {
            viewModel.tempMeals.clear()
            retrieveMeals.isEnabled=false
            val ingredient = editText.text.toString()
            println(ingredient)
            viewModel.search(ingredient)
            details.text=viewModel.allMeals.toString()
            retrieveMeals.isEnabled=true
        }
        //adds meals to the database once save meals button is clicked
        saveButton.setOnClickListener{
            runBlocking {
                launch {// run the code of the coroutine in a new thread
                    val mealDao=MealDatabase.getDatabase(application).MealsDao()
                    for (meal in viewModel.tempMeals){
                        mealDao.insertMeals(meal)
                    }
                    viewModel.tempMeals.clear()
                }
            }
            Toast.makeText(applicationContext,"Meals Added", Toast.LENGTH_SHORT).show()
        }
    }
}
