package com.emreozcan.foodrecipesapp.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.emreozcan.foodrecipesapp.util.Constants
import com.emreozcan.foodrecipesapp.util.Constants.Companion.API_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_API_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_DIET
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_INGREDIENTS
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_NUMBER
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_RECIPE_INFORMATION
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_TYPE

class RecipeViewModel(application: Application): AndroidViewModel(application) {

    fun applyQueries(): HashMap<String, String> {
        val queries : HashMap<String,String> = HashMap()

        queries[QUERY_NUMBER] = "50"
        queries[QUERY_API_KEY]= API_KEY
        queries[QUERY_TYPE]= "snack"
        queries[QUERY_DIET]="vegan"
        queries[QUERY_RECIPE_INFORMATION] = "true"
        queries[QUERY_INGREDIENTS] = "true"


        return queries
    }
}