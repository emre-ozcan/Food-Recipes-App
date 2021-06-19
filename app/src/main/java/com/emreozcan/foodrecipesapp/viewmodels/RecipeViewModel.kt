package com.emreozcan.foodrecipesapp.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.emreozcan.foodrecipesapp.data.DataStoreRepository
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

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveBackOnline(online: Boolean){
        viewModelScope.launch(Dispatchers.IO){
            dataStoreRepository.saveBackOnline(online)
        }
    }

    fun saveMealAndDietType(mealType: String, mealTypeId: Int,dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveMealAndDietType(mealType,mealTypeId,dietType,dietTypeId)
        }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            readMealAndDietType.collect { value->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
        queries[QUERY_DIET] = dietType
        queries[QUERY_RECIPE_INFORMATION] = "true"
        queries[QUERY_INGREDIENTS] = "true"

        return queries
    }
    fun searchQueries(query: String): HashMap<String, String>{
        val queries: HashMap<String, String> = HashMap()

        queries[QUERY_KEY] = query
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_RECIPE_INFORMATION] = "true"
        queries[QUERY_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus(){
        if (!networkStatus){
            Toast.makeText(getApplication(),"No Internet Connection!",Toast.LENGTH_LONG).show()
            saveBackOnline(true)
        }else if (networkStatus){
            if (backOnline){
                Toast.makeText(getApplication(),"We are back online !",Toast.LENGTH_LONG).show()
                saveBackOnline(false)
            }
        }
    }
}