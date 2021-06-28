package com.emreozcan.foodrecipesapp.ui.fragments.home.bottomsheet

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.databinding.FragmentRecipesBottomSheetBinding
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.emreozcan.foodrecipesapp.viewmodels.RecipeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.lang.Exception
import java.util.*

class RecipesBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentRecipesBottomSheetBinding? = null
    private val binding get() = _binding!!

    private lateinit var recipeViewModel: RecipeViewModel

    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private var dietTypeChip = DEFAULT_DIET_TYPE
    private var dietTypeChipId= 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        recipeViewModel= ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecipesBottomSheetBinding.inflate(inflater,container,false)

        recipeViewModel.readMealAndDietType.asLiveData().observe(viewLifecycleOwner,{ value ->
            mealTypeChip = value.selectedMealType
            dietTypeChip = value.selectedDietType

            Log.e("Chip",value.selectedDietTypeId.toString())
            Log.e("Chip",value.selectedMealTypeId.toString())

            updateChip(value.selectedMealTypeId,binding.mealTypeChipGroup)
            updateChip(value.selectedDietTypeId,binding.dietTypeChipGroup)
        })

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString().lowercase(Locale.ROOT)

            mealTypeChip = selectedMealType
            mealTypeChipId = checkedId

        }

        binding.dietTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedDietType = chip.text.toString().lowercase(Locale.ROOT)

            dietTypeChip = selectedDietType
            dietTypeChipId = checkedId
        }

        binding.buttonApply.setOnClickListener {
            recipeViewModel.saveMealAndDietTypeTemp(mealTypeChip,mealTypeChipId,dietTypeChip,dietTypeChipId)
            val action = RecipesBottomSheetDirections.actionRecipesBottomSheetToHomeFragment(true)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun updateChip(selectedTypeId: Int, chipGroup: ChipGroup) {
        if (selectedTypeId != 0){
            try {
                val targetView = chipGroup.findViewById<Chip>(selectedTypeId)
                targetView.isChecked = true
                chipGroup.requestChildFocus(targetView,targetView)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}