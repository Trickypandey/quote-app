package com.example.quoteapp.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    lateinit var binding:ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utililty().disableSystemFontScaling(this)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_about)
        binding.back.setOnClickListener {
            finish()
        }
    }
    override fun onResume() {
        super.onResume()
        Utililty().updateTextSizeRecursive(binding.root)
    }
}