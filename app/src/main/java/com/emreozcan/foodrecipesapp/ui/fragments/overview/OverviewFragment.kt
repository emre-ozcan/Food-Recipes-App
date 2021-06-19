package com.emreozcan.foodrecipesapp.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.emreozcan.foodrecipesapp.R

import com.emreozcan.foodrecipesapp.databinding.FragmentOverviewBinding
import com.emreozcan.foodrecipesapp.models.Result
import com.emreozcan.foodrecipesapp.util.Constants.Companion.RECIPE_BUNDLE
import org.jsoup.Jsoup


class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater,container,false)

        val args = arguments
        val recipe: Result? = args?.getParcelable(RECIPE_BUNDLE)

        binding.recipeImageDtl.load(recipe?.image)

        binding.recipeTitle.text = recipe?.title
        binding.likesTextView.text = recipe?.aggregateLikes.toString()
        binding.timeTextView.text = recipe?.readyInMinutes.toString()

        recipe?.summary?.let {
            binding.summaryTextView.text = Jsoup.parse(it).text()
        }

        if (recipe?.vegetarian==true){
            binding.vegetarianTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.vegetarianImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if (recipe?.vegan==true){
            binding.veganTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.veganImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if (recipe?.glutenFree==true){
            binding.glutenTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.glutenFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if (recipe?.dairyFree==true){
            binding.dairyTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.dairyFreeImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if (recipe?.veryHealthy==true){
            binding.healthyTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.healthyImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if (recipe?.cheap==true){
            binding.cheapTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
            binding.cheapImageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}