package com.example.quoteapp.Fragment


import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.quoteapp.Activity.AboutActivity
import com.example.quoteapp.Activity.AppearanceActivity
import com.example.quoteapp.Activity.ReminderActivity
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.FragmentSettingsBinding
import com.example.quoteapp.databinding.LayoutCustomDailogeBinding

class SettingsFragment : Fragment() {
lateinit var binding:FragmentSettingsBinding
val util=Utililty()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)
        allclick()

        return binding.root
    }

    private fun allclick() {
        binding.groupAbout.setOnClickListener {
            val intent= Intent(requireContext(),AboutActivity::class.java)
            activity?.startActivity(intent)
        }
        binding.groupReminder.setOnClickListener {
            val intent= Intent(requireContext(),ReminderActivity::class.java)
            activity?.startActivity(intent)
        }
        binding.groupAppearance.setOnClickListener {
            val intent= Intent(requireContext(),AppearanceActivity::class.java)
            activity?.startActivity(intent)
        }
        binding.groupExport.setOnClickListener {
            dailogeBox()
        }
    }


    private fun dailogeBox(){
        val alertDialog = Dialog(requireContext())
        val factory = LayoutInflater.from(requireContext())
        val bindingAlert: LayoutCustomDailogeBinding = DataBindingUtil.inflate(
            factory,
            R.layout.layout_custom_dailoge, null, false
        )
        bindingAlert.title.setText(R.string.export_data_title)
        bindingAlert.body.setText(R.string.export_data_body)
        alertDialog.setContentView(bindingAlert.root)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            bindingAlert.noButton.setOnClickListener {

            alertDialog.dismiss()

        }
        bindingAlert.yesButton.setOnClickListener {

            alertDialog.dismiss()

        }
    }
}