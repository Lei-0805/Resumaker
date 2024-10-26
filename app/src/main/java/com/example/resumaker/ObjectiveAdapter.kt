package com.example.resumaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ObjectiveAdapter(private val objectiveList: MutableList<Objective>) : RecyclerView.Adapter<ObjectiveAdapter.ObjectiveViewHolder>() {

    class ObjectiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvObjectiveText: TextView = itemView.findViewById(R.id.tv_objective_text)

        fun bind(objective: Objective) {
            tvObjectiveText.text = objective.objectiveText
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectiveViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_objective, parent, false)
        return ObjectiveViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObjectiveViewHolder, position: Int) {
        val objective = objectiveList[position]
        holder.bind(objective)
    }

    override fun getItemCount(): Int = objectiveList.size
}
