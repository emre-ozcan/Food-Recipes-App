package com.emreozcan.foodrecipesapp.data

import com.emreozcan.foodrecipesapp.data.network.FoodRecipesApi
import com.emreozcan.foodrecipesapp.models.FoodJoke
import com.emreozcan.foodrecipesapp.models.FoodModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
){
    suspend fun getRecipes(queries: Map<String,String>): Response<FoodModel>{
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(queries: Map<String, String>) :Response<FoodModel>{
        return foodRecipesApi.getRecipes(queries)
    }

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke>{
        return foodRecipesApi.getFoodJoke(apiKey)
    }
}