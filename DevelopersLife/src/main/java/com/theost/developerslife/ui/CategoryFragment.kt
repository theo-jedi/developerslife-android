package com.theost.developerslife.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.theost.developerslife.data.Category
import com.theost.developerslife.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment() {

    companion object {
        private const val FRAGMENT_CATEGORY_KEY = "category"

        fun newFragment(category: Category) : Fragment {
            val fragment = CategoryFragment()
            val bundle = Bundle()
            bundle.putSerializable(FRAGMENT_CATEGORY_KEY, category)
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

        val category = arguments?.getSerializable(FRAGMENT_CATEGORY_KEY) as Category

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}