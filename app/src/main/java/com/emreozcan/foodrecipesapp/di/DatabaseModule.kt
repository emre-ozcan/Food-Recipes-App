package com.emreozcan.foodrecipesapp.di

import android.content.Context
import androidx.room.Room
import com.emreozcan.foodrecipesapp.data.database.RecipesDatabase
import com.emreozcan.foodrecipesapp.util.Constants.Companion.RECIPES_TABLE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,RecipesDatabase::class.java,RECIPES_TABLE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDao(database: RecipesDatabase) = database.recipesDao()

}