package com.emreozcan.foodrecipesapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.emreozcan.foodrecipesapp.models.Result
import com.emreozcan.foodrecipesapp.util.Constants.Companion.FAVORITE_TABLE_NAME

@Entity(tableName = FAVORITE_TABLE_NAME)
class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val result: Result
)