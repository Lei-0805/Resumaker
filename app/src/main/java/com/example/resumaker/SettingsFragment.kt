package com.example.resumaker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    private lateinit var btn_created_profiles: Button
    private lateinit var btn_log_out: Button
    private val sharedPrefFile = "UserPrefs"

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        btn_log_out = view.findViewById(R.id.btn_log_out)
        btn_created_profiles = view.findViewById(R.id.btn_created_profile)

        btn_created_profiles.setOnClickListener {
            // Navigate to CreatedProfilesFragment or CreatedProfilesActivity
            navigateToCreatedProfiles()
        }

        btn_log_out.setOnClickListener {
            showLogoutDialog()
        }

        return view
    }

    private fun navigateToCreatedProfiles() {
        val intent = Intent(requireContext(), createdprofiles::class.java)
        startActivity(intent)
    }


    private fun showLogoutDialog() {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.logout_dialogbox)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawableResource(R.drawable.logout_dialog_bg)

        dialog.setCancelable(false)

        val btnDialogYes: Button = dialog.findViewById(R.id.btn_yes)
        val btnDialogNo: Button = dialog.findViewById(R.id.btn_no)

        btnDialogYes.setOnClickListener {
            dialog.dismiss()
            logoutUser()
        }

        btnDialogNo.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    // In SettingsFragment
    private fun logoutUser() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("is_logged_in", false)
        editor.putBoolean("is_signed_in", false)
        editor.putBoolean("is_setup_complete", false) // Clear setup completion if needed
        editor.apply()

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Close all activities and exit the app
        requireActivity().finishAffinity()
    }
}