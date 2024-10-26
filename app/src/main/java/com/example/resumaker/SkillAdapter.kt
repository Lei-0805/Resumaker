package com.example.resumaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SkillAdapter(private val skillList: MutableList<Skill>) : RecyclerView.Adapter<SkillAdapter.SkillViewHolder>() {

    class SkillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvSkillName: TextView = itemView.findViewById(R.id.tv_skill_name)

        fun bind(skill: Skill) {
            tvSkillName.text = skill.skillName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SkillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skill, parent, false)
        return SkillViewHolder(view)
    }

    override fun onBindViewHolder(holder: SkillViewHolder, position: Int) {
        val skill = skillList[position]
        holder.bind(skill)
    }

    override fun getItemCount(): Int = skillList.size
}
