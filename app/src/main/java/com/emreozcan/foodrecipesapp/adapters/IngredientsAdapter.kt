package com.emreozcan.foodrecipesapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.databinding.IngredientsRowLayoutBinding
import com.emreozcan.foodrecipesapp.models.ExtendedIngredient
import com.emreozcan.foodrecipesapp.util.Constants.Companion.IMAGE_BASE_URL
import com.emreozcan.foodrecipesapp.util.RecipesDiffUtil
import java.util.*

class IngredientsAdapter: RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>(){

    private var ingredientsList = emptyList<ExtendedIngredient>()

    class MyViewHolder(val binding: IngredientsRowLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(IngredientsRowLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val ingredient = ingredientsList[position]
        holder.binding.ingredientImage.load(IMAGE_BASE_URL + ingredient.image){
            crossfade(600)
            error(R.drawable.ic_placeholder)
        }

        holder.binding.ingredientsName.text = ingredient.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        holder.binding.ingredientsAmountUnit.setText("${ingredient.amount} ${ingredient.unit}")
        holder.binding.ingredientsConsistency.text = ingredient.consistency
        holder.binding.ingredientsOriginal.text = ingredient.original

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredientsList: List<ExtendedIngredient>){
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList,newIngredientsList)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredientsList
        diffUtilResult.dispatchUpdatesTo(this)
    }
}