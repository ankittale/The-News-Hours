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

class ArticleFragment : Fragment(R.layout.fragment_article) {

    lateinit var newsViewModel: NewsViewModel
    lateinit var newsWebView: WebView
    private val args: ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as NewsActivity).newsViewModel
        newsWebView = view.findViewById(R.id.webView)
        val article = args.article

        newsWebView.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url)
        }

    }
}