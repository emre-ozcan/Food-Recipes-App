package com.emreozcan.foodrecipesapp.models


import com.google.gson.annotations.SerializedName

data class FoodModel(
    @SerializedName("results")
    val results: List<Result>
)