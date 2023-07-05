package com.example.cw2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class SearchMeal: AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.meal_search)
        val searchButton=findViewById<Button>(R.id.search_meal)
        val editText: EditText = findViewById(R.id.getMeal)
        newRecyclerView=findViewById(R.id.recyclerView)
        //setting the layout of the recycler view
        newRecyclerView.layoutManager= LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        var viewModel= ViewModelProvider(this)[ViewModel::class.java]
        //to set the layout once rotated
        newRecyclerView.adapter=Recycle(viewModel.mealSearch)

        searchButton.setOnClickListener{
            val mealDao=MealDatabase.getDatabase(application).MealsDao()
            val searchQuery = editText.text.toString().lowercase(Locale.getDefault())
            //uses the function in the view model to search meal from the database
            viewModel.searchMealDao(mealDao,searchQuery)
            //sends the list to the recycler view class to display the related search items and their images
            newRecyclerView.adapter=Recycle(viewModel.mealSearch)

        }
    }

}



