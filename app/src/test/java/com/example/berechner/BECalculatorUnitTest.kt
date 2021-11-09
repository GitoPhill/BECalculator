package com.example.berechner

import com.example.berechner.model.BECalculator
import com.example.berechner.model.Food
import com.example.berechner.model.Unit
import org.junit.Assert.assertEquals
import org.junit.Test



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BECalculatorUnitTest {
    @Test
    fun calculation_isCorrect() {
        val food = Food("Apfel", Unit.g, 150, 15.0, 0.0)
        val amount: Int = 50
        val expectedBreadUnits: Double = amount.toDouble() * (food.carbs / food.amount) / 12.0

        assertEquals(expectedBreadUnits, BECalculator().calculate(food, amount), 0.0)
    }
}