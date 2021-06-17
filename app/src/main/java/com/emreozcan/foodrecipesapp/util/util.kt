package com.emreozcan.foodrecipesapp.util

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import com.emreozcan.foodrecipesapp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


fun ImageView.loadCircleImage(url: String?, placeHolder: CircularProgressDrawable) {
    this.load(url) {
        crossfade(true)
        placeholder(placeHolder)
        error(R.drawable.ic_placeholder)
        crossfade(500)
        transformations(CircleCropTransformation())
    }
}

fun createPlaceHolder(context: Context): CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 12f
        centerRadius = 40f
        start()
    }
}

fun RecyclerView.addScrollHide(fab: FloatingActionButton, bnv: BottomNavigationView) {
    this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy < 0 && !fab.isShown) {
                fab.show()
                bnv.startAnimation(
                    AnimationUtils.loadAnimation(
                        bnv.context,
                        R.anim.anim_show_bottom_navigation
                    )
                )
            } else if (dy > 0 && fab.isShown) {
                fab.hide()
                bnv.startAnimation(
                    AnimationUtils.loadAnimation(
                        bnv.context,
                        R.anim.anim_hide_bottom_navigation
                    )
                )
                bnv.visibility = View.INVISIBLE
            }
        }
    })
}