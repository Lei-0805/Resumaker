package com.example.resumaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EducationAdapter(private val educationList: MutableList<Education>) : RecyclerView.Adapter<EducationAdapter.EducationViewHolder>() {

    // ViewHolder class to hold item views
    inner class EducationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProgram: TextView = itemView.findViewById(R.id.tv_program)
        val tvSchool: TextView = itemView.findViewById(R.id.tv_school)
        val tvSchoolYear: TextView = itemView.findViewById(R.id.tv_school_year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_education, parent, false)
        return EducationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val education = educationList[position]
        holder.tvProgram.text = education.program
        holder.tvSchool.text = education.school
        holder.tvSchoolYear.text = education.schoolYear
    }

    override fun getItemCount(): Int {
        return educationList.size
    }
}
