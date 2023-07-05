package com.example.cw2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class WebSearch: AppCompatActivity() {
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
        var viewModel= ViewModelProvider(this).get(ViewModel::class.java)
        //to set the layout once rotated
        newRecyclerView.adapter=Recycle(viewModel.tempMeals)

        searchButton.setOnClickListener{
            searchButton.isEnabled=false
            viewModel.tempMeals.clear()
            val searchQuery = editText.text.toString().lowercase(Locale.getDefault())
            viewModel.webSearch(searchQuery)
            newRecyclerView.adapter=Recycle(viewModel.tempMeals)
            searchButton.isEnabled=true
        }
    }
}