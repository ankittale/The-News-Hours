package com.ankitt.thenewshours

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ankitt.thenewshours.db.ArticleDatabase
import com.ankitt.thenewshours.repository.NewsRepository
import com.ankitt.thenewshours.viewmodel.NewsViewModel
import com.ankitt.thenewshours.viewmodel.NewsViewModelProviderFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class NewsActivity : AppCompatActivity() {

    lateinit var newsViewModel: NewsViewModel
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        newsViewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]

        val newsNavHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        bottomNavigationView = findViewById(R.id.bottomAppBar)
        if (newsNavHostFragment != null) {
            bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
        }
    }
}