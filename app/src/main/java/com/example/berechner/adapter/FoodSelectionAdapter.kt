package com.example.berechner.adapter

import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.berechner.R
import com.example.berechner.model.Food
import com.example.berechner.ui.FoodSelectionFragment
import com.example.berechner.ui.foodList
import com.example.berechner.ui.selection

private const val TAG = "FoodSelectionAdapter"

var foodForEditing: Food? = null

class FoodSelectionAdapter(private var foodSelection: FoodSelectionFragment)
    : RecyclerView.Adapter<FoodSelectionAdapter.ItemViewHolder>(){

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
        //food_sel_item.itemView.setOnLongClickListener { handleOnLongClick(food_sel_item) }

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

        //foodSelection.setResult(FoodSelection.FoodSelectionResultCode);
        //foodSelection.finish()
    }

    private var actionMode: ActionMode? = null
    private var selectedItem: ItemViewHolder? = null
//    private fun handleOnLongClick(v: ItemViewHolder): Boolean {
//        Log.d("TAG", "Long Clicked!!")
//        return when(actionMode) {
//            null -> {
//                actionMode = foodSelection?.startActionMode(actionModeCallback)
//                v.itemView.isSelected = true
//                selectedItem = v
//                true
//            }
//            else -> false
//        }
//    }

//    private val actionModeCallback = object : ActionMode.Callback {
//        override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
//            val inflator: MenuInflater = mode.menuInflater
//            inflator.inflate(R.menu.foodselection_context_menu, menu)
//            return true
//        }
//
//        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
//            return false
//        }
//
////        override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
////            return when (item.itemId) {
////                R.id.edit_food_item -> {
////                    Log.d(TAG, "Edit Food Item")
////
////                    foodForEditing = foodList[selectedItem!!.adapterPosition]
////                    val intent = Intent(foodSelection, EditFoodActivity::class.java)
////                    foodSelection.startActivityForResult(intent, EditFoodActivity.NewFoodResultCode)
////                    mode.finish()
////                    true
////                }
////                R.id.delete_food_item -> {
////                    foodList.removeAt(selectedItem!!.adapterPosition)
////                    notifyItemRemoved(selectedItem!!.adapterPosition)
////                    Datasource(foodSelection.applicationContext).storeData(foodList.toList())
////                    mode.finish()
////                    true
////                }
////                else -> false
////            }
////        }
//
//        override fun onDestroyActionMode(mode: ActionMode) {
//            actionMode = null
//        }
//
//    }
}