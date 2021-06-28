package com.emreozcan.foodrecipesapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.foodrecipesapp.adapters.FavoritesAdapter
import com.emreozcan.foodrecipesapp.data.database.entities.FavoriteEntity

class FavoriteBinding {

    companion object{

        @BindingAdapter("favoritesList","setAdapter",requireAll = false)
        @JvmStatic
        fun viewVisibilityWithData(view: View, favoritesEntity: List<FavoriteEntity>?, mAdapter : FavoritesAdapter?){
            val dataCheck = favoritesEntity.isNullOrEmpty()
            view.isVisible = dataCheck
            if(view is RecyclerView && !dataCheck){
                view.visibility = View.VISIBLE
                favoritesEntity?.let {
                    mAdapter?.updateFavoritesList(it)
                }
            }
        }

    }
}