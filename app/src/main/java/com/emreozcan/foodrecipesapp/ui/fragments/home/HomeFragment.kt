package com.emreozcan.foodrecipesapp.ui.fragments.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.foodrecipesapp.MainViewModel
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.adapters.RecipesAdapter
import com.emreozcan.foodrecipesapp.databinding.FragmentHomeBinding
import com.emreozcan.foodrecipesapp.util.Constants.Companion.API_KEY
import com.emreozcan.foodrecipesapp.util.NetworkResult
import com.emreozcan.foodrecipesapp.util.addScrollHide
import com.emreozcan.foodrecipesapp.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipeViewModel

    private val mAdapter by lazy { RecipesAdapter() }

    private var _binding: FragmentHomeBinding?= null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipeViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater,container,false)


        setupRecyclerView()
        getApiData()

        return binding.root
    }

    private fun getApiData() {
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Success->{
                    hideShimmerEffect()
                    response.data?.let {
                        mAdapter.setData(it)
                    }
                }
                is NetworkResult.Error->{
                    hideShimmerEffect()
                    Toast.makeText(context,response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading->{
                    showShimmerEffect()
                }
            }



        })
    }
    private fun setupRecyclerView() {
        binding.rvRecipes.adapter = mAdapter
        binding.rvRecipes.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipes.addScrollHide(binding.floatingActionButton,requireActivity().findViewById(R.id.bottomNavigationView))
    }
    private fun showShimmerEffect(){
        binding.shimmerFrameLayout.startShimmer()
        binding.shimmerFrameLayout.visibility = View.VISIBLE
        binding.rvRecipes.visibility = View.GONE
    }
    private fun hideShimmerEffect(){
        binding.shimmerFrameLayout.hideShimmer()
        binding.shimmerFrameLayout.visibility = View.GONE
        binding.rvRecipes.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}