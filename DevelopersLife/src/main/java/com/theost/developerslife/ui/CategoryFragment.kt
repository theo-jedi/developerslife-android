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

        fun newFragment(category: Category): Fragment {
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
            when (status!!) {
                Status.LOADING -> showLoading()
                Status.ERROR -> showNetworkError()
                Status.SUCCESS -> showContent()
                Status.EMPTY -> showEmptyError()
            }
        }

        viewModel.allData.observe(viewLifecycleOwner) { onPostReceived(it) }
        viewModel.currentPosition.observe(viewLifecycleOwner) { currentPosition = it }

        binding.networkButton.setOnClickListener { loadPost(Direction.DEFAULT) }
        binding.welcomeButton.setOnClickListener { loadPost(Direction.NEXT) }
        binding.nextButton.setOnClickListener { loadPost(Direction.NEXT) }
        binding.prevButton.setOnClickListener { loadPost(Direction.PREV) }

        return binding.root
    }

    private fun loadPost(direction: Direction) {
        lifecycleScope.launch { viewModel.loadPost(category, direction) }
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

    private fun updateButtons(isNextEnabled: Boolean) {
        binding.nextButton.isEnabled = isNextEnabled
        binding.prevButton.isEnabled = currentPosition > 0
        if (binding.prevButton.isEnabled || binding.nextButton.isEnabled) {
            updateActionButtonsViews(View.VISIBLE)
        }
    }

    private fun showContent() {
        binding.loadingBar.visibility = View.INVISIBLE
        binding.contentCard.visibility = View.VISIBLE

        updateButtons(true)
    }

    private fun showNetworkError() {
        binding.loadingBar.visibility = View.INVISIBLE
        updateNetworkErrorViews(View.VISIBLE)
        updateButtons(false)
    }

    private fun showEmptyError() {
        binding.loadingBar.visibility = View.INVISIBLE
        updateEmptyErrorViews(View.VISIBLE)

        if (currentPosition > 0) {
            updateButtons(false)
        }
    }

    private fun showLoading() {
        updateEmptyErrorViews(View.INVISIBLE)
        updateNetworkErrorViews(View.INVISIBLE)
        updateActionButtonsViews(View.INVISIBLE)
        hideWelcomeViews()

        binding.contentCard.visibility = View.INVISIBLE
        binding.loadingBar.visibility = View.VISIBLE
    }

    private fun hideWelcomeViews() {
        updateViews(
            binding.welcomeButton,
            binding.welcomeText,
            binding.welcomeIcon,
            visibility = View.INVISIBLE
        )
    }

    private fun updateNetworkErrorViews(visibility: Int) {
        updateViews(
            binding.networkButton,
            binding.networkText,
            binding.networkIcon,
            visibility = visibility
        )
    }

    private fun updateEmptyErrorViews(visibility: Int) {
        updateViews(
            binding.emptyText,
            binding.emptyIcon,
            visibility = visibility
        )
    }

    private fun updateActionButtonsViews(visibility: Int) {
        updateViews(
            binding.prevButton,
            binding.nextButton,
            visibility = visibility
        )
    }

    private fun updateViews(vararg views: View, visibility: Int) {
        views.forEach { it.visibility = visibility }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}