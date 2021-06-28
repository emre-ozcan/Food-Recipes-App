package com.emreozcan.foodrecipesapp.ui.fragments.overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
        val recipe: Result = args!!.getParcelable<Result>(RECIPE_BUNDLE) as Result

        binding.recipeImageDtl.load(recipe.image)

        binding.recipeTitle.text = recipe.title
        binding.likesTextView.text = recipe.aggregateLikes.toString()
        binding.timeTextView.text = recipe.readyInMinutes.toString()

        recipe.summary.let {
            binding.summaryTextView.text = Jsoup.parse(it).text()
        }

        updateColors(recipe.vegetarian,binding.vegetarianTextView,binding.vegetarianImageView)
        updateColors(recipe.vegan,binding.veganTextView,binding.veganImageView)
        updateColors(recipe.cheap,binding.cheapTextView,binding.cheapImageView)
        updateColors(recipe.dairyFree,binding.dairyTextView,binding.dairyFreeImageView)
        updateColors(recipe.glutenFree,binding.glutenTextView,binding.glutenFreeImageView)
        updateColors(recipe.veryHealthy,binding.healthyTextView,binding.healthyImageView)


        return binding.root
    }
    private fun updateColors(bool: Boolean,textView: TextView,imageView: ImageView){
        if (bool){
            imageView.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            textView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}