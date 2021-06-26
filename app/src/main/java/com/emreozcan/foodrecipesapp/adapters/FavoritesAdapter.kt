package com.emreozcan.foodrecipesapp.adapters

import android.app.Activity
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.data.database.entities.FavoriteEntity
import com.emreozcan.foodrecipesapp.databinding.FavoritesRecipesRowLayoutBinding
import com.emreozcan.foodrecipesapp.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.emreozcan.foodrecipesapp.util.RecipesDiffUtil
import com.emreozcan.foodrecipesapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoritesAdapter(private val requireActivity: FragmentActivity,private val mainViewModel: MainViewModel) :
    RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favoriteRecipes = emptyList<FavoriteEntity>()
    private lateinit var rootView : View
    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoriteEntity>()
    private var myViewHolderList = arrayListOf<MyViewHolder>()

    private lateinit var mActionMode: ActionMode

    class MyViewHolder(val binding: FavoritesRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteEntity: FavoriteEntity) {
            binding.favorite = favoriteEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FavoritesRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)
        myViewHolderList.add(holder)

        rootView = holder.itemView.rootView

        //Single Click
        holder.binding.favoriteCardView.setOnClickListener {
            if (multiSelection){
                applySelection(holder,currentRecipe)
            }else{
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }


        }

        //Long Click
        holder.binding.favoriteCardView.setOnLongClickListener {
            if (!multiSelection){
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder,currentRecipe)
                true
            }else{
                multiSelection = false
                false
            }

        }

    }

    private fun applySelection(holder: MyViewHolder,currentRecipe: FavoriteEntity){
        if (selectedRecipes.contains(currentRecipe)){
            selectedRecipes.remove(currentRecipe)
            changeCardStyle(holder,R.color.cardBackgroundColor,R.color.strokeColor)
            applyActionModeTitle()
        }else{
            selectedRecipes.add(currentRecipe)
            changeCardStyle(holder,R.color.cardBackgroundLightColor,R.color.purple_500)
            applyActionModeTitle()
        }
    }
    private fun changeCardStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int){
        holder.binding.favoriteCardView.setCardBackgroundColor(ContextCompat.getColor(requireActivity,backgroundColor))
        holder.binding.favoriteCardView.strokeColor = ContextCompat.getColor(requireActivity,strokeColor)
    }


    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun updateFavoritesList(newList: List<FavoriteEntity>) {
        val favoriteRecipesDiffUtil = RecipesDiffUtil(favoriteRecipes, newList)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)
        favoriteRecipes = newList
        diffUtilResult.dispatchUpdatesTo(this)
    }
    private fun applyActionModeTitle(){
        when(selectedRecipes.size){

            0 -> mActionMode.finish()

            1 -> mActionMode.title = "1 item selected"

            else -> mActionMode.title = "${selectedRecipes.size} items selected"

        }
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)
        applyStatusBarColor(R.color.contextualStatusBarColor)
        mActionMode = mode!!
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_delete){
            selectedRecipes.forEach {
                mainViewModel.deleteFavorite(it)
            }
            showSnackbar("${selectedRecipes.size} Recipe/s deleted")
            selectedRecipes.clear()
            multiSelection = false
            mActionMode.finish()
        }

        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolderList.forEach{
            changeCardStyle(it,R.color.cardBackgroundColor,R.color.strokeColor)
        }

        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }
    private fun applyStatusBarColor(color: Int){
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity,color)
    }

    private fun showSnackbar(message: String){
        Snackbar.make(rootView,message,Snackbar.LENGTH_LONG).setAction("OKAY"){}.show()
    }

    fun closeContextualActionMode(){
        if(this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
}