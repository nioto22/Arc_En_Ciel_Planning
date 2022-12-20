package com.aprouxdev.arcencielplanning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.data.models.Event



data class HomeEventData(val date: String, val events: List<Event>)

class HomeEventContainerAdapter(
    var data: List<HomeEventData>,
    private val listener: HomeEventListener?
    ) : RecyclerView.Adapter<HomeEventContainerAdapter.HomeEventContainerViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeEventContainerViewHolder {
        return HomeEventContainerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_event_container, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: HomeEventContainerViewHolder, position: Int) {
        holder.bind(data[position], position, listener)
    }

    fun updateData(data: List<HomeEventData>) {
        this.data = data
        notifyDataSetChanged()
    }

    class HomeEventContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mDate: AppCompatTextView = itemView.findViewById(R.id.home_event_date_title)
        private val mEventRecyclerView: RecyclerView = itemView.findViewById(R.id.home_event_events_recyclerview)

        private val mLineTop: View? = itemView.findViewById(R.id.home_event_line_top)

        fun bind(item: HomeEventData, position: Int, listener: HomeEventListener?) {
            mLineTop?.isInvisible = position == 0
            mDate.text = item.date.toString() // TODO FORMAT DATE STRING
            with(mEventRecyclerView) {
                layoutManager = LinearLayoutManager(context)
                val eventsAdapter = HomeEventAdapter(
                    data= item.events,
                    listener= object: HomeEventListener{
                        override fun onHomeEventClicked(event: Event) {
                            listener?.onHomeEventClicked(event)
                        }
                    }
                )
                adapter = eventsAdapter
            }

        }
    }
}