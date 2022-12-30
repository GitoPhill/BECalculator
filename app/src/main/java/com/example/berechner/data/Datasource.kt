package com.example.berechner.data

import android.content.Context
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import com.example.berechner.model.Food
import com.example.berechner.model.MealComponent
import com.example.berechner.model.Unit
import java.io.File
import java.io.StringWriter

private const val TAG = "Datasource"

class Datasource (val context: Context){
    fun loadTestMeal(): MutableList<MealComponent> {
        return mutableListOf<MealComponent>(
            MealComponent(Food("VK Brot", Unit.g, 45, 18.0, 1.5)),
            MealComponent(Food("Nudeln", Unit.g, 100,28.7, 2.39)),
            MealComponent(Food("Reiswaffel", Unit.g, 100, 80.6, 6.72))
        )
    }

    fun loadFoodList(): List<Food> {

        val file: File = File(context.filesDir, "foodDB.json")
        if (file.exists() == false){
            Log.d(TAG, "File ${file.name} does not exist yet. Returning an empty list.")
            return listOf()
        }

        val resultList: MutableList<Food> = mutableListOf()

        val reader = JsonReader(File(context.filesDir, "foodDB.json").reader(Charsets.UTF_8))
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

        Log.d("DataSource", "resultList.size = ${resultList.size}")
        return resultList
    }

    fun storeData(foodList: List<Food>) {
        Log.d(TAG, "Store data:")

        val result = StringWriter()
        val writer = JsonWriter(result)
        writer.setIndent("  ")

        writer.beginArray()
        for (item in foodList) {
            item.writeJson(writer)
            result.appendLine()
        }
        writer.endArray()
        writer.close()

        Log.d(TAG, result.toString())

        val file = File(context.filesDir, "foodDB.json")
        file.writeText(result.toString(), Charsets.UTF_8)
    }

}