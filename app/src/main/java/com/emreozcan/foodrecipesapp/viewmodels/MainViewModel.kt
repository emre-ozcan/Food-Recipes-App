package com.emreozcan.foodrecipesapp.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.*
import com.emreozcan.foodrecipesapp.data.Repository
import com.emreozcan.foodrecipesapp.data.database.RecipesEntity
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
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readDatabase().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertRecipes(recipesEntity)
        }


    //Retrofit
    var recipesResponse: MutableLiveData<NetworkResult<FoodModel>> = MutableLiveData()
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodModel>> = MutableLiveData()

    fun getRecipes(queries: Map<String,String>)= viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(queries: Map<String, String>) = viewModelScope.launch {
        searchRecipesSafeCall(queries)
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