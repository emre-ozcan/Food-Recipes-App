package com.emreozcan.foodrecipesapp.data.database

import androidx.room.*
import com.emreozcan.foodrecipesapp.data.database.entities.FavoriteEntity
import com.emreozcan.foodrecipesapp.data.database.entities.FoodJokeEntity
import com.emreozcan.foodrecipesapp.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow




@Dao
interface RecipesDao {

    //Recipes
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Query("SELECT * FROM recipestable ORDER BY id ASC")
    fun readAllRecipes(): Flow<List<RecipesEntity>>

    //Favorites
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favoritestable ORDER BY id ASC")
    fun readFavorites(): Flow<List<FavoriteEntity>>

    @Delete
    suspend fun deleteFavorite(favoriteEntity: FavoriteEntity)

    @Query("DELETE FROM favoritestable")
    suspend fun deleteAllFavorites()

    //Food Joke
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM foodjoketable ORDER BY id ASC")
    fun readFoodJoke(): Flow<List<FoodJokeEntity>>

}