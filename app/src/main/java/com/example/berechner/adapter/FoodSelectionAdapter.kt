package com.example.berechner.adapter

import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.berechner.R
import com.example.berechner.data.Datasource
import com.example.berechner.model.Food
import com.example.berechner.model.MealCompositionViewModel
import com.example.berechner.ui.FoodSelectionFragment

private const val TAG = "FoodSelectionAdapter"

var foodForEditing: Food? = null

class FoodSelectionAdapter(private var foodSelection: FoodSelectionFragment)
    : RecyclerView.Adapter<FoodSelectionAdapter.ItemViewHolder>(){

    private val model: MealCompositionViewModel by foodSelection.activityViewModels()
    private var foodList: MutableList<Food> = mutableListOf()

    init {
        foodForEditing = null
    }

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

    override fun getItemCount(): Int {
//        if (model.foodList.value == null) {
//            return 0
//        }

        return foodList.size//model.foodList.value!!.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val food = foodList[position]//model.foodList.value!![position]
        holder.itemName.text = food.name
        holder.itemInfo.text = String.format("%d %s -> %.1f g KH -> %.1f BE",
                                             food.amount,
                                             food.unit,
                                             food.carbs,
                                             food.bread_unit)
    }

    private fun handleOnItemClicked(holder: ItemViewHolder) {
        Log.d(TAG, "handleOnItemClicked ${holder.adapterPosition} foodList size = $itemCount")
        //selection.add(foodList[holder.adapterPosition])

        foodSelection.onSelected(foodList[holder.adapterPosition])//model.foodList.value!![holder.adapterPosition])

        //foodSelection.onSelected(foodList[holder.adapterPosition])
    }

    public fun setFoodList(list: MutableList<Food>){
        Log.d(TAG, "setFoodList(list.size = ${foodList.size})")
        foodList = list
        Log.d(TAG, "setFoodList(list.size = ${foodList.size})")
        notifyDataSetChanged()
    }


    private var actionMode: ActionMode? = null
    private var selectedItem: ItemViewHolder? = null
    private fun handleOnLongClick(v: ItemViewHolder): Boolean {
        Log.d("TAG", "handleOnLongClick(pos: ${v.adapterPosition})")
        return when(actionMode) {
            null -> {
                //actionMode = foodSelection?.startActionMode(actionModeCallback)
                actionMode = foodSelection.activity?.startActionMode(actionModeCallback)
                v.itemView.isSelected = true
                selectedItem = v
                true
            }
            else -> false
        }
    }

    private val actionModeCallback = object : ActionMode.Callback {
        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
            val inflator: MenuInflater = mode.menuInflater
            inflator.inflate(R.menu.foodselection_context_menu, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.edit_food_item -> {
                    Log.d(TAG, "Edit Food Item")

                    foodForEditing = foodList[selectedItem!!.adapterPosition]
                    foodSelection.findNavController().navigate(R.id.action_foodSelectionFragment_to_editFoodFragment)
                    mode.finish()
//                    val intent = Intent(foodSelection, EditFoodActivity::class.java)
//                    foodSelection.startActivityForResult(intent, EditFoodActivity.NewFoodResultCode)
                    true
                }
                R.id.delete_food_item -> {
                    foodList.removeAt(selectedItem!!.adapterPosition)
                    notifyItemRemoved(selectedItem!!.adapterPosition)
                    mode.finish()
                    Datasource(foodSelection.requireContext()).storeData(model.foodList.value!!)

//                    Datasource(foodSelection.applicationContext).storeData(foodList.toList())
                    true
                }
                else -> false
            }
        }

        override fun onDestroyActionMode(mode: ActionMode) {
            actionMode = null
        }

    }
}