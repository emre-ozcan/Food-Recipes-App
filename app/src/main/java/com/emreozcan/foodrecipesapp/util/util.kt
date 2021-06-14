package com.emreozcan.foodrecipesapp.util

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton


fun ImageView.loadCircleImage(url: String?, placeHolder: CircularProgressDrawable){
    this.load(url){
        crossfade(true)
        placeholder(placeHolder)
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

fun RecyclerView.addScrollHide(fab: FloatingActionButton, bnv: BottomNavigationView){
    this.addOnScrollListener(object : RecyclerView.OnScrollListener(){
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy<0&&!fab.isShown){
                fab.show()
                bnv.visibility = View.VISIBLE
            }else if (dy>0&&fab.isShown){
                fab.hide()
                bnv.visibility = View.INVISIBLE
            }
        }
    })
}