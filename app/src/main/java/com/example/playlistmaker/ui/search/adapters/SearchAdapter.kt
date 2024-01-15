package com.example.playlistmaker.ui.search.adapters

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
import com.example.playlistmaker.domain.search.model.tracks
import java.text.SimpleDateFormat
import java.util.*

class SearchAdapter(
    private val clickListener: TrackClick,
    private val longClickListener: LongClick,
    private val imageSize: ImageSize
) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    fun updateData() {
        notifyDataSetChanged()
    }

    fun newTracks(newTracks: ArrayList<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val cardMusicView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_music, parent, false)
        return SearchViewHolder(cardMusicView, imageSize)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        val track = tracks[position]
        holder.itemView.setOnClickListener {
            clickListener.onClick(track)
        }
        holder.itemView.setOnLongClickListener {
            longClickListener.onLongClick(track)
            notifyDataSetChanged()
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int = tracks.size

    class SearchViewHolder(view: View, private val imageSize: ImageSize) : RecyclerView.ViewHolder(view) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_Name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_Time)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            Glide.with(itemView.context)
                .load(when(imageSize){
                    ImageSize._100 -> track.artworkUrl100
                    ImageSize._60 -> track.artworkUrl60
                })
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners)))
                .into(image)
        }
    }

    enum class ImageSize{
        _60,
        _100
    }
    fun interface TrackClick {
        fun onClick(track: Track)
    }

    fun interface LongClick {
        fun onLongClick(track: Track)
    }

}
