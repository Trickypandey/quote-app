package com.example.quoteapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.quoteapp.Activity.HomePageActivity
import com.example.quoteapp.R
import com.example.quoteapp.databinding.FragmentIntro3Binding

class Intro3Fragment : Fragment() {
    lateinit var binding:FragmentIntro3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_intro3, container, false)
        binding.getstart.setOnClickListener {
            val intent=Intent(requireContext(),HomePageActivity::class.java)
            requireActivity().startActivity(intent)
            requireActivity().finish()
        }
        return binding.root
    }
}