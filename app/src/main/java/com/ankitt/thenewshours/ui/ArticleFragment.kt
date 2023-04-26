package com.ankitt.thenewshours.ui

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ankitt.thenewshours.NewsActivity
import com.ankitt.thenewshours.R
import com.ankitt.thenewshours.viewmodel.NewsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsWebView: WebView
    lateinit var fabSavedNews: FloatingActionButton
    private val args: ArticleFragmentArgs by navArgs() // The args are pass in navigation
    //which return whole article object from breaking, search and saved news

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
        fabSavedNews = view.findViewById(R.id.fab)
        newsWebView = view.findViewById(R.id.webView)
        val article = args.article

        newsWebView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url.toString())
        }

        fabSavedNews.setOnClickListener {
            newsViewModel.saveArticle(article)
            Snackbar.make(view, "Article Saved", Snackbar.LENGTH_SHORT).show()
        }
    }
}