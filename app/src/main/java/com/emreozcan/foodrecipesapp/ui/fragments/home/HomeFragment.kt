package com.emreozcan.foodrecipesapp.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.emreozcan.foodrecipesapp.viewmodels.MainViewModel
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.adapters.RecipesAdapter
import com.emreozcan.foodrecipesapp.databinding.FragmentHomeBinding
import com.emreozcan.foodrecipesapp.util.NetworkListener
import com.emreozcan.foodrecipesapp.util.NetworkResult
import com.emreozcan.foodrecipesapp.util.addScrollHide
import com.emreozcan.foodrecipesapp.util.observeOnce
import com.emreozcan.foodrecipesapp.viewmodels.RecipeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private val args by navArgs<HomeFragmentArgs>()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipeViewModel

    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var networkListener: NetworkListener

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

        setHasOptionsMenu(true)
        setupRecyclerView()

        recipeViewModel.readBackOnline.observe(viewLifecycleOwner,{
            recipeViewModel.backOnline = it
        })


        lifecycleScope.launchWhenCreated {
            networkListener = NetworkListener()
            networkListener.checkNetwork(requireContext()).collect { status->
                recipeViewModel.networkStatus = status
                recipeViewModel.showNetworkStatus()
                readDatabase()
            }
        }
        binding.floatingActionButton.setOnClickListener {
            if (recipeViewModel.networkStatus){
                findNavController().navigate(R.id.action_homeFragment_to_recipesBottomSheet)
            }else{
                recipeViewModel.showNetworkStatus()
            }

        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.recipes_menu,menu)

        val search = menu.findItem(R.id.action_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchApiData(query)
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return true
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner,{ database->
                if (!database.isNullOrEmpty()&& ! args.backFromBottomSheet){
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
                    hideShimmerEffect()
                    Toast.makeText(context,response.message.toString(),Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading->{
                    showShimmerEffect()
                }
            }



        })
    }
    private fun searchApiData(query: String){
        showShimmerEffect()
        mainViewModel.searchRecipes(recipeViewModel.searchQueries(query))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner,{
            response->
            when(response){
                is NetworkResult.Success->{
                    response.data?.let {
                        mAdapter.setData(it)
                        hideShimmerEffect()
                    }
                }
                is NetworkResult.Error->{
                    loadDataFromCache()
                    hideShimmerEffect()
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