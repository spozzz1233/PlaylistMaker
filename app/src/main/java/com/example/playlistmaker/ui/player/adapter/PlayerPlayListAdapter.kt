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

class PlayerPlayListAdapter(private var playlists: List<Playlist>,
                            private val clickListener: PlaylistClick
): RecyclerView.Adapter<PlayerPlayListAdapter.PlayerPlayLisViewHolder>() {
    fun updateData(newPlaylistList: List<Playlist>) {
        playlists = newPlaylistList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerPlayLisViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        return PlayerPlayLisViewHolder(CardNewPlaylistMiniBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlayerPlayLisViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(playlists[position])
            notifyDataSetChanged()
        }

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

            Glide.with(itemView)
                .load(item.uri)
                .placeholder(R.drawable.placeholder)
                .transform(CenterCrop(), RoundedCorners(2))
                .override(45, 45)
                .into(binding.image)
        }
    }
    fun interface PlaylistClick {
        fun onClick(playlist: Playlist)
    }

    fun setItems(items: List<Playlist>) {
        playlists = items
        notifyDataSetChanged()
    }
}
