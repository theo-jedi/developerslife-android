package com.theost.developerslife.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.theost.developerslife.R
import com.theost.developerslife.data.Category
import com.theost.developerslife.widgets.CategoryPageAdapter

class FeedActivity : FragmentActivity() {

    private val categoryList = listOf(
        Category.LATEST,
        Category.BEST,
        Category.HOT
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        val categoryPages = findViewById<ViewPager2>(R.id.category_pages)
        val categoryTabs = findViewById<TabLayout>(R.id.category_tabs)
        categoryPages.adapter = CategoryPageAdapter(this, categoryList)
        TabLayoutMediator(categoryTabs, categoryPages) { tab, position ->
            tab.text = categoryList[position].uiName
        }.attach()
    }
}