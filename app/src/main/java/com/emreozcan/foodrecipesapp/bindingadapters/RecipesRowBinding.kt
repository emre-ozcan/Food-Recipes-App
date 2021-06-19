package com.emreozcan.foodrecipesapp.bindingadapters

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.models.Result
import com.emreozcan.foodrecipesapp.ui.fragments.home.HomeFragmentDirections
import com.emreozcan.foodrecipesapp.util.createPlaceHolder
import com.emreozcan.foodrecipesapp.util.loadCircleImage
import org.jsoup.Jsoup
import java.lang.Exception

class RecipesRowBinding {
    companion object{
        @BindingAdapter("recipeClickListener")
        @JvmStatic
        fun recipeRowClickListener(recipeRowLayout: ConstraintLayout, result: Result){
            recipeRowLayout.setOnClickListener {
                try {
                    val action = HomeFragmentDirections.actionHomeFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (e: Exception){
                    Log.d("RecipeRowClick",e.toString())
                }
            }
        }

        @BindingAdapter("applyVeganColor")
        @JvmStatic
        fun applyVeganColor(view: View, vegan: Boolean){
            if (vegan){
                when(view){
                    is TextView -> {
                        view.setTextColor(ContextCompat.getColor(view.context, R.color.green))
                    }
                    is ImageView ->{
                        view.setColorFilter(ContextCompat.getColor(view.context,R.color.green))
                    }
                }
            }
        }
        @BindingAdapter("loadCircleImage")
        @JvmStatic
        fun loadCircleImageFromUrl(imageView: ImageView,url: String){
            imageView.loadCircleImage(url, createPlaceHolder(imageView.context))
        }

        @BindingAdapter("parseHTML")
        @JvmStatic
        fun parseHTML(textView: TextView, str: String){
            if (str != null){
                val desc = Jsoup.parse(str).text()
                textView.text = desc
            }
        }
    }
}