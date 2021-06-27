package com.emreozcan.foodrecipesapp.ui.fragments.foodjoke

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.databinding.FragmentFoodJokeBinding
import com.emreozcan.foodrecipesapp.util.Constants.Companion.API_KEY
import com.emreozcan.foodrecipesapp.util.NetworkResult
import com.emreozcan.foodrecipesapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FoodJokeFragment : Fragment() {

    private val mainViewModel by viewModels<MainViewModel>()

    private var _binding: FragmentFoodJokeBinding? = null
    private val binding get() = _binding!!

    private var foodJoke = "No Food Joke"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFoodJokeBinding.inflate(inflater,container,false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.mainViewModel = mainViewModel
        setHasOptionsMenu(true)

        mainViewModel.getFoodJoke(API_KEY)
        mainViewModel.foodJokeResponse.observe(viewLifecycleOwner,{ response ->
            when(response){
                is NetworkResult.Success -> {
                    foodJoke = response.data!!.text
                    binding.foodJokeTextView.text = foodJoke
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(requireContext(),response.message.toString(),Toast.LENGTH_LONG).show()

                }
            }
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.food_joke_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_share){
            val shareIntent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT,foodJoke)
                this.type = "text/plain"
            }
            startActivity(shareIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadDataFromCache(){

        lifecycleScope.launch {
            mainViewModel.readFoodJoke.observe(viewLifecycleOwner,{database ->
                if (!database.isNullOrEmpty()){
                    foodJoke = database[0].foodJoke.text
                    binding.foodJokeTextView.text = foodJoke
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}