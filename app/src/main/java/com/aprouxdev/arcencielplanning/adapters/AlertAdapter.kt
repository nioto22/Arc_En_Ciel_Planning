package com.aprouxdev.arcencielplanning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.aprouxdev.arcencielplanning.R
import com.aprouxdev.arcencielplanning.models.Alert

interface AlertCallback {
    fun onAlertClicked(alert: Alert)
}

class AlertAdapter(var data: List<Alert>, private val listener: AlertCallback) : RecyclerView.Adapter<AlertAdapter.AlertViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        return AlertViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_view_home_alert, parent, false)
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) =
        holder.bind(data[position], listener)

    fun updateData(data: List<Alert>) {
        this.data = data
        notifyDataSetChanged()
    }

    class AlertViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var mIcon: AppCompatImageView? = itemView.findViewById(R.id.alert_icon)
        private var mAlertTypeTv: AppCompatTextView? = itemView.findViewById(R.id.alert_type_tv)
        private var mAlertTitle: AppCompatTextView? = itemView.findViewById(R.id.alert_title_tv)
        private var mAlertBody: AppCompatTextView? = itemView.findViewById(R.id.alert_body_tv)
        private var mAlertButton: AppCompatImageButton? = itemView.findViewById(R.id.alert_button)

        fun bind(item: Alert, listener: AlertCallback) = with(itemView) {
            itemView.background = ContextCompat.getDrawable(context, item.type.background)

            mIcon?.setImageDrawable(ContextCompat.getDrawable(context, item.type.icon))
            mAlertTypeTv?.text = context.getString(item.type.text)

            mAlertTitle?.text = item.title
            mAlertBody?.text = item.body

            mAlertButton?.setImageDrawable(ContextCompat.getDrawable(context, item.type.chevronIcon))
            mAlertButton?.setOnClickListener {
                listener.onAlertClicked(item)
            }
            itemView.setOnClickListener {
                listener.onAlertClicked(item)
            }
        }
    }
}