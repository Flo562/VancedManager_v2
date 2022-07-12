package com.vanced.manager.origin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.vanced.manager.origin.R
import com.vanced.manager.origin.databinding.ViewSocialLinkBinding
import com.vanced.manager.origin.model.LinkModel
import com.vanced.manager.origin.ui.viewmodels.HomeViewModel

class LinkAdapter(
    private val context: Context,
    private val viewModel: HomeViewModel
) : RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {

    private val github = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_github),
        GITHUB
    )

    private val telegram = LinkModel(
        AppCompatResources.getDrawable(context, R.drawable.ic_telegram),
        TELEGRAM
    )

    val links = arrayOf(github, telegram)

    inner class LinkViewHolder(private val binding: ViewSocialLinkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val logo = binding.linkImage

        fun bind(position: Int) {
            binding.linkBg.setOnClickListener {
                viewModel.openUrl(links[position].linkUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val view = ViewSocialLinkBinding.inflate(LayoutInflater.from(context), parent, false)
        return LinkViewHolder(view)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(position)
        holder.logo.setImageDrawable(links[position].linkIcon)
    }

    override fun getItemCount(): Int = links.size

    companion object {
        const val GITHUB = "https://github.com/inotia00/VancedManager_v2"
        const val TELEGRAM = "https://t.me/vanced_mod_archive"
    }

}