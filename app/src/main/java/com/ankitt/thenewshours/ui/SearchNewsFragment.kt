package com.ankitt.thenewshours.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankitt.thenewshours.NewsActivity
import com.ankitt.thenewshours.R
import com.ankitt.thenewshours.adapter.NewsAdapter
import com.ankitt.thenewshours.utils.QUERY_PAGE_SIZE
import com.ankitt.thenewshours.utils.Resource
import com.ankitt.thenewshours.utils.SEARCH_NEWS_DELAY
import com.ankitt.thenewshours.viewmodel.NewsViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    private var job: Job? = null

    var isLoadingNewsPage = false
    var isLastNewsPageLoad = false
    var isScrollingNewsPage = false

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var rvSearchNews: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var etSearchView: EditText
    private lateinit var paginationProgressBar: ProgressBar


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
        rvSearchNews = view.findViewById(R.id.rvSearchNews) as RecyclerView
        paginationProgressBar = view.findViewById(R.id.paginationProgressBar) as ProgressBar
        etSearchView = view.findViewById(R.id.etSearch) as EditText
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }


        etSearchView.addTextChangedListener {editable ->
            job?.cancel()
            job = MainScope().launch {
                delay(SEARCH_NEWS_DELAY)
                editable?.let {
                    if(editable.toString().isNotEmpty()){
                        newsViewModel.searchNews(editable.toString())
                    }
                }
            }
        }


        newsViewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data.let { newsResponse ->
                        if (newsResponse != null) {
                            newsAdapter.differ.submitList(newsResponse.articles.toList())
                            val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                            //integer division that return near vale. last page is empty so add 1 that why 2
                            isLastNewsPageLoad = newsViewModel.searchNewsPage == totalPages
                            if(isLastNewsPageLoad){
                                rvSearchNews.setPadding(0, 0, 0, 0)
                            }
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { message ->
                        //Log.e("SearchNewsFragment", "An error occurred $message")
                        Toast.makeText(activity, "An error occurred $message", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private val scrollChangeListener = object : RecyclerView.OnScrollListener() {

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            //Calculate to check we reached bottom of view
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleNewsPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleNewsItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            //Check for bottom reach in recycler view
            val isNotLoadingAndNotLastPage = !isLoadingNewsPage && !isLastNewsPageLoad
            val isAtLastNewsItem = firstVisibleNewsPosition + visibleNewsItemCount >= totalItemCount

            val isNotAtBeginning = firstVisibleNewsPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldWePaginate =
                isNotLoadingAndNotLastPage && isAtLastNewsItem && isNotAtBeginning
                        && isTotalMoreThanVisible && isScrollingNewsPage
            if (shouldWePaginate) {
                newsViewModel.searchNews(etSearchView.text.toString())
            }
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                //Are we scrolling list in recyclerview
                isScrollingNewsPage = true
            }
        }
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
        isLoadingNewsPage = false
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
        isLoadingNewsPage = true
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSearchNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            addOnScrollListener(this@SearchNewsFragment.scrollChangeListener)
        }
    }
}