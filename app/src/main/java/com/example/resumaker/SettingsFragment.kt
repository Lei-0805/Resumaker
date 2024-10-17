import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.resumaker.R

class SettingsFragment : Fragment() {

    private lateinit var btn_import: Button
    private lateinit var btn_export: Button
    private lateinit var btn_log_out: Button
    private val FILE_SELECT_CODE = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        btn_import = view.findViewById(R.id.btn_import)
        btn_export = view.findViewById(R.id.btn_export)
        btn_log_out = view.findViewById(R.id.btn_log_out)

        btn_import.setOnClickListener {
            openFileManager()
        }

        btn_export.setOnClickListener {
            // Handle the export button click
        }

        btn_log_out.setOnClickListener {
            showLogoutDialog()
        }

        return view
    }

    private fun openFileManager() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        try {
            startActivityForResult(
                Intent.createChooser(intent, "Select a file to import"),
                FILE_SELECT_CODE
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == FILE_SELECT_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri: Uri ->
                // Handle the file URI here, e.g., upload or process the file
                println("File Uri: $uri")
            }
        }
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

    private fun logoutUser() {
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("userSession", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Close all activities and exit the app
        requireActivity().finishAffinity()
    }
}
