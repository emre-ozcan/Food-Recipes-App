package com.emreozcan.foodrecipesapp.adapters

import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.foodrecipesapp.databinding.RecyclerRowLayoutBinding
import com.emreozcan.foodrecipesapp.models.FoodModel
import com.emreozcan.foodrecipesapp.models.Result
import com.emreozcan.foodrecipesapp.util.RecipesDiffUtil

class RecipesAdapter : RecyclerView.Adapter<RecipesAdapter.MViewHolder>() {

    private var recipes = emptyList<Result>()

    class MViewHolder(private val binding: RecyclerRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.result = result
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RecyclerRowLayoutBinding.inflate(layoutInflater,parent,false)
                return MViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MViewHolder {
        return MViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    fun setData(data: FoodModel){
        val recipesDiffUtil = RecipesDiffUtil(recipes,data.results)
        val diffResult = DiffUtil.calculateDiff(recipesDiffUtil)
        recipes = data.results
        diffResult.dispatchUpdatesTo(this)
    }
}