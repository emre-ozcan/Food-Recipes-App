package com.emreozcan.foodrecipesapp.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emreozcan.foodrecipesapp.models.FoodJoke
import com.emreozcan.foodrecipesapp.util.Constants.Companion.FOOD_JOKE_TABLE_NAME

@Entity(tableName = FOOD_JOKE_TABLE_NAME)
data class FoodJokeEntity(
    @Embedded
    var foodJoke: FoodJoke
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}