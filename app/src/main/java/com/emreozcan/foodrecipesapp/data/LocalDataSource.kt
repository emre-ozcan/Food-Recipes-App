package com.emreozcan.foodrecipesapp.data

import com.emreozcan.foodrecipesapp.data.database.RecipesDao
import com.emreozcan.foodrecipesapp.data.database.entities.FavoriteEntity
import com.emreozcan.foodrecipesapp.data.database.entities.FoodJokeEntity
import com.emreozcan.foodrecipesapp.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){

    //Recipes
    fun readRecipes(): Flow<List<RecipesEntity>>{
        return recipesDao.readAllRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }

    //Favorites
    fun readFavorites(): Flow<List<FavoriteEntity>>{
        return recipesDao.readFavorites()
    }

    suspend fun insertFavorite(favoriteEntity: FavoriteEntity){
        recipesDao.insertFavorite(favoriteEntity)
    }

    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity){
        recipesDao.deleteFavorite(favoriteEntity)
    }

    suspend fun deleteAllFavorites(){
        recipesDao.deleteAllFavorites()
    }

    //Food Joke
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>{
        return recipesDao.readFoodJoke()
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity){
        recipesDao.insertFoodJoke(foodJokeEntity)
    }


}