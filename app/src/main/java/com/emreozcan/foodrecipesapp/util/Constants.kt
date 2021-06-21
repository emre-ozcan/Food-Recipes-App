package com.emreozcan.foodrecipesapp.util

class Constants {

    companion object{

        const val BASE_URL = "https://api.spoonacular.com"
        const val API_KEY ="a88afbf1edb14bd18aa137db7fca0405"
        const val IMAGE_BASE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"

        // API Query Keys
        const val QUERY_KEY= "query"
        const val QUERY_NUMBER = "number"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_RECIPE_INFORMATION = "addRecipeInformation"
        const val QUERY_INGREDIENTS = "fillIngredients"

        // Room
        const val RECIPES_TABLE_NAME = "recipestable"
        const val FAVORITE_TABLE_NAME= "favoritestable"

        // DataStore - Bottom Sheet
        const val DATASTORE_PREFERENCE_NAME = "food_recipes_preferences"
        const val DEFAULT_RECIPES_NUMBER = "30"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_DIET_TYPE = "gluten free"

        const val MEAL_TYPE_PREFERENCE_KEY = "mealType"
        const val MEAL_TYPE_ID_PREFERENCE_KEY = "mealTypeId"

        const val DIET_TYPE_PREFERENCE_KEY = "dietType"
        const val DIET_TYPE_ID_PREFERENCE_KEY = "dietTypeId"

        const val INTERNET_CONNECTION_KEY = "backOnline"

        //Bundle
        const val RECIPE_BUNDLE = "recipeBundle"


    }
}