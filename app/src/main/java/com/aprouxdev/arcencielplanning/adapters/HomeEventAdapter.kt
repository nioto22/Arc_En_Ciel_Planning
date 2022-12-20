package com.aprouxdev.arcencielplanning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.data.models.Event

interface HomeEventListener {
    fun onHomeEventClicked(event: Event)
}

class HomeEventAdapter(
    var data: List<Event>,
    val listener: HomeEventListener
) : RecyclerView.Adapter<HomeEventAdapter.HomeEventViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeEventViewHolder {
        return HomeEventViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_home_event, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: HomeEventViewHolder, position: Int) =
        holder.bind(data[position], listener)

    fun updateData(data: List<Event>) {
        this.data = data
        notifyDataSetChanged()
    }

    class HomeEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val eventContainer: RelativeLayout? =
            itemView.findViewById(R.id.home_event_container)
        private val eventTime: AppCompatTextView? = itemView.findViewById(R.id.home_event_time)
        private val titleSeparator: AppCompatTextView? =
            itemView.findViewById(R.id.home_event_title_separator)
        private val eventTeam: AppCompatTextView? = itemView.findViewById(R.id.home_event_team)
        private val eventTitle: AppCompatTextView? = itemView.findViewById(R.id.home_event_title)


        fun bind(item: Event, listener: HomeEventListener) {
            eventTime?.text = item.time
            //titleSeparator?.isVisible = item.team != null
            eventTeam?.text = item.team.name
            eventTitle?.text = item.title
            eventContainer?.setOnClickListener {
                listener.onHomeEventClicked(item)
            }
        }
    }
}