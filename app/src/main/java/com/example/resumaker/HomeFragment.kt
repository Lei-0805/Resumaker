package com.example.resumaker


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class HomeFragment : Fragment() {

    lateinit var ibtn_createhp: ImageButton
    lateinit var ibtn_created_profilehp: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        ibtn_createhp = view.findViewById(R.id.ibtn_createhp)
        ibtn_created_profilehp = view.findViewById(R.id.ibtn_created_profilehp)

        ibtn_createhp.setOnClickListener {
            val intent = Intent(requireContext(), createpage::class.java)
            startActivity(intent)
        }
        ibtn_created_profilehp.setOnClickListener {
            val intent = Intent(requireContext(), createdprofiles::class.java)
            startActivity(intent)
        }
        return view
    }
}