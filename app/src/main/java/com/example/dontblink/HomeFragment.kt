package com.example.dontblink

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dontblink.adapters.AdapterCategory
import com.example.dontblink.databinding.FragmentHomeBinding
import com.example.dontblink.models.Category


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        statusBarColour()
        setAllCategories()
        return binding.root
    }

    private fun setAllCategories() {
        val categoryList = ArrayList<Category>()
        for (i in Constants.allProductcategory.indices) {
            Log.d("HomeFragment", "Category: ${Constants.allProductcategory[i]}, Image: ${Constants.allProductCategoryIcon[i]}")
            categoryList.add(Category(Constants.allProductcategory[i], Constants.allProductCategoryIcon[i]))
        }
        binding.rvCategories.layoutManager = GridLayoutManager(context, 4)
        binding.rvCategories.adapter = AdapterCategory(categoryList)
    }




    private fun statusBarColour() {
        activity?.window?.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}