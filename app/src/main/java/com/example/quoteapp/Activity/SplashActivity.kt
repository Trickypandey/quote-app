package com.example.quoteapp.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sh = getSharedPreferences(Constants.loginCheck, MODE_PRIVATE)
        val login = sh.getBoolean(Constants.loginFlag,false)
        Handler(Looper.getMainLooper()).postDelayed({
            if (login){
                startActivity(Intent(this,HomePageActivity::class.java))
            }else{
                startActivity(Intent(this,IntroActivity::class.java))
            }
            finish()
        }, 2000)
    }
}