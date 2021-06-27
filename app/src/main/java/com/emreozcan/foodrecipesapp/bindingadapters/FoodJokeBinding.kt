package com.emreozcan.foodrecipesapp.bindingadapters

import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.emreozcan.foodrecipesapp.data.database.entities.FoodJokeEntity
import com.emreozcan.foodrecipesapp.models.FoodJoke
import com.emreozcan.foodrecipesapp.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {


    companion object {

        @BindingAdapter("foodJokeApiResponse","foodJokeReadDatabase",requireAll = false)
        @JvmStatic
        fun setCardandProgressVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when (apiResponse) {
                is NetworkResult.Loading -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.VISIBLE
                        is MaterialCardView -> view.visibility = View.INVISIBLE
                    }
                }
                is NetworkResult.Error -> {
                    when (view) {
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> {
                            if (!database.isNullOrEmpty()){
                                view.visibility = View.INVISIBLE
                            }
                            view.visibility = View.VISIBLE
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when(view){
                        is ProgressBar -> view.visibility = View.INVISIBLE
                        is MaterialCardView -> view.visibility = View.VISIBLE
                    }
                }
            }
        }
        @BindingAdapter("errorFoodJokeApiResponse","errorFoodJokeReadDatabase",requireAll = true)
        @JvmStatic
        fun setJokeErrorViewVisibility(view: View, apiResponse: NetworkResult<FoodJoke>?, database: List<FoodJokeEntity>?){
            if (apiResponse is NetworkResult.Success){
                view.visibility = View.INVISIBLE
            }
            if (database != null){
                if (database.isEmpty()){
                    view.visibility = View.VISIBLE
                    if (view is TextView){
                        if (apiResponse != null){
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }

        }
    }


}