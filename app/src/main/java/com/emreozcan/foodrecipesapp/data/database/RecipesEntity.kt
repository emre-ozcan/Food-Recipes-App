package com.emreozcan.foodrecipesapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emreozcan.foodrecipesapp.models.FoodModel
import com.emreozcan.foodrecipesapp.util.Constants.Companion.RECIPES_TABLE_NAME

@Entity(tableName = RECIPES_TABLE_NAME)
class RecipesEntity(
    var foodModel: FoodModel
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0;
}