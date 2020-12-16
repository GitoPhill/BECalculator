package com.example.berechner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.JsonWriter
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.berechner.adapter.ItemAdapter
import com.example.berechner.databinding.ActivityMainBinding
import com.example.berechner.model.MealComponent
import java.io.File
import java.io.StringWriter


private const val TAG = "MainActivity"
lateinit var myDataset : MutableList<MealComponent>

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "MainActivity.onCreate")

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = binding.recyclerView
        myDataset = mutableListOf()
        recyclerView.adapter = ItemAdapter(this, myDataset)
        recyclerView.setHasFixedSize(true)

        binding.floatingActionButtonClear.setOnClickListener{ clearMeal() }
        binding.floatingActionButtonAdd.setOnClickListener{
            val intent = Intent( Intent.ACTION_PICK,
                Uri.EMPTY,
                this,
                                FoodSelection::class.java)

            startActivityForResult(intent, FoodSelection.FoodSelectionResultCode)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "MainActivity.onResume")
    }

    fun clearMeal() {
        myDataset.clear()
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

    fun storeData() {
        Log.d(TAG, "Store data:")

        var result = StringWriter()
        var writer = JsonWriter(result)
        writer.setIndent("  ")

        writer.beginArray()
        for (data in myDataset) {
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
        Log.d(TAG, "MainActivity.onActivityResult")

        myDataset.add(MealComponent(selection[0]))
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }

}