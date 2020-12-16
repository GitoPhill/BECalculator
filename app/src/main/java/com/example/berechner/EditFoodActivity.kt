package com.example.berechner

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.berechner.databinding.ActivityNewFoodBinding
import com.example.berechner.model.Food
import com.example.berechner.model.Unit
import java.util.*

lateinit var newFood : Food

private const val TAG : String = "EditFoodActivity"

class EditFoodActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    companion object {
        const val NewFoodResultCode = 0
        val invalidFoodElement = Food("Invalid", Unit.g, 0, 0.0, 0.0)
    }


    lateinit var binding: ActivityNewFoodBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spinner: Spinner = binding.spinnerUnit
        ArrayAdapter.createFromResource(this,
            R.array.unit_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        spinner.onItemSelectedListener = this

        binding.buttonOk.setOnClickListener{ eval_input_and_return() }
        binding.editTextBEs.setOnEditorActionListener { v, actionId, event ->
            onEditorBEsAction(v, actionId, event)
        }
        binding.editTextCarbs.setOnEditorActionListener { v, actionId, event ->
            onEditorCarbsAction(v, actionId, event)
        }

        binding.editTextAmount.setText(invalidFoodElement.amount.toString())
        binding.editTextCarbs.setText(invalidFoodElement.carbs.toString())
        binding.editTextBEs.setText(invalidFoodElement.bread_unit.toString())
    }

    fun eval_input_and_return(){
        var unit : Unit
        val unitStringArray = resources.getStringArray(R.array.unit_array)
        if (binding.spinnerUnit.selectedItem == unitStringArray[0]){
            unit = Unit.g
        }else {
            unit = Unit.ml
        }

        newFood = Food(
            binding.editTextName.text.toString(),
            unit,
            binding.editTextAmount.text.toString().toInt(),
            binding.editTextCarbs.text.toString().toDouble(),
            binding.editTextBEs.text.toString().toDouble()
        )

        Log.d(TAG, "Food is ${newFood.toString()}")
        setResult(NewFoodResultCode)
        finish()
    }

    fun onEditorCarbsAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        val imm: InputMethodManager = v.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        binding.editTextCarbs.clearFocus()

        recalculateBreadUnits(binding.editTextCarbs.text.toString().toDouble())
        return true
    }

    fun onEditorBEsAction(v: TextView, actionId: Int, event: KeyEvent?): Boolean {
        val imm: InputMethodManager = v.context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.windowToken, 0)
        binding.editTextBEs.clearFocus()

        recalculateCarbs(binding.editTextBEs.text.toString().toDouble())
        return true
    }

    fun recalculateCarbs(breadUnits: Double){
        var carbs : Double = breadUnits * 12.0
        binding.editTextCarbs.setText(String.format(Locale.ENGLISH, "%1$.2f", carbs))
    }

    fun recalculateBreadUnits(carbs: Double) {
        var breadUnits: Double = carbs / 12.0
        binding.editTextBEs.setText(String.format(Locale.ENGLISH ,"%1$.2f", breadUnits))
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.d(TAG, "Currently selected is ${binding.spinnerUnit.selectedItem}")
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}