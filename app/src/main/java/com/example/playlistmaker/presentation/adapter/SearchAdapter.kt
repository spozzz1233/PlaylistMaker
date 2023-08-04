package com.example.playlistmaker.presentation.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.util.MusicHistory
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.SongActivity
import com.example.playlistmaker.util.Debounce
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.model.tracks
import java.text.SimpleDateFormat
import java.util.*

class searchAdapter(private val context: Context) : RecyclerView.Adapter<searchAdapter.SearchViewHolder>() {

    private val musicHistory = MusicHistory(context)
    private val debounce = Debounce()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val cardMusicView = LayoutInflater.from(parent.context).inflate(R.layout.card_music, parent, false)
        return SearchViewHolder(cardMusicView)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])
        val track = tracks[position]
        holder.itemView.setOnClickListener {
            musicHistory.saveHistoryTrack(track)
            debounce.clickDebounce()
            val songActivity = Intent(holder.itemView.context, SongActivity::class.java)
            songActivity.putExtra("trackName", track.trackName)
            songActivity.putExtra("artistName", track.artistName)
            songActivity.putExtra("trackTimeMillis", track.trackTimeMillis)
            songActivity.putExtra("artworkUrl100", track.artworkUrl100)
            songActivity.putExtra("collectionName", track.collectionName)
            songActivity.putExtra("releaseDate", track.releaseDate)
            songActivity.putExtra("primaryGenreName", track.primaryGenreName)
            songActivity.putExtra("country", track.country)
            songActivity.putExtra("trackUrl", track.previewUrl)
            holder.itemView.context.startActivity(songActivity)
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
