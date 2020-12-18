package com.example.berechner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.berechner.FoodSelection
import com.example.berechner.R
import com.example.berechner.foodList
import com.example.berechner.selection

class FoodSelectionAdapter(private var foodSelection: FoodSelection)
    : RecyclerView.Adapter<FoodSelectionAdapter.ItemViewHolder>(){

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val foodEntry: TextView = view.findViewById(R.id.foodSelectionTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_selection_item, parent, false)

        val food_sel_item = FoodSelectionAdapter.ItemViewHolder(adapterLayout)

        food_sel_item.itemView.setOnClickListener{ handleOnItemClicked(food_sel_item)}

        return food_sel_item
    }

    override fun getItemCount() = foodList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.foodEntry.text = foodList[position].name
    }

    private fun handleOnItemClicked(holder: ItemViewHolder) {
        selection.add(foodList[holder.adapterPosition])

        foodSelection.setResult(FoodSelection.FoodSelectionResultCode);
        foodSelection.finish()
    }

}