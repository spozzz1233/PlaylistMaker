package com.example.playlistmaker.ui.search.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.domain.search.model.HistoryTrack
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.search.model.historyTracks
import com.example.playlistmaker.util.MusicHistory
import java.text.SimpleDateFormat
import java.util.*


class HistoryAdapter(
    private val context: Context,
    private val clickListener: HistoryClick
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val musicHistory = MusicHistory(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val cardMusicView =
            LayoutInflater.from(parent.context).inflate(R.layout.card_music, parent, false)
        return HistoryViewHolder(cardMusicView)
    }

    override fun getItemCount(): Int = historyTracks.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyTrack = historyTracks[position]
        holder.bind(historyTrack)
        holder.itemView.setOnClickListener {
            musicHistory.curentPosition(historyTrack)
            clickListener.onClick(historyTrack)
            notifyDataSetChanged()
        }
    }

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_Name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_Time)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(track: HistoryTrack) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .transform(RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners)))
                .into(image)
        }
    }

    fun interface HistoryClick {
        fun onClick(track: HistoryTrack)
    }
}
