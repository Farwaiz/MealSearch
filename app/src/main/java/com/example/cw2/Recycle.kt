package com.example.cw2

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL

class Recycle(val meals: List<Meals>): RecyclerView.Adapter<Recycle.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val mealText: TextView= itemView.findViewById(R.id.mealDetails)
        val mealImage:ImageView= itemView.findViewById(R.id.meal_image)
    }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_layout,parent,false))
    }

    // Return the size of your dataset
    override fun getItemCount(): Int {
        return meals.size
    }

    // Replace the contents of a view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem=meals[position]
        holder.mealText.text=currentItem.Meal.toString()
        Thread {
            //Gets the image and sets the image
            try {
                val connection = URL("${currentItem.MealThumb}").openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                val bitmapImage = BitmapFactory.decodeStream(input)
                holder.mealImage.post { holder.mealImage.setImageBitmap(bitmapImage) }
            } catch (e: Exception) {
                holder.mealImage.post { holder.mealImage.setImageResource(R.drawable.ic_launcher_foreground) }
            }
        }.start()
    }

}