package com.example.playlistmaker.ui.media.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.Track
import java.text.SimpleDateFormat
import java.util.*

class FragmentFavoriteAdapter(
    private val clickListener: TrackClick
) : RecyclerView.Adapter<FragmentFavoriteAdapter.FavoriteViewHolder>() {

    private var _items: List<Track> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val cardMusicView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_music, parent, false)
        return FavoriteViewHolder(cardMusicView)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(_items[position])
        holder.itemView.setOnClickListener {
            clickListener.onClick(_items[position])
        }
    }

    override fun getItemCount(): Int = _items.size

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_Name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_Time)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault())
                .format(track.trackTimeMillis)

            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(
                    RoundedCorners(
                        itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners)
                    )
                )
                .into(image)
        }
    }

    fun interface TrackClick {
        fun onClick(track: Track)
    }

    fun setItems(items: List<Track>) {
        _items = items
        notifyDataSetChanged()
    }
}
