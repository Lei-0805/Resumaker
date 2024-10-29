package com.example.resumaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AwardAdapter(private val awardsList: MutableList<Award>) : RecyclerView.Adapter<AwardAdapter.AwardViewHolder>() {

    class AwardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvAchievement: TextView = itemView.findViewById(R.id.tv_award)
        private val tvAwardDescription: TextView = itemView.findViewById(R.id.tv_award_description)

        fun bind(award:Award) {
            tvAchievement.text = award.award
            tvAwardDescription.text = award.awardDescription
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_award, parent, false)
        return AwardViewHolder(view)
    }

    override fun onBindViewHolder(holder: AwardViewHolder, position: Int) {
        val award = awardsList[position]
        holder.bind(award)
    }

    override fun getItemCount(): Int = awardsList.size
}

