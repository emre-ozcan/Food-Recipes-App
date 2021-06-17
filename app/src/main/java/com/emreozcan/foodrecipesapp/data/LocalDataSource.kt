package com.emreozcan.foodrecipesapp.data

import com.emreozcan.foodrecipesapp.data.database.RecipesDao
import com.emreozcan.foodrecipesapp.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
){

    fun readDatabase(): Flow<List<RecipesEntity>>{
        return recipesDao.readAllRecipes()
    }

    suspend fun insertRecipes(recipesEntity: RecipesEntity){
        recipesDao.insertRecipes(recipesEntity)
    }


}