package com.ankitt.thenewshours.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ankitt.thenewshours.R
import com.ankitt.thenewshours.model.Article
import com.bumptech.glide.Glide

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newsImage = itemView.findViewById(R.id.ivArticleImage) as ImageView
        val newsTitle = itemView.findViewById(R.id.tvTitle) as TextView
        val newsDescription = itemView.findViewById(R.id.tvDescription) as TextView
        val newsSource = itemView.findViewById(R.id.tvSource) as TextView
        val newsPublishAt = itemView.findViewById(R.id.tvPublishedAt) as TextView
    }

    //Compare two list and update only content which are required and list of notifyDataSetChange
    // which updates whole list things

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url  // Why URL because it will be unique with each news
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size   // return differ thing only to update
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val articleListPosition = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(this).load(articleListPosition.urlToImage).into(holder.newsImage)
            holder.newsSource.text = articleListPosition.source.name
            holder.newsDescription.text = articleListPosition.description
            holder.newsTitle.text = articleListPosition.title
            holder.newsPublishAt.text = articleListPosition.publishedAt

            setOnClickListener {
                onItemClickListener?.let { it(articleListPosition) }
            }
        }
    }

    private var onItemClickListener: ((Article) -> Unit)? = null  //Passed full article

    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}