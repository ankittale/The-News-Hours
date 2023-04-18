package com.ankitt.thenewshours.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankitt.thenewshours.NewsActivity
import com.ankitt.thenewshours.R
import com.ankitt.thenewshours.adapter.NewsAdapter
import com.ankitt.thenewshours.utils.Resource
import com.ankitt.thenewshours.viewmodel.NewsViewModel

class BreakingNewsFragment : Fragment(R.layout.fragment_breaking_news) {

    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var rvBreakingNews: RecyclerView
    private lateinit var paginationProgressBar: ProgressBar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
        rvBreakingNews = view.findViewById(R.id.rvBreakingNews) as RecyclerView
        paginationProgressBar = view.findViewById(R.id.paginationProgressBar) as ProgressBar
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_breakingNewsFragment_to_articleFragment,
                bundle
            )
        }



        //set observer for updating response
        newsViewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    //Null Check for data
                    response.data.let { newsResponse ->
                        if (newsResponse != null) {
                            newsAdapter.differ.submitList(newsResponse.articles)
                        }
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message.let { message ->
                        Log.e("BreakingNewsFragment", "An error occurred $message")
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        paginationProgressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        paginationProgressBar.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}