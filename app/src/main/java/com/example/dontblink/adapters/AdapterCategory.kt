package com.example.dontblink.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dontblink.databinding.ItemViewProductCategoryBinding
import com.example.dontblink.models.Category

class AdapterCategory(val categoryList: ArrayList<Category>):RecyclerView.Adapter<AdapterCategory.CategoryViewHolder>() {
    class CategoryViewHolder(val binding:ItemViewProductCategoryBinding):ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(ItemViewProductCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        val category = categoryList[position]

        holder.binding.apply {
            ivCategoryImage.setImageResource(category.image)
            tvcategory.text  = category.title
        }
    }
}

