package com.emreozcan.foodrecipesapp.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.foodrecipesapp.adapters.FavoritesAdapter
import com.emreozcan.foodrecipesapp.data.database.entities.FavoriteEntity

class FavoriteBinding {

    companion object{

        @BindingAdapter("favoritesList","setAdapter",requireAll = false)
        @JvmStatic
        fun viewVisibilityWithData(view: View, favoritesEntity: List<FavoriteEntity>?, mAdapter : FavoritesAdapter?){
            if (favoritesEntity.isNullOrEmpty()){
                when(view){
                    is ImageView -> view.visibility = View.VISIBLE
                    is TextView -> view.visibility = View.VISIBLE
                    is RecyclerView -> view.visibility = View.INVISIBLE
                }
            }else{
                when(view){
                    is ImageView -> view.visibility = View.INVISIBLE
                    is TextView -> view.visibility = View.INVISIBLE
                    is RecyclerView ->{
                        view.visibility = View.VISIBLE
                        mAdapter?.updateFavoritesList(favoritesEntity)
                    }
                }
            }
        }

    }
}