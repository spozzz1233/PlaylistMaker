package com.example.playlistmaker.ui.player.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.CardNewPlaylistMiniBinding
import com.example.playlistmaker.domain.playList.model.Playlist

class PlayerPlayListAdapter: RecyclerView.Adapter<PlayerPlayListAdapter.PlayerPlayLisViewHolder>() {
    private var plalists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlayLisViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerPlayLisViewHolder(CardNewPlaylistMiniBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return plalists.size
    }

    override fun onBindViewHolder(holder: PlayerPlayLisViewHolder, position: Int) {
        holder.bind(plalists[position])
    }


    class PlayerPlayLisViewHolder(private val binding: CardNewPlaylistMiniBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Playlist) {
            binding.playListName.text = item.playlistName
            val innerNumber = item.arrayNumber.toString()
            val text = when {
                innerNumber.toInt() % 10 == 1 && innerNumber.toInt() % 100 != 11 -> " трек"
                innerNumber.toInt() % 10 == 2 && innerNumber.toInt() % 100 != 12 -> " трека"
                innerNumber.toInt() % 10 == 3 && innerNumber.toInt() % 100 != 13 -> " трека"
                innerNumber.toInt() % 10 == 4 && innerNumber.toInt() % 100 != 14 -> " трека"
                else -> " треков"
            }
            val number = "$innerNumber $text"
            binding.numberOfTracks.text = number

            if (item.uri.isEmpty()) {
                val imageResource = R.drawable.placeholder
                binding.image.setImageResource(imageResource)
                val layoutParams = binding.image.layoutParams
                layoutParams.width = 104
                layoutParams.height = 103
                binding.image.layoutParams = layoutParams
            } else {
                Glide.with(itemView)
                    .load(item.uri)
                    .placeholder(R.drawable.placeholder)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .override(160, 160)
                    .into(binding.image)
            }
        }
    }
    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }

    fun setItems(items: List<Playlist>) {
        plalists = items
        notifyDataSetChanged()
    }
}
