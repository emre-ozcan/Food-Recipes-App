package com.emreozcan.foodrecipesapp.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.adapters.FavoritesAdapter
import com.emreozcan.foodrecipesapp.databinding.FavoritesRecipesRowLayoutBinding
import com.emreozcan.foodrecipesapp.databinding.FragmentFavoriteRecipesBinding
import com.emreozcan.foodrecipesapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {

    private var _binding: FragmentFavoriteRecipesBinding?= null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by viewModels()

    private val mAdapter: FavoritesAdapter by lazy { FavoritesAdapter(requireActivity(),mainViewModel) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(layoutInflater,container,false)
        setHasOptionsMenu(true)

        setupRecyclerView()

        mainViewModel.readFavorites.observe(viewLifecycleOwner,{ favoritesList->
            mAdapter.updateFavoritesList(favoritesList)
        })

        binding.lifecycleOwner = this
        binding.mAdapter = mAdapter
        binding.mainViewModel = mainViewModel



        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_deleteAll){
            mainViewModel.deleteAllFavorites()
            showSnackbar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(){
        binding.rvFavorites.adapter = mAdapter
        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mAdapter.closeContextualActionMode()
    }

    private fun showSnackbar(){
        Snackbar.make(binding.root,"All Favorite Recipes Deleted",Snackbar.LENGTH_LONG).setAction("OKAY"){}.show()
    }
}