package com.example.berechner.model

import android.app.Application
import android.util.JsonReader
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.berechner.data.FoodRepository
import java.io.File

private const val TAG = "MealCompVM"

class MealCompositionViewModel(application: Application): AndroidViewModel(application) {
    private val repo = FoodRepository()
    val mealList: MutableLiveData<MutableList<MealComponent>> by lazy {
        MutableLiveData<MutableList<MealComponent>>(mutableListOf<MealComponent>())
    }

    private val foodListDB: MutableList<Food> by lazy {
        repo.getFoodList(File(application.applicationContext.filesDir, "foodDB.json"))
    }

    val foodList: MutableLiveData<MutableList<Food>> by lazy {
        MutableLiveData<MutableList<Food>>(foodListDB)
    }

    fun filterFoodList(name: String) {
        if (name.isNullOrEmpty()) {
            foodList.value = foodListDB
        } else {
            foodList.value = mutableListOf()
            for (food in foodListDB) {
                if (food.name.contains(name, true)) {
                    foodList.value!!.add(food)
                }
            }
        }
    }

    fun loadFromFile(reader: JsonReader) {
        foodList.value = repo.getFoodList(reader)
    }

    fun clearMealComponentList() {
        Log.d(TAG, "mealList size ${mealList.value?.size} ")
        mealList.value = mutableListOf()
        Log.d(TAG, "clearMealComponentList()")
        Log.d(TAG, "mealList size ${mealList.value?.size} ")
    }

    fun updateMealComponent(position: Int, amount: Int) {
        val list = mealList.value!!

        val mealComponent: MealComponent = list[position]

        mealComponent.bread_unit = BECalculator().calculate(mealComponent.food, amount)
        mealComponent.amount = amount

        mealList.value = list

        Log.d(TAG, "updateMealComponent: food = ${mealComponent.food.name}")
    }

    fun getBEsForMeal(): Double {
        var result: Double = 0.0
        for (meal in mealList.value!!) { result += meal.bread_unit }
        return result
    }
}
