package com.example.playlistmaker

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class searchAdapter(private val context: Context) : RecyclerView.Adapter<searchAdapter.SearchViewHolder>() {

    private val musicHistory = MusicHistory(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val cardMusicView = LayoutInflater.from(parent.context).inflate(R.layout.card_music, parent, false)
        return SearchViewHolder(cardMusicView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        val track = tracks[position]
        holder.itemView.setOnClickListener {
            musicHistory.saveHistoryTrack(track)
        }
    }

    override fun getItemCount(): Int = tracks.size

    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_Name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_Time)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners)))
                .into(image)
        }
    }
}
