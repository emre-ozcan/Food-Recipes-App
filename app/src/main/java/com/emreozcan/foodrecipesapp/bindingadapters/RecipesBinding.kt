package com.emreozcan.foodrecipesapp.bindingadapters

import android.view.TextureView
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.emreozcan.foodrecipesapp.data.database.RecipesEntity
import com.emreozcan.foodrecipesapp.models.FoodModel
import com.emreozcan.foodrecipesapp.util.NetworkResult

class RecipesBinding {

    companion object{

        @BindingAdapter("readApiResponse","readDatabase",requireAll = true)
        @JvmStatic
        fun errorSituation(view: View, apiResponse: NetworkResult<FoodModel>?,database: List<RecipesEntity>?){
            if (apiResponse is NetworkResult.Error && database.isNullOrEmpty()){
                view.visibility = View.VISIBLE

                when (view) {
                    is TextView -> {
                        view.text = apiResponse.message.toString()
                    }
                }

            }else{
                view.visibility = View.INVISIBLE
            }
        }
    }



}