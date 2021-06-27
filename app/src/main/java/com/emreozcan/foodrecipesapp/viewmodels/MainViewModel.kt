package com.emreozcan.foodrecipesapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.emreozcan.foodrecipesapp.data.Repository
import com.emreozcan.foodrecipesapp.data.database.entities.FavoriteEntity
import com.emreozcan.foodrecipesapp.data.database.entities.FoodJokeEntity
import com.emreozcan.foodrecipesapp.data.database.entities.RecipesEntity
import com.emreozcan.foodrecipesapp.models.FoodJoke
import com.emreozcan.foodrecipesapp.models.FoodModel
import com.emreozcan.foodrecipesapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application){

    //Room
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()
    val readFavorites: LiveData<List<FavoriteEntity>> = repository.local.readFavorites().asLiveData()
    val readFoodJoke: LiveData<List<FoodJokeEntity>> = repository.local.readFoodJoke().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }

    fun insertFavorite(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavorite(favoriteEntity)
        }

    fun deleteFavorite(favoriteEntity: FavoriteEntity) =
        viewModelScope.launch(Dispatchers.IO) {

            repository.local.deleteFavorite(favoriteEntity)
        }

    fun deleteAllFavorites() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavorites()
        }

    private fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.local.insertFoodJoke(foodJokeEntity)
    }


    //Retrofit
    var recipesResponse: MutableLiveData<NetworkResult<FoodModel>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodModel>> = MutableLiveData()
    var foodJokeResponse: MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries: Map<String,String>)= viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(queries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(queries)
    }

    fun getFoodJoke(apiKey: String) = viewModelScope.launch {
        getFoodSafeCall(apiKey)
    }

    private suspend fun getFoodSafeCall(apiKey: String) {
        foodJokeResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)

                val foodJoke = foodJokeResponse.value!!.data
                if (foodJoke != null) {
                    offlineCacheFoodJoke(foodJoke)
                }

            }catch (e: Exception){
                foodJokeResponse.value = NetworkResult.Error("Recipies could not found")
            }

        }else{
            foodJokeResponse.value = NetworkResult.Error("There Is Any Internet Connection!")
        }
    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }
            response.isSuccessful->{
                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)
            }
            else-> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                val foodRecipes = recipesResponse.value!!.data
                if (foodRecipes != null){
                    offlineCacheRecipes(foodRecipes)
                }

            }catch (e: Exception){
                recipesResponse.value = NetworkResult.Error("Recipies could not found")
            }

        }else{
            recipesResponse.value = NetworkResult.Error("There Is Any Internet Connection!")
        }
    }

    private suspend fun searchRecipesSafeCall(queries: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()){
            try {
                val response = repository.remote.searchRecipes(queries)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)


            }catch (e: Exception){
                searchedRecipesResponse.value = NetworkResult.Error("Recipies could not found")
            }

        }else{
            searchedRecipesResponse.value = NetworkResult.Error("There Is Any Internet Connection!")
        }
    }


    private fun offlineCacheRecipes(foodRecipes: FoodModel) {
        insertRecipes(RecipesEntity(foodRecipes))
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke){
        insertFoodJoke(FoodJokeEntity(foodJoke))
    }

    private fun handleFoodRecipesResponse(response: Response<FoodModel>): NetworkResult<FoodModel>? {
        when{
            response.message().toString().contains("timeout")->{
                return NetworkResult.Error("Timeout")
            }

            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited")
            }

            response.body()!!.results.isNullOrEmpty()->{
                return NetworkResult.Error("Recipes could not found")
            }

            response.isSuccessful->{
                val foodModel = response.body()
                return NetworkResult.Success(foodModel!!)
            }
            else-> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
            else->false
        }
    }


}