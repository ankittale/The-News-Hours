package com.ankitt.thenewshours.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ankitt.thenewshours.NewsActivity
import com.ankitt.thenewshours.R
import com.ankitt.thenewshours.adapter.NewsAdapter
import com.ankitt.thenewshours.viewmodel.NewsViewModel

class SavedNewsFragment : Fragment(R.layout.fragment_saved_news) {

    lateinit var newsViewModel: NewsViewModel
    private lateinit var rvSavedNews: RecyclerView
    private lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvSavedNews = view.findViewById(R.id.rvSavedNews) as RecyclerView
        newsViewModel = (activity as NewsActivity).newsViewModel

        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(
                R.id.action_savedNewsFragment_to_articleFragment,
                bundle
            )
        }

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        rvSavedNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}