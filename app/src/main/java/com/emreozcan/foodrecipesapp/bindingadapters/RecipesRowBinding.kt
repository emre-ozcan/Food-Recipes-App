package com.emreozcan.foodrecipesapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.util.createPlaceHolder
import com.emreozcan.foodrecipesapp.util.loadCircleImage

class RecipesRowBinding {
    companion object{

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
    }
}