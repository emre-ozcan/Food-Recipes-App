package com.emreozcan.foodrecipesapp.ui.fragments.instructions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.emreozcan.foodrecipesapp.R
import com.emreozcan.foodrecipesapp.databinding.FragmentInstructionsBinding
import com.emreozcan.foodrecipesapp.models.Result
import com.emreozcan.foodrecipesapp.util.Constants


class InstructionsFragment : Fragment() {

    private var _binding: FragmentInstructionsBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInstructionsBinding.inflate(layoutInflater,container,false)
        val args = arguments
        val recipe: Result? = args?.getParcelable(Constants.RECIPE_BUNDLE)

        binding.instructionsWebview.webViewClient = object : WebViewClient(){}
        val url= recipe!!.sourceUrl
        binding.instructionsWebview.loadUrl(url)



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}