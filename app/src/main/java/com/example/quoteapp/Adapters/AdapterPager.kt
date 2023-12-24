package com.example.quoteapp.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.quoteapp.Fragment.Intro1Fragment
import com.example.quoteapp.Fragment.Intro2Fragment
import com.example.quoteapp.Fragment.Intro3Fragment

class AdapterPager(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
      return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {
                Intro1Fragment()
            }
            1 -> {
                Intro2Fragment()
            }
            else->{
                Intro3Fragment()
            }
        }
    }
}