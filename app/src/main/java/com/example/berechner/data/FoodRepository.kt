package com.example.berechner.data

import android.util.JsonReader
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.berechner.model.Food
import com.example.berechner.model.MealComponent
import com.example.berechner.model.Unit
import java.io.File

private const val TAG = "FoodRepository"

class FoodRepository() {

    fun loadTestMeal(): MutableLiveData<MutableList<MealComponent>> {
//        return LiveData<MutableList<MealComponent>>(mutableListOf<MealComponent>(
//            MealComponent(Food("VK Brot", Unit.g, 45, 18.0, 1.5)),
//            MealComponent(Food("Nudeln", Unit.g, 100,28.7, 2.39)),
//            MealComponent(Food("Reiswaffel", Unit.g, 100, 80.6, 6.72))
//        ))
          return MutableLiveData<MutableList<MealComponent>>(mutableListOf<MealComponent>(
              MealComponent(Food("VK Brot", Unit.g, 45, 18.0, 1.5)),
              MealComponent(Food("Nudeln", Unit.g, 100,28.7, 2.39)),
              MealComponent(Food("Reiswaffel", Unit.g, 100, 80.6, 6.72))))
    }

    fun getFoodList(reader: JsonReader): MutableList<Food> {
        val resultList = mutableListOf<Food>()

        reader.beginArray()

        while (reader.hasNext()) {
            var food_name: String = ""
            var food_unit: Unit = Unit.g
            var food_amount: Int = 0
            var food_carbs: Double = 0.0
            var food_BEs: Double = 0.0

            reader.beginObject()
            while(reader.hasNext()) {
                val name : String = reader.nextName()
                if (name.equals("name")){
                    food_name = reader.nextString()
                }
                else if(name.equals("unit")) {
                    if (reader.nextString().equals("g")){
                        food_unit = Unit.g
                    }
                    else {
                        food_unit = Unit.ml
                    }
                }
                else if (name.equals("amount")){
                    food_amount = reader.nextInt()
                }
                else if (name.equals("carbs")){
                    food_carbs = reader.nextDouble()
                }
                else if (name.equals("BE")) {
                    food_BEs = reader.nextDouble()
                }
                else{
                    Log.e( TAG, "Invalid format! Json element name \"" + name + "\" is unknown.")
                    error("Invalid format!")
                }
            }
            resultList.add(Food(food_name, food_unit, food_amount, food_carbs, food_BEs))
            reader.endObject()
        }

        reader.endArray()
        reader.close()

        Log.d(TAG, "resultList.size = ${resultList.size}")
        return resultList
    }

    fun getFoodList(file: File): MutableList<Food> {

        if (!file.exists()){
            Log.d(TAG, "File ${file.name} does not exist yet. Returning an empty list.")
            return mutableListOf<Food>()
        }

        return getFoodList(JsonReader(file.reader()))
    }


}