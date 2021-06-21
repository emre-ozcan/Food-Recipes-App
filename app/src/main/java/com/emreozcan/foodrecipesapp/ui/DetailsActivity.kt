package com.emreozcan.foodrecipesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.adapters.PagerAdapter
import com.emreozcan.foodrecipesapp.data.database.entities.FavoriteEntity
import com.emreozcan.foodrecipesapp.databinding.ActivityDetailsBinding
import com.emreozcan.foodrecipesapp.ui.fragments.ingredients.IngredientsFragment
import com.emreozcan.foodrecipesapp.ui.fragments.instructions.InstructionsFragment
import com.emreozcan.foodrecipesapp.ui.fragments.overview.OverviewFragment
import com.emreozcan.foodrecipesapp.util.Constants.Companion.RECIPE_BUNDLE
import com.emreozcan.foodrecipesapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel: MainViewModel by viewModels()

    private var isRecipeSaved = false
    private var recipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.viewPager.adapter = getAdapter()
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    private fun getAdapter(): PagerAdapter {
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_BUNDLE, args.result)

        val adapter = PagerAdapter(resultBundle, fragments, titles, supportFragmentManager)
        return adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu_item, menu)
        val menuItem = menu?.findItem(R.id.action_star)
        checkFavorites(menuItem!!)
        return true
    }

    private fun checkFavorites(menuItem: MenuItem) {
        mainViewModel.readFavorites.observe(this, {
            try {
                for (savedRecipe in it) {
                    if (savedRecipe.result.id == args.result.id) {
                        changeMenuIcon(menuItem, R.drawable.ic_star_fill)
                        isRecipeSaved = true
                        recipeId = savedRecipe.id
                        break
                    } else {
                        changeMenuIcon(menuItem, R.drawable.ic_star)
                    }
                }
            } catch (e: Exception) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.action_star && !isRecipeSaved) {
            insertToFavorites(item)
        } else if (item.itemId == R.id.action_star && isRecipeSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertToFavorites(item: MenuItem) {
        val favoriteEntitiy = FavoriteEntity(0, args.result)
        mainViewModel.insertFavorite(favoriteEntitiy)
        changeMenuIcon(item, R.drawable.ic_star_fill)
        showSnackBar("Recipe Added to Favorites")
        isRecipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoriteEntity = FavoriteEntity(recipeId, args.result)
        mainViewModel.deleteFavorite(favoriteEntity)
        isRecipeSaved = false
        showSnackBar("Recipe Deleted From Favorites !")
        changeMenuIcon(item, R.drawable.ic_star)
    }

    private fun showSnackBar(str: String) {
        Snackbar.make(binding.detailslayout, str, Snackbar.LENGTH_LONG).setAction("OKAY") {}.show()
    }

    private fun changeMenuIcon(item: MenuItem, icon: Int) {
        item.setIcon(icon)
    }
}