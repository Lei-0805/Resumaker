package com.example.resumaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PersonalDetailsAdapter(private val personalDetailsList: MutableList<PersonalDetail>) :
    RecyclerView.Adapter<PersonalDetailsAdapter.PersonalDetailsViewHolder>() {

    class PersonalDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvName: TextView = itemView.findViewById(R.id.tv_name)
        private val tvDesiredJob: TextView = itemView.findViewById(R.id.tv_desiredJob)
        private val tvAddress: TextView = itemView.findViewById(R.id.tv_address)
        private val tvEmail: TextView = itemView.findViewById(R.id.tv_email)
        private val tvPhone: TextView = itemView.findViewById(R.id.tv_phone)
        private val tvLinkedIn: TextView = itemView.findViewById(R.id.tv_linkedin)

        // Bind data to the view elements
        fun bind(personalDetails: PersonalDetail) {
            tvName.text = personalDetails.name
            tvDesiredJob.text = personalDetails.desiredJob
            tvAddress.text = personalDetails.address
            tvEmail.text = personalDetails.email
            tvPhone.text = personalDetails.phone
            tvLinkedIn.text = personalDetails.linkedIn
        }
    }

    //Function to view the saved data via use of view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonalDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_personal_detail, parent, false)
        return PersonalDetailsViewHolder(view)
    }

    //Function to bind the save data to the view holder
    override fun onBindViewHolder(holder: PersonalDetailsViewHolder, position: Int) {
        val personalDetails = personalDetailsList[position]
        holder.bind(personalDetails)
    }

    override fun getItemCount(): Int = personalDetailsList.size
}
