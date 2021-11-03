package com.example.berechner.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.berechner.R
import com.example.berechner.adapter.ItemAdapter
import com.example.berechner.adapter.ItemAdapterUpdateInterface
import com.example.berechner.databinding.FragmentMealCompositionBinding
import com.example.berechner.model.MealCompositionViewModel

private const val TAG = "MealCompositionFragment"


class MealCompositionFragment: Fragment(),
    ItemAdapterUpdateInterface {
    lateinit var binding: FragmentMealCompositionBinding
    private val viewModel: MealCompositionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        binding = FragmentMealCompositionBinding.inflate(inflater)
        return binding.root
        //return inflater.inflate(R.layout.fragment_meal_composition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        //binding = FragmentMealCompositionBinding.inflate(layoutInflater)
        var recyclerView = binding.recyclerView
        recyclerView.setHasFixedSize(true)
        var adapter: ItemAdapter = ItemAdapter(requireContext(), this)
        recyclerView.adapter = adapter
        binding.textViewTotal.text = getString(R.string.totelBreadUnitHint)

        viewModel.mealList.observe(viewLifecycleOwner)  { list ->
            Log.d(TAG, "observer mealList size is ${list.size}")

            adapter.setMealComponentList(list)
            adapter.notifyDataSetChanged()
        }

        binding.floatingActionButtonClear.setOnClickListener{
            viewModel.clearMealComponentList()
        }

        binding.floatingActionButtonAdd.setOnClickListener{
            findNavController().navigate(R.id.action_mealCompositionFragment_to_foodSelectionFragment)
        }

    }

    override fun onItemUpdate(position: Int, amount: Int) {
        Log.d(TAG, "onItemUpdate(pos = ${position}, amount = ${amount})")
        viewModel.updateMealComponent(position, amount)
//        binding.recyclerView.adapter?.notifyItemChanged(position)
//        viewModel.mealList.value!![position].bread_unit = amount.toDouble()
//        viewModel.mealList.value!![position].amount = amount
//        binding.recyclerView.adapter?.notifyItemChanged(position)
    //    updateResult()
    }

//
//    fun storeData() {
//        Log.d(TAG, "Store data:")
//
//        var result = StringWriter()
//        var writer = JsonWriter(result)
//        writer.setIndent("  ")
//
//        writer.beginArray()
//        for (data in mealComponentList) {
//            data.food.writeJson(writer)
//        }
//        writer.endArray()
//        writer.close()
//
//        Log.d( TAG, result.toString())
//
//        val file = File(requireContext().filesDir, "foodDB.json")
//        file.writeText(result.toString())
//    }

}