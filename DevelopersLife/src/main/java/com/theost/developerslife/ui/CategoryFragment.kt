package com.theost.developerslife.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.theost.developerslife.data.Category
import com.theost.developerslife.data.Direction
import com.theost.developerslife.data.Post
import com.theost.developerslife.data.Status
import com.theost.developerslife.databinding.FragmentCategoryBinding
import kotlinx.coroutines.launch

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

    private lateinit var category: Category
    private val viewModel: PostViewModel by viewModels()

    private var currentPosition = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        category = arguments?.getSerializable(FRAGMENT_CATEGORY_KEY) as Category

        viewModel.loadingStatus.observe(viewLifecycleOwner) { status ->
            when(status!!) {
                Status.LOADING -> showLoading()
                Status.ERROR -> showNetworkError()
                Status.SUCCESS -> showContent()
                Status.EMPTY -> showEmptyError()
            }
        }

        viewModel.allData.observe(viewLifecycleOwner) { onPostReceived(it) }

        binding.networkButton.setOnClickListener { loadPost(Direction.DEFAULT) }
        binding.welcomeButton.setOnClickListener { loadPost(Direction.NEXT) }
        binding.nextButton.setOnClickListener { loadPost(Direction.NEXT) }
        binding.prevButton.setOnClickListener { loadPost(Direction.PREV) }

        updateButtons()

        return binding.root
    }

    private fun onPostReceived(post: Post) {
        Glide.with(requireContext())
            .asGif()
            .load(post.imageLink)
            .listener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    viewModel.setStatus(Status.ERROR)
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.contentText.text = post.description
                    viewModel.setStatus(Status.SUCCESS)
                    return false
                }
            }).into(binding.contentImage)
    }

    private fun updateButtons() {
        if (currentPosition > 0) {
            binding.prevButton.isEnabled = true
            binding.prevButton.alpha = 1.0f
        } else {
            binding.prevButton.isEnabled = false
            binding.prevButton.alpha = 0.6f
        }

        binding.actionButtonsLayout.visibility = View.VISIBLE
    }

    private fun showContent() {
        binding.loadingBar.visibility = View.INVISIBLE
        binding.contentCard.visibility = View.VISIBLE

        updateButtons()
    }

    private fun showNetworkError() {
        binding.loadingBar.visibility = View.INVISIBLE
        binding.networkErrorLayout.visibility = View.VISIBLE
    }

    private fun showEmptyError() {
        binding.loadingBar.visibility = View.INVISIBLE
        binding.emptyErrorLayout.visibility = View.VISIBLE
    }

    private fun showLoading() {
        binding.welcomeMessageLayout.visibility = View.INVISIBLE
        binding.networkErrorLayout.visibility = View.INVISIBLE
        binding.actionButtonsLayout.visibility = View.INVISIBLE
        binding.contentCard.visibility = View.INVISIBLE
        binding.loadingBar.visibility = View.VISIBLE
    }

    private fun loadPost(direction: Direction) {
        currentPosition += direction.value
        lifecycleScope.launch { viewModel.loadPost(category, currentPosition) }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}