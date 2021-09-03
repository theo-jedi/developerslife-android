package com.theost.developerslife.widgets

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.theost.developerslife.data.Category
import com.theost.developerslife.ui.CategoryFragment

class CategoryPageAdapter(fragmentActivity: FragmentActivity, val categoryList: List<Category>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment =
        CategoryFragment.newFragment(categoryList[position])

    override fun getItemCount(): Int = categoryList.size
}