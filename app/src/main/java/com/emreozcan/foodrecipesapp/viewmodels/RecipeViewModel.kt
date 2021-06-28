package com.emreozcan.foodrecipesapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.emreozcan.foodrecipesapp.data.DataStoreRepository
import com.emreozcan.foodrecipesapp.data.MealAndDietType
import com.emreozcan.foodrecipesapp.util.Constants
import com.emreozcan.foodrecipesapp.util.Constants.Companion.API_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.emreozcan.foodrecipesapp.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_API_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_DIET
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_INGREDIENTS
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_KEY
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_NUMBER
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_RECIPE_INFORMATION
import com.emreozcan.foodrecipesapp.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    application: Application
) : AndroidViewModel(application) {

    private lateinit var mealAndDietType: MealAndDietType

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    private fun saveBackOnline(online: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(online)
        }
    }

    fun saveMealAndDietType() =
        viewModelScope.launch(Dispatchers.IO) {
            if (this@RecipeViewModel::mealAndDietType.isInitialized){
                dataStoreRepository.saveMealAndDietType(
                    mealAndDietType.selectedMealType,
                    mealAndDietType.selectedMealTypeId,
                    mealAndDietType.selectedDietType,
                    mealAndDietType.selectedDietTypeId
                )
            }
        }

    fun saveMealAndDietTypeTemp(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        mealAndDietType = MealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
    }


    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()


        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_RECIPE_INFORMATION] = "true"
        queries[QUERY_INGREDIENTS] = "true"

        if (this@RecipeViewModel::mealAndDietType.isInitialized) {

            queries[QUERY_TYPE] = mealAndDietType.selectedMealType
            queries[QUERY_DIET] = mealAndDietType.selectedDietType

        } else {
            queries[QUERY_TYPE] = DEFAULT_MEAL_TYPE
            queries[QUERY_DIET] = DEFAULT_DIET_TYPE
        }

        return queries
    }

    fun searchQueries(query: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_KEY] = query
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_RECIPE_INFORMATION] = "true"
        queries[QUERY_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection!", Toast.LENGTH_LONG).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "We are back online !", Toast.LENGTH_LONG).show()
                saveBackOnline(false)
            }
        }
    }
}