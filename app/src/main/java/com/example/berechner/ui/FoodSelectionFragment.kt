package com.example.berechner.ui

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.JsonReader
import android.util.JsonWriter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.berechner.R
import com.example.berechner.adapter.FoodSelectionAdapter
import com.example.berechner.databinding.FragmentFoodSelectionBinding
import com.example.berechner.model.Food
import com.example.berechner.model.MealComponent
import com.example.berechner.model.MealCompositionViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.StringWriter

private const val TAG = "FoodSelectionFragment"

const val CREATE_FOOD_DB_JSON = 1
const val READ_FOOD_DB_JSON = 2

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

        binding.floatingActionButtonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_foodSelectionFragment_to_editFoodFragment)
        }

        binding.floatingActionButtonSave.setOnClickListener{
            Log.d(TAG, "Trying to save list...")
            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            startActivityForResult(intent, CREATE_FOOD_DB_JSON)
        }

        binding.floatingActionButtonRead.setOnClickListener {
            Log.d(TAG, "Reading from file...")
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            startActivityForResult(intent, READ_FOOD_DB_JSON)
        }

        binding.editTextTextFoodSearch.addTextChangedListener(textWatcher)

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            Log.d(TAG, "afterTextChanged: $s")
            model.filterFoodList(s.toString())
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated")

        var recyclerView = binding.recyclerView
        var adapter = FoodSelectionAdapter(this)
        recyclerView.adapter = adapter

        model.foodList.observe(viewLifecycleOwner) { list ->
            Log.d(TAG, "observed food list update (size ${list.size}")
            adapter.setFoodList(list)
        }

        model.filterFoodList("")
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode) {
            CREATE_FOOD_DB_JSON -> {
                Log.d(TAG, "onActivityResult: selected ${data?.data?.toString()}")
                data?.data?.also { uri ->
                    requireContext().contentResolver.openFileDescriptor(uri, "w")?.use { fileDescriptor ->
                        FileOutputStream(fileDescriptor.fileDescriptor).use {
                            var result = StringWriter()
                            var writer = JsonWriter(result)
                            writer.beginArray()
                            for (item in model.foodList.value!!) {
                                item.writeJson(writer)
                                result.appendLine()
                            }
                            writer.endArray()
                            writer.close()
                            it.write((result.toString()).toByteArray())
                        }
                    }
                }
            }
            READ_FOOD_DB_JSON -> {
                Log.d(TAG, "onActivityResult: Reading file ${data?.data?.toString()}")

                data?.data?.also { uri ->
                    val fd = requireContext().contentResolver.openFileDescriptor(uri, "r")
                    val inputStream = FileInputStream(fd!!.fileDescriptor)
                    val jsonReader = JsonReader(inputStream.reader())
                    try {
                        model.loadFromFile(jsonReader)
                    } catch (e: Exception) {
                        val info = Snackbar.make(requireView(), R.string.snackbar_parse_error, Snackbar.LENGTH_SHORT)
                        info.show()
                    }
                }

                Log.d(TAG, "binding.recyclerView.adapter = ${binding.recyclerView.adapter}")
                if (binding.recyclerView.adapter != null) {
                    binding.recyclerView.adapter!!.notifyDataSetChanged()
                }
            }
            else -> Log.d(TAG,"onActivityResult: Unexpected request code ($requestCode)")
        }
    }

    fun onSelected(food: Food) {
        Log.d(TAG, "onSelected ${food.name}")
        model.mealList.value?.add(MealComponent(food))
        findNavController().navigate(R.id.action_foodSelectionFragment_to_mealCompositionFragment)
    }
}