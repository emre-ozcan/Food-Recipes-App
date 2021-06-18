package com.emreozcan.foodrecipesapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DATASTORE_PREFERENCE_NAME
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DIET_TYPE_PREFERENCE_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DIET_TYPE_ID_PREFERENCE_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.MEAL_TYPE_ID_PREFERENCE_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.MEAL_TYPE_PREFERENCE_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys{

        val selectedMealType = stringPreferencesKey(MEAL_TYPE_PREFERENCE_KEY)
        val selectedMealTypeId = intPreferencesKey(MEAL_TYPE_ID_PREFERENCE_KEY)

        val selectedDietType = stringPreferencesKey(DIET_TYPE_PREFERENCE_KEY)
        val selectedDietTypeId = intPreferencesKey(DIET_TYPE_ID_PREFERENCE_KEY)

    }

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_PREFERENCE_NAME)

    suspend fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int){
        context.dataStore.edit { preferences->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId

        }
    }

    val readMealAndDietType: Flow<MealAndDietType> = context.dataStore.data
        .catch { exception->
            if (exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }

        }.map {  preferences->

            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0

            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

            MealAndDietType(selectedMealType,selectedMealTypeId,selectedDietType, selectedDietTypeId)
        }


}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)