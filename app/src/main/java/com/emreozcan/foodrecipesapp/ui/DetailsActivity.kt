package com.emreozcan.foodrecipesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.adapters.PagerAdapter
import com.emreozcan.foodrecipesapp.databinding.ActivityDetailsBinding
import com.emreozcan.foodrecipesapp.ui.fragments.ingredients.IngredientsFragment
import com.emreozcan.foodrecipesapp.ui.fragments.instructions.InstructionsFragment
import com.emreozcan.foodrecipesapp.ui.fragments.overview.OverviewFragment
import com.emreozcan.foodrecipesapp.util.Constants.Companion.RECIPE_BUNDLE

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private val args by navArgs<DetailsActivityArgs>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.viewPager.adapter = getAdapter()
        binding.tabLayout.setupWithViewPager(binding.viewPager)


    }

    private fun getAdapter() : PagerAdapter{
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable(RECIPE_BUNDLE,args.result)

        val adapter = PagerAdapter(resultBundle,fragments,titles,supportFragmentManager)
        return adapter

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}