package com.example.quoteapp.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.ActivityAppearanceBinding
import com.example.quoteapp.databinding.LayoutCustomDailogeBinding

class AppearanceActivity : AppCompatActivity() {
    lateinit var binding:ActivityAppearanceBinding
    var textSize =Constants.textSize
    var saveflag=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_appearance)
        Utililty().disableSystemFontScaling(this)
        binding.back.setOnClickListener {
            if (saveflag) {
               finish()
            } else {
                dailogeBox(
                    getString(R.string.changes_dont_save),
                    getString(R.string.changes_dont_save_body),
                    0
                )
            }
        }
        if (Constants.textSize==16f){
            binding.reguler.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_checked)
            binding.reguler.setTextColor(resources.getColor(R.color.black))
            binding.large.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.large.setTextColor(resources.getColor(R.color.about_text_color))
            binding.extralarge.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.extralarge.setTextColor(resources.getColor(R.color.about_text_color))

        }
        if (Constants.textSize==22f){
            binding.reguler.setTextColor(resources.getColor(R.color.about_text_color))
            binding.large.setTextColor(resources.getColor(R.color.black))
            binding.extralarge.setTextColor(resources.getColor(R.color.about_text_color))
            binding.reguler.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.large.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_checked)
            binding.extralarge.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
        }
        if (Constants.textSize==26f){
            binding.reguler.setTextColor(resources.getColor(R.color.about_text_color))
            binding.large.setTextColor(resources.getColor(R.color.about_text_color))
            binding.extralarge.setTextColor(resources.getColor(R.color.black))
            binding.reguler.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.large.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.extralarge.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_checked)
        }
        changeFont()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun changeFont() {
        binding.reguler.setOnClickListener {
            binding.reguler.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_checked)
            binding.reguler.setTextColor(resources.getColor(R.color.black))
            binding.large.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.large.setTextColor(resources.getColor(R.color.about_text_color))
            binding.extralarge.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.extralarge.setTextColor(resources.getColor(R.color.about_text_color))
            textSize=16f
            updateTextSize()
        }
        binding.large.setOnClickListener {
            binding.reguler.setTextColor(resources.getColor(R.color.about_text_color))
            binding.large.setTextColor(resources.getColor(R.color.black))
            binding.extralarge.setTextColor(resources.getColor(R.color.about_text_color))
            binding.reguler.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.large.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_checked)
            binding.extralarge.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            textSize =22f
            updateTextSize()
        }
        binding.extralarge.setOnClickListener {
            binding.reguler.setTextColor(resources.getColor(R.color.about_text_color))
            binding.large.setTextColor(resources.getColor(R.color.about_text_color))
            binding.extralarge.setTextColor(resources.getColor(R.color.black))
            binding.reguler.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.large.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_notchecked)
            binding.extralarge.background=resources.getDrawable(R.drawable.backgroud_black_radius_r_checked)
            textSize =26f // Increase text size by 2
            updateTextSizeRecursive(binding.chapter1Text)
        }
        binding.applychanges.setOnClickListener {

            dailogeBox(getString(R.string.appearance_title), getString(R.string.appearance_body), 1)
        }
    }
    private fun dailogeBox(title: String, body: String, i: Int){

        val alertDialog = Dialog(this)
        val factory = LayoutInflater.from(this)
        val bindingAlert:LayoutCustomDailogeBinding = DataBindingUtil.inflate(
            factory,
            R.layout.layout_custom_dailoge, null, false
        )
        bindingAlert.title.text = title
        bindingAlert.body.text = body

        alertDialog.setContentView(bindingAlert.root)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingAlert.noButton.setOnClickListener {
           saveflag=false
            alertDialog.dismiss()

        }
        bindingAlert.yesButton.setOnClickListener {
            if (i==1)
            {
                Constants.textSize=textSize
                updateTextSize()
                saveflag=true
                alertDialog.dismiss()
                finish()
            }else{
                finish()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        Utililty().updateTextSizeRecursive(binding.chapter1Text)
    }
    private fun updateTextSize() {
        updateTextSizeRecursive(binding.chapter1Text)
    }

    private fun updateTextSizeRecursive(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                updateTextSizeRecursive(child)
            }
        } else if (view is TextView) {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        }
    }

    override fun onBackPressed() {
        if (saveflag) {
            super.onBackPressed()
        } else {
            dailogeBox(getString(R.string.changes_dont_save),getString(R.string.changes_dont_save_body),0)
        }
    }

}