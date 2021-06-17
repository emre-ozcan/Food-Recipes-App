package com.emreozcan.foodrecipesapp.data.database

import androidx.room.TypeConverter
import com.emreozcan.foodrecipesapp.models.FoodModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun foodRecipesToString(foodModel: FoodModel): String{
        return gson.toJson(foodModel)
    }

    @TypeConverter
    fun stringToFoodRecipes(str: String): FoodModel{
        val list = object : TypeToken<FoodModel>() {}.type
        return gson.fromJson(str,list)
    }


}