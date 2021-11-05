package com.example.berechner.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.berechner.data.FoodRepository

private const val TAG = "MealCompVM"

class MealCompositionViewModel(application: Application): AndroidViewModel(application) {
    val repo = FoodRepository(application.applicationContext)
    var mealList: MutableLiveData<MutableList<MealComponent>> = repo.loadTestMeal()
    val foodList: LiveData<MutableList<Food>> = repo.getFoodList()

    fun update()
    {
        mealList = repo.loadTestMeal()
    }

    fun addMealComponent(mealComponent: MealComponent) {
        mealList.value?.add(mealComponent)
    }

    fun clearMealComponentList() {
        Log.d(TAG, "mealList size ${mealList.value?.size} ")
        mealList.value = mutableListOf()
        Log.d(TAG, "clearMealComponentList()")
        Log.d(TAG, "mealList size ${mealList.value?.size} ")
    }

    fun updateMealComponent(position: Int, amount: Int) {
//        viewModel.mealList.value!![position].bread_unit = amount.toDouble()
//        viewModel.mealList.value!![position].bread_unit = BECalculator().calculate(viewModel.mealList.value!![position].food, amount)
//        viewModel.mealList.value!![position].amount = amount
        var _list = mealList.value!!

        var mealComponent: MealComponent = _list[position]

        mealComponent.bread_unit = BECalculator().calculate(mealComponent.food, amount)
        mealComponent.amount = amount

        mealList.value = _list

        Log.d(TAG, "updateMealComponent: food = ${mealComponent.food.name}")
    }
}


class SharedViewModel : ViewModel() {

    val selected = MutableLiveData<Food>()

    fun select(item: Food) {
        selected.value = item
    }
}
