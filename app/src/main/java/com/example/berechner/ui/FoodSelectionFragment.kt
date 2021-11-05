package com.example.berechner.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.berechner.R
import com.example.berechner.adapter.FoodSelectionAdapter
import com.example.berechner.data.Datasource
import com.example.berechner.databinding.FragmentFoodSelectionBinding
import com.example.berechner.model.Food
import com.example.berechner.model.MealComponent
import com.example.berechner.model.MealCompositionViewModel

private const val TAG = "FoodSelectionFragment"

lateinit var foodList : MutableList<Food>
lateinit var selection : MutableList<Food>

class FoodSelectionFragment: Fragment() {
    lateinit var binding: FragmentFoodSelectionBinding

    private val model: MealCompositionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentFoodSelectionBinding.inflate(inflater)

        // TODO: How to access the context in a more safe manner?
        foodList = context?.let { Datasource(it).loadFoodList().toMutableList() }!!

        val recyclerView = binding.recyclerView
        recyclerView.adapter = FoodSelectionAdapter(this)

        binding.floatingActionButtonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_foodSelectionFragment_to_editFoodFragment)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // val recyclerView = binding.recyclerView
        // recyclerView.adapter = FoodSelectionAdapter(this)

    }

    fun onSelected(food: Food) {
        Log.d(TAG, "onSelected ${food.name}")
        model.mealList.value?.add(MealComponent(food))
        findNavController().navigate(R.id.action_foodSelectionFragment_to_mealCompositionFragment)
    }
}