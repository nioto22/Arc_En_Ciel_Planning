package com.aprouxdev.arcencielplanning.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.RecyclerView
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.extensions.formattedToString
import com.google.android.flexbox.FlexboxLayout

interface HomeEventListener {
    fun onHomeEventClicked(event: Event)
}

class HomeEventAdapter(
    val context: Context,
    var data: List<Event>,
    private val listener: HomeEventListener?
) : RecyclerView.Adapter<HomeEventAdapter.HomeEventContainerViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeEventContainerViewHolder {
        return HomeEventContainerViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_event_container, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: HomeEventContainerViewHolder, position: Int) {
        holder.bind(context, data[position], listener)
    }

    fun updateData(data: List<Event>) {
        this.data = data
        notifyDataSetChanged()
    }

    class HomeEventContainerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mDate: AppCompatTextView = itemView.findViewById(R.id.home_event_date_title)
        private val mCardView: CardView? = itemView.findViewById(R.id.home_event_card_view)
        private val mTime: AppCompatTextView? = itemView.findViewById(R.id.event_time)
        private val mTeam: AppCompatTextView? = itemView.findViewById(R.id.event_team)
        private val mAvatarsContainer: FlexboxLayout? =
            itemView.findViewById(R.id.event_avatars_flexbox)
        private val mCommentsTextView: AppCompatTextView? =
            itemView.findViewById(R.id.event_comments_tv)
        private val mCheckIcon: AppCompatImageView? = itemView.findViewById(R.id.event_check_icon)


        fun bind(context: Context, item: Event, listener: HomeEventListener?) {
            mCardView?.setOnClickListener {
                listener?.onHomeEventClicked(item)
            }
            mDate.text = item.date?.formattedToString("dd MMMM yyyy")
            mCardView?.apply {
                setCardBackgroundColor(ContextCompat.getColor(context, item.team.getColorRes()))
            }
            mTime?.apply {
                isInvisible = item.time == null
                text = item.time
            }
            mTeam?.text = if (item.team == Teams.Other) item.title else item.team.getName()

            /*
            * AVATARS
             */
            // TODO AVATAR ICONS
            mAvatarsContainer.apply {

            }

            /*
            * COMMENTS
             */
            // TODO SORT COMMENTS BY DATE AND WHAT IF DATE NULL
            // TODO MANAGE TEXT ELLIPSE
            val commentsText =
                if (item.comments.isEmpty()) context.getString(R.string.event_comment_hint)
                else item.comments.joinToString("\n") { "${it.user}: ${it.text}" }
            val commentsColor =
                if (item.comments.isEmpty()) R.color.greys_400 else R.color.greys_700
            mCommentsTextView?.apply {
                text = commentsText
                setTextColor(ContextCompat.getColor(context, commentsColor))
            }

            /*
            * SHOP TEAM ICON
             */
            mCheckIcon?.isInvisible = item.team != Teams.Shop
            if (item.team == Teams.Shop) {
                val iconRes = if (item.users.size >= 3) R.drawable.ic_check else R.drawable.ic_bad
                mCheckIcon?.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
            }
        }
    }
}