package com.example.berechner.model

import android.util.JsonWriter

enum class Unit { g, ml}

data class Food (
    var name: String,
    var unit: Unit,
    var amount: Int,
    var carbs: Double,
    var bread_unit: Double
) {

    fun writeJson(writer: JsonWriter) {
        writer.beginObject()
        writer.name("name").value(name)
        writer.name("unit").value(unit.toString())
        writer.name("amount").value(amount)
        writer.name("carbs").value(carbs)
        writer.name("BE").value(bread_unit)
        writer.endObject()
    }
}