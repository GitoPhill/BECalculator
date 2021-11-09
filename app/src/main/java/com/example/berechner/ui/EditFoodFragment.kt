package com.example.berechner.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.berechner.R
import com.example.berechner.adapter.foodForEditing
import com.example.berechner.data.Datasource
import com.example.berechner.databinding.FragmentNewFoodBinding
import com.example.berechner.model.Food
import com.example.berechner.model.MealCompositionViewModel
import com.example.berechner.model.Unit
import java.util.*

private const val TAG = "EditFoodFragment"

class EditFoodFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var binding: FragmentNewFoodBinding

    private val model: MealCompositionViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView")

        binding = FragmentNewFoodBinding.inflate(inflater)

        val spinner: Spinner = binding.spinnerUnit
        ArrayAdapter.createFromResource( requireContext(),
            R.array.unit_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        binding.buttonOk.setOnClickListener{ evalInputAndReturn() }
        binding.editTextBEs.setOnEditorActionListener { v, actionId, event ->
            onEditorBEsAction(v, actionId, event)
        }
        binding.editTextCarbs.setOnEditorActionListener { v, actionId, event ->
            onEditorCarbsAction(v, actionId, event)
        }

        when (foodForEditing == null){
            true -> {
                newFood = Food(
                    EditFoodActivity.invalidFoodElement.name,
                    EditFoodActivity.invalidFoodElement.unit,
                    EditFoodActivity.invalidFoodElement.amount,
                    EditFoodActivity.invalidFoodElement.carbs,
                    EditFoodActivity.invalidFoodElement.bread_unit)
            }
            false -> {
                newFood = foodForEditing as Food
                binding.editTextName.setText(newFood.name)
            }
        }

        binding.editTextAmount.setText(newFood.amount.toString())
        binding.editTextCarbs.setText(newFood.carbs.toString())
        binding.editTextBEs.setText(newFood.bread_unit.toString())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun evalInputAndReturn(){
        var food = Food("", Unit.g, 0, 0.0, 0.0)

        Log.d(TAG, "evalInputAndReturn: foodForEditing = $foodForEditing")

        if (foodForEditing != null) {
            food = foodForEditing!!
        } else {
            model.foodList.value?.add(food)
        }

        Log.d(TAG, "evalInputAndReturn: foodlist size = ${model.foodList.value?.size}")


        val unitStringArray = resources.getStringArray(R.array.unit_array)
        if (binding.spinnerUnit.selectedItem == unitStringArray[0]){
            food.unit = Unit.g
        }else {
            food.unit = Unit.ml
        }

        food.name = binding.editTextName.text.toString()
        food.amount = binding.editTextAmount.text.toString().toInt()
        food.carbs = binding.editTextCarbs.text.toString().toDouble()
        food.bread_unit = binding.editTextBEs.text.toString().toDouble()

        Log.d(TAG, "Food is ${food.toString()}")

        Datasource(requireContext()).storeData(model.foodList.value!!)
        findNavController().navigate(R.id.action_editFoodFragment_to_foodSelectionFragment)
    }

    private fun onEditorCarbsAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        val imm: InputMethodManager = v.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        binding.editTextCarbs.clearFocus()

        recalculateBreadUnits(binding.editTextCarbs.text.toString().toDouble())
        return true
    }

    private fun onEditorBEsAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        val imm: InputMethodManager = v.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        binding.editTextBEs.clearFocus()

        recalculateCarbs(binding.editTextBEs.text.toString().toDouble())
        return true
    }

    private fun recalculateCarbs(breadUnits: Double){
        val carbs : Double = breadUnits * 12.0
        binding.editTextCarbs.setText(String.format(Locale.ENGLISH, "%1$.2f", carbs))
    }

    private fun recalculateBreadUnits(carbs: Double) {
        val breadUnits: Double = carbs / 12.0
        binding.editTextBEs.setText(String.format(Locale.ENGLISH ,"%1$.2f", breadUnits))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "onItemSelected")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}