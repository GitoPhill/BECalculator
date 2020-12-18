package com.example.berechner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.berechner.adapter.ItemAdapter
import com.example.berechner.adapter.ItemAdapterUpdateInterface
import com.example.berechner.databinding.ActivityMainBinding
import com.example.berechner.model.BECalculator
import com.example.berechner.model.MealComponent
import java.io.File
import java.io.StringWriter


private const val TAG = "MainActivity"
lateinit var mealComponentList : MutableList<MealComponent>

class MainActivity : AppCompatActivity(), ItemAdapterUpdateInterface {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "MainActivity.onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView
        mealComponentList = mutableListOf()
        recyclerView.adapter = ItemAdapter(this, mealComponentList, this)
        recyclerView.setHasFixedSize(true)

        binding.floatingActionButtonClear.setOnClickListener{ clearMeal() }
        binding.floatingActionButtonAdd.setOnClickListener{
            val intent = Intent( Intent.ACTION_PICK,
                Uri.EMPTY,
                this,
                                FoodSelection::class.java)

            startActivityForResult(intent, FoodSelection.FoodSelectionResultCode)
        }

        binding.textViewTotal.text = "Gesamt: 0.0 BE"
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity.onResume")
    }

    fun clearMeal() {
        mealComponentList.clear()
        binding.recyclerView.adapter?.notifyDataSetChanged()
        updateResult()
    }

    fun storeData() {
        Log.d(TAG, "Store data:")

        var result = StringWriter()
        var writer = JsonWriter(result)
        writer.setIndent("  ")

        writer.beginArray()
        for (data in mealComponentList) {
            data.food.writeJson(writer)
        }
        writer.endArray()
        writer.close()

        Log.d( TAG, result.toString())

        val file = File(applicationContext.filesDir, "foodDB.json")
        file.writeText(result.toString())
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "MainActivity.onActivityResult (requestCode = ${requestCode}, resultCode = ${resultCode})")

        if (resultCode != FoodSelection.FoodSelectionResultCode) {
            Log.d(TAG, "Received unexepected result code from FoodSelection (${resultCode})")
            return
        }

        if (selection.isEmpty()) {
            Log.d(TAG, "No food selected.")
            return
        }

        mealComponentList.add(MealComponent(selection[0]))
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    fun updateResult() {
        var result: Double = 0.0
        for (component in mealComponentList) {
            result += component.bread_unit
        }

        binding.textViewTotal.text = String.format("Gesamt: %1$.1f BE", result)
    }

    override fun onItemUpdate(position: Int, amount: Int) {
        mealComponentList[position].bread_unit = amount.toDouble()
        mealComponentList[position].bread_unit = BECalculator().calculate(mealComponentList[position].food, amount)
        mealComponentList[position].amount = amount
        binding.recyclerView.adapter?.notifyItemChanged(position)

        updateResult()
    }
}