package com.example.resumaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExperienceAdapter(private val experienceList: MutableList<Experience>) : RecyclerView.Adapter<ExperienceAdapter.ExperienceViewHolder>() {

    inner class ExperienceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCompany: TextView = itemView.findViewById(R.id.tv_company_name)
        val tvJobTitle: TextView = itemView.findViewById(R.id.tv_job_title)
        val tvStartDate: TextView = itemView.findViewById(R.id.tv_start_date)
        val tvEndDate: TextView = itemView.findViewById(R.id.tv_end_date)
        val tvDetails: TextView = itemView.findViewById(R.id.tv_details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExperienceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_experience, parent, false)
        return ExperienceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExperienceViewHolder, position: Int) {
        val experience = experienceList[position]
        holder.tvCompany.text = experience.company
        holder.tvJobTitle.text = experience.jobTitle
        holder.tvStartDate.text = experience.startDate
        holder.tvEndDate.text = experience.endDate
        holder.tvDetails.text = experience.details
    }

    override fun getItemCount(): Int {
        return experienceList.size
    }
}
