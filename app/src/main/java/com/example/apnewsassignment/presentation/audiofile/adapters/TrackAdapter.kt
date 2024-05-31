package com.example.apnewsassignment.presentation.audiofile.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apnewsassignment.R
import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.Result

class TrackAdapter(
    private val itemList: List<Result>,
    private val onItemClick: (Result) -> Unit
) :
    RecyclerView.Adapter<TrackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tracks_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.trackNameTextView.text = item.name
        holder.downloadTrackImageButton.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val trackNameTextView: TextView = itemView.findViewById(R.id.trackNameTextView)
        val downloadTrackImageButton: ImageButton =
            itemView.findViewById(R.id.downloadTrackImageButton)
    }
}