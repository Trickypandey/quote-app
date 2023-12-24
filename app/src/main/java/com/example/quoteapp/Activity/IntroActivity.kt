package com.example.quoteapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.quoteapp.Adapters.AdapterPager
import com.example.quoteapp.R
import com.example.quoteapp.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_intro)
        binding.viewPager.arrowScroll(1)
        binding.viewPager.adapter= AdapterPager(supportFragmentManager)
        binding.dot1.setViewPager(binding.viewPager)

        binding.skip.setOnClickListener {
            val intent=Intent(this,HomePageActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}