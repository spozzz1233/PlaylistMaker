package com.example.playlistmaker
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.*

class searchAdapter() : RecyclerView.Adapter<searchAdapter.SearchViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        var cardMusicView = LayoutInflater.from(parent.context).inflate(R.layout.card_music, parent, false)
        return SearchViewHolder(cardMusicView)
    }
    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(tracks[position])

    }
    override fun getItemCount(): Int = tracks.size
    class SearchViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val trackName: TextView = itemView.findViewById(R.id.track_name)
        private val artistName: TextView = itemView.findViewById(R.id.artist_Name)
        private val trackTime: TextView = itemView.findViewById(R.id.track_Time)
        private val image: ImageView = itemView.findViewById(R.id.image)
        fun bind(track: Track) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text =  SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)

            Glide.with(itemView.context)
                .load(track.artworkUrl100)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .transform(RoundedCorners(2))
                .into(image)

        }
    }
}
