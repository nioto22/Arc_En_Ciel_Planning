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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.data.enums.Teams
import com.aprouxdev.arcencielplanning.data.models.Event
import com.aprouxdev.arcencielplanning.extensions.formattedToString
import com.google.android.flexbox.FlexboxLayout

interface PlanningEventListener {
    fun onPlanningEventClicked(event: Event)
}

public class PlanningEventAdapter(
    val context: Context,
    var data: List<Event>,
    private val listener: PlanningEventListener?
) : RecyclerView.Adapter<PlanningEventAdapter.PlanningEventViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlanningEventViewHolder {
        return PlanningEventViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_home_event_container, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: PlanningEventViewHolder, position: Int) {
        holder.bind(context, position, data[position], listener)
    }

    fun updateData(data: List<Event>) {
        this.data = data
        notifyDataSetChanged()
    }

    class PlanningEventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val mDate: AppCompatTextView = itemView.findViewById(R.id.home_event_date_title)
        private val mCardView: CardView? = itemView.findViewById(R.id.home_event_card_view)
        private val mTime: AppCompatTextView? = itemView.findViewById(R.id.event_time)
        private val mTeam: AppCompatTextView? = itemView.findViewById(R.id.event_team)
        private val mAvatarsContainer: FlexboxLayout? =
            itemView.findViewById(R.id.event_avatars_flexbox)
        private val mCommentsTextView: AppCompatTextView? =
            itemView.findViewById(R.id.event_comments_tv)
        private val mCheckIcon: AppCompatImageView? = itemView.findViewById(R.id.event_check_icon)


        fun bind(context: Context, position: Int, item: Event, listener: PlanningEventListener?) {
            mCardView?.setOnClickListener {
                listener?.onPlanningEventClicked(item)
            }
            mDate.apply {
                isVisible = position == 0
                text = item.date?.formattedToString("dd MMMM yyyy")
            }
            mCardView?.apply {
                setCardBackgroundColor(ContextCompat.getColor(context, item.getTeamByName().getColorRes()))
            }
            mTime?.apply {
                isInvisible = item.time == null
                text = item.time
            }
            mTeam?.text = if (item.getTeamByName() == Teams.Other) item.title else item.getTeamByName().getName()

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
           /* val commentsText =
                if (item.comments.isNullOrEmpty()) context.getString(R.string.event_comment_hint)
                else // TODO GET COMMENT
            val commentsColor =
                if (item.comments.isNullOrEmpty()) R.color.greys_400 else R.color.greys_700
            mCommentsTextView?.apply {
                text = commentsText
                setTextColor(ContextCompat.getColor(context, commentsColor))
            }*/

            /*
            * SHOP TEAM ICON
             */
            mCheckIcon?.isInvisible = item.getTeamByName() != Teams.Shop
            if (item.getTeamByName() == Teams.Shop) {
                val iconRes = if ((item.users?.size ?: 0) >= 3) R.drawable.ic_check else R.drawable.ic_bad
                mCheckIcon?.setImageDrawable(ContextCompat.getDrawable(context, iconRes))
            }
        }
    }
}