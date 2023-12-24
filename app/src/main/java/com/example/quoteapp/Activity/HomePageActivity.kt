package com.example.quoteapp.Activity

import CalenderFragment
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.quoteapp.Fragment.*
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.ActivityHomePageBinding


class HomePageActivity : AppCompatActivity() {
    lateinit var binding :ActivityHomePageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this, com.example.quoteapp.R.layout.activity_home_page)
        Utililty().disableSystemFontScaling(this)
        val sh = getSharedPreferences(Constants.loginCheck, MODE_PRIVATE)
        val edit = sh.edit()
        edit.putBoolean(Constants.loginFlag,true)
        edit.commit()
        addOrShowFragment(HomeFragment())
        allclick()
    }

    fun allclick(){
        binding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_item_1 -> {
                    // Handle item 1 click
                    addOrShowFragment(HomeFragment())

                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_2 -> {
                    // Handle item 2 click
                    addOrShowFragment(FactsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_3 -> {
                    // Handle item 3 click
                    addOrShowFragment(CalenderFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_4 -> {
                    // Handle item 4 click
                    addOrShowFragment(SearchFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.menu_item_5 -> {
                    // Handle item 5 click
                    addOrShowFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

    }

    private fun addOrShowFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTag = fragment.javaClass.simpleName
        val currentFragment1 = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)
        val currentFragment2 = fragmentManager.findFragmentByTag(FactsFragment::class.java.simpleName)
        val currentFragment3 = fragmentManager.findFragmentByTag(CalenderFragment::class.java.simpleName)
        val currentFragment4 = fragmentManager.findFragmentByTag(SearchFragment::class.java.simpleName)
        val currentFragment5 = fragmentManager.findFragmentByTag(SettingsFragment::class.java.simpleName)

        val transaction = fragmentManager.beginTransaction()

        if (currentFragment1 != null && currentFragment1.isVisible) {

            if (fragmentTag == currentFragment1.tag) {

                // Fragment already added and visible, do nothing
                return

            }

            transaction.hide(currentFragment1)

        }

        if (currentFragment2 != null && currentFragment2.isVisible) {
            if (fragmentTag == currentFragment2.tag) {

                // Fragment already added and visible, do nothing
                return

            }

            transaction.hide(currentFragment2)

        }

        if (currentFragment3 != null && currentFragment3.isVisible) {
            if (fragmentTag == currentFragment3.tag) {

                // Fragment already added and visible, do nothing
                return

            }

            transaction.hide(currentFragment3)

        }
        if (currentFragment4 != null && currentFragment4.isVisible) {
            if (fragmentTag == currentFragment4.tag) {

                // Fragment already added and visible, do nothing
                return

            }

            transaction.hide(currentFragment4)

        }
        if (currentFragment5 != null && currentFragment5.isVisible) {
            if (fragmentTag == currentFragment5.tag) {

                // Fragment already added and visible, do nothing
                return

            }

            transaction.hide(currentFragment5)

        }

        val existingFragment = fragmentManager.findFragmentByTag(fragmentTag)

        if (existingFragment != null) {

            // Fragment is already added, so show it
            transaction.show(existingFragment)

        } else {

            // Fragment is not added, so add it
            transaction.add(R.id.frame_layout, fragment, fragmentTag)

        }
//        if (){
//                val window = window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = Color.BLACK
//
//        }
//        else{
//            val window = window
//
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            window.statusBarColor = Color.WHITE
//
////        }
//        val decorView = window.decorView
//        var flags = decorView.systemUiVisibility
//
//        flags = if (fragmentTag==currentFragment1?.tag|| fragmentTag==currentFragment2?.tag) {
//            // Dark theme
//            flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//        } else {
//            // Light theme
//            flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
//        }
//
//        decorView.systemUiVisibility = flags
        transaction.commit()
    }

    override fun onResume() {
        super.onResume()


    }
}