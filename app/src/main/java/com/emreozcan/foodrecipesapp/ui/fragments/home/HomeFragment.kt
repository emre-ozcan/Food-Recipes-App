package com.emreozcan.foodrecipesapp.ui.fragments.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.foodrecipesapp.viewmodels.MainViewModel
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.adapters.RecipesAdapter
import com.emreozcan.foodrecipesapp.databinding.FragmentHomeBinding
import com.emreozcan.foodrecipesapp.util.NetworkResult
import com.emreozcan.foodrecipesapp.util.addScrollHide
import com.emreozcan.foodrecipesapp.util.observeOnce
import com.emreozcan.foodrecipesapp.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel


        setupRecyclerView()
        readDatabase()

        return binding.root
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner,{ database->
                if (!database.isNullOrEmpty()){
                    Log.e("Database","Local Cache")
                    mAdapter.setData(database[0].foodModel)
                    hideShimmerEffect()
                }else{
                    getApiData()
                }
            })
        }

    }

    private fun getApiData() {
        Log.e("Database","From API")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Success->{
                    response.data?.let {
                        mAdapter.setData(it)
                        hideShimmerEffect()
                    }
                }
                is NetworkResult.Error->{
                    loadDataFromCache()
                    Toast.makeText(context,response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading->{
                    showShimmerEffect()
                }
            }



        })
    }
    private fun loadDataFromCache(){
        mainViewModel.readRecipes.observe(viewLifecycleOwner,{database->
            if (!database.isNullOrEmpty()){
                mAdapter.setData(database[0].foodModel)
            }
        })
        hideShimmerEffect()
    }
    private fun setupRecyclerView() {
        showShimmerEffect()
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