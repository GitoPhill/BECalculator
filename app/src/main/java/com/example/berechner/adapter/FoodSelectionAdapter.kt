package com.example.berechner.adapter

import android.util.Log
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
        val itemName: TextView = view.findViewById(R.id.textViewCardFoodName)
        val itemInfo: TextView = view.findViewById(R.id.textViewCardFoodInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_selection_item, parent, false)

        val food_sel_item = FoodSelectionAdapter.ItemViewHolder(adapterLayout)

        food_sel_item.itemView.setOnClickListener{ handleOnItemClicked(food_sel_item)}
        food_sel_item.itemView.setOnLongClickListener { handleOnLongClick(food_sel_item) }

        return food_sel_item
    }

    override fun getItemCount() = foodList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemName.text = foodList[position].name
        holder.itemInfo.text = String.format("%d %s -> %.1f BE"/* %2$'s' -> %3$'f' BE"*/,
                                             foodList[position].amount,
                                             foodList[position].unit,
                                             foodList[position].bread_unit)
    }

    private fun handleOnItemClicked(holder: ItemViewHolder) {
        selection.add(foodList[holder.adapterPosition])

        foodSelection.setResult(FoodSelection.FoodSelectionResultCode);
        foodSelection.finish()
    }

    private fun handleOnLongClick(v: ItemViewHolder): Boolean {
        Log.d("TAG" , "Long Clicked!!")
        return true
    }
}