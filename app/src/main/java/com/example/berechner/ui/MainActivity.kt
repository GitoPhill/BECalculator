package com.example.berechner.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.berechner.R
import com.example.berechner.model.MealComponent


private const val TAG = "MainActivity"
lateinit var mealComponentList : MutableList<MealComponent>

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "MainActivity.onCreate")
        setContentView(R.layout.activity_main)
    }
}