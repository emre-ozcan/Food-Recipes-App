package com.emreozcan.foodrecipesapp.data.network

import com.emreozcan.foodrecipesapp.models.FoodJoke
import com.emreozcan.foodrecipesapp.models.FoodModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipesApi {
    //https://api.spoonacular.com/recipes/complexSearch?number=1&apiKey=a88afbf1edb14bd18aa137db7fca0405&type=finger%20food&diet=vegan&addRecipeInformation=true&fillIngredients=true

    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String,String>): Response<FoodModel>

    @GET("/recipes/complexSearch")
    suspend fun searchRecipes(@QueryMap searchQuery: Map<String,String>) : Response<FoodModel>

    @GET("food/jokes/random")
    suspend fun getFoodJoke(@Query("apiKey") apiKey: String): Response<FoodJoke>
}