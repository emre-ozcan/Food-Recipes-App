package com.emreozcan.foodrecipesapp.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.adapters.IngredientsAdapter
import com.emreozcan.foodrecipesapp.databinding.FragmentIngredientsBinding
import com.emreozcan.foodrecipesapp.models.Result
import com.emreozcan.foodrecipesapp.util.Constants

class IngredientsFragment : Fragment() {

    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        val args = arguments
        val recipe: Result? = args?.getParcelable(Constants.RECIPE_BUNDLE)

        setupRecycler()
        recipe?.extendedIngredients?.let {
            mAdapter.setData(it)
        }


        return binding.root
    }

    private fun setupRecycler() {
        binding.rvIngredients.adapter = mAdapter
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}