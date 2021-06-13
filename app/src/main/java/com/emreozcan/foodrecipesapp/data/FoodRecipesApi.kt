package com.emreozcan.foodrecipesapp.data

import com.emreozcan.foodrecipesapp.models.FoodModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface FoodRecipesApi {
    //https://api.spoonacular.com/recipes/complexSearch?number=1&apiKey=a88afbf1edb14bd18aa137db7fca0405&type=finger%20food&diet=vegan&addRecipeInformation=true&fillIngredients=true
    @GET("/recipes/complexSearch")
    suspend fun getRecipes(@QueryMap queries: Map<String,String>): Response<FoodModel>

}