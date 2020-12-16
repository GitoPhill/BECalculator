package com.example.berechner.model

const val carbsPerBreadUnit = 12

class BECalculator (){
    fun calculate(food: Food, amount: Int): Double {
        return amount.toDouble() * (food.carbs / food.amount) / carbsPerBreadUnit
    }
}