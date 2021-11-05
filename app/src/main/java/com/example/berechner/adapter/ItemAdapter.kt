package com.example.berechner.adapter

import android.content.Context
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.berechner.R
import com.example.berechner.model.MealComponent

private const val TAG = "ItemAdapter"

interface ItemAdapterUpdateInterface {
    fun onItemUpdate(position: Int, amount: Int)
}

class ItemAdapter (private val context: Context,
                   private val itemAdapterUpdater: ItemAdapterUpdateInterface
) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){
//    override fun onItemUpdate(position: Int, amount: Int) {
//        Log.d(TAG, "dataset[" + position + "].amount = " + amount)
//        dataset[position].bread_unit = amount.toDouble()
//        dataset[position].bread_unit = BECalculator().calculate(dataset[position].food, amount)
//        dataset[position].amount = amount
//        notifyItemChanged(position)
//    }

    private var mealComponentList: MutableList<MealComponent> = mutableListOf()

    class ItemViewHolder(view: View, val adapterUpdateInterface: ItemAdapterUpdateInterface) : RecyclerView.ViewHolder(view){
        val item_name: TextView = view.findViewById(R.id.item_name)
        val item_amount: EditText = view.findViewById(R.id.item_amount)
        val item_result: TextView = view.findViewById(R.id.item_result)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder")
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)

        val itemView = ItemViewHolder(adapterLayout, itemAdapterUpdater)

        itemView.item_amount.setOnEditorActionListener(MyEditorActionListener(itemView))

        return itemView
    }

    override fun getItemCount() = mealComponentList.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = mealComponentList[position]
        holder.item_name.text = item.food.name
        holder.item_amount.setText(item.amount.toString())
        holder.item_result.text = String.format("%1$.1f BE", item.bread_unit)
        //holder.item_result.text = item.bread_unit.toString().format() + " BE"
        Log.d(TAG, "onBindViewHolder: name = ${holder.item_name.text}")
    }

    public fun setMealComponentList(list: MutableList<MealComponent>){
        Log.d(TAG, "setMealComponentList(list.size = ${mealComponentList.size})")
        mealComponentList = list
        Log.d(TAG, "setMealComponentList(list.size = ${mealComponentList.size})")
        notifyDataSetChanged()
    }

    public fun addMealComponent(item: MealComponent) {
        mealComponentList.add(item)
        notifyDataSetChanged()
    }

    class MyEditorActionListener(private val itemViewHolder: ItemViewHolder) : TextView.OnEditorActionListener {
        override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
            val imm: InputMethodManager = v.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)

            itemViewHolder.item_amount.clearFocus()

            if (v == null) {
                return false
            }

            if (itemViewHolder.adapterPosition == RecyclerView.NO_POSITION) {
                Log.d(TAG, "No adapter Position")
                return false
            }

            itemViewHolder.adapterUpdateInterface.onItemUpdate(
                itemViewHolder.adapterPosition,
                v.text.toString().toInt())

            return true
        }

    }
}