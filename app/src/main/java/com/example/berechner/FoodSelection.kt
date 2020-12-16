package com.example.berechner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.berechner.adapter.FoodSelectionAdapter
import com.example.berechner.data.Datasource
import com.example.berechner.databinding.ActivityFoodSelectionBinding
import com.example.berechner.model.Food

private const val TAG = "FoodSelection"

lateinit var foodList : MutableList<Food>
lateinit var selection : MutableList<Food>

class FoodSelection : AppCompatActivity() {

    companion object {
        const val FoodSelectionResultCode = 1
    }

    lateinit var binding: ActivityFoodSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        foodList = Datasource(applicationContext).loadFoodList().toMutableList()

        val recyclerView = binding.recyclerView
        recyclerView.adapter = FoodSelectionAdapter(this)
        setResult(FoodSelectionResultCode)

        selection = mutableListOf()

        binding.floatingActionButtonAdd.setOnClickListener{
            val intent = Intent(this, EditFoodActivity::class.java)
            startActivityForResult(intent, EditFoodActivity.NewFoodResultCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != EditFoodActivity.NewFoodResultCode) {
            Log.d(TAG, "Received unexpected result from child activity (${resultCode}).")
            return
        }

        if (newFood == EditFoodActivity.invalidFoodElement){
            Log.d(TAG, "Received the invalid food element ${newFood.toString()}")
            return
        }

        foodList.add(newFood)
        Datasource(applicationContext).storeData(foodList.toList())
        binding.recyclerView.adapter?.notifyItemInserted(foodList.size-1)
    }
}