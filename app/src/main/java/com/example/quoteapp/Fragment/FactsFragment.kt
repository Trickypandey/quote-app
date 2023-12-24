package com.example.quoteapp.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.quoteapp.ModelClass.MOdelClassFactResponse
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants
import com.example.quoteapp.Utils.ProgressUtil
import com.example.quoteapp.Utils.RetrofitHelper
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.FragmentFactsBinding
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class FactsFragment : Fragment() {
    lateinit var binding:FragmentFactsBinding
    private lateinit var progressUtil: ProgressUtil
    override fun onResume() {
        super.onResume()
        Utililty().updateTextSizeRecursive(binding.fact)
        // Optionally, change the status bar color
//        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Optionally, change the status bar color
//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_facts, container, false)
        progressUtil= ProgressUtil(requireActivity())
        progressUtil.showProgress()
        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH)
        val formattedDate = formatter.format(LocalDate.now())
        binding.date.text=formattedDate

        Calendar.getInstance().time

        val dayOfWeekString = Calendar.getInstance().getDisplayName(
            Calendar.DAY_OF_WEEK,
            Calendar.LONG,
            Locale.getDefault()
        )
        binding.day.text = dayOfWeekString

        setClickListeners()
        apiCall()

        return binding.root
    }

    fun setClickListeners(){
        binding.refresh.setOnClickListener {
            progressUtil.showProgress()
            apiCall()
        }

    }

    fun apiCall(){
        val fact = RetrofitHelper().retrofitobj()
        fact.Fact(Constants.token).enqueue(object:retrofit2.Callback<MOdelClassFactResponse>{
            override fun onResponse(
                call: Call<MOdelClassFactResponse>,
                response: Response<MOdelClassFactResponse>
            ) {
                if (response.isSuccessful && response.body()?.payload != null) {
                    binding.homecard.visibility=View.VISIBLE
                    progressUtil.hideProgress()
                    val payload = response.body()!!.payload
                    binding.fact.text=payload.fact
                } else {
                    progressUtil.hideProgress()
                    Toast.makeText(
                        requireContext(),
                        response.body()?.message ?: "Unknown error occurred",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<MOdelClassFactResponse>, t: Throwable) {
                progressUtil.hideProgress()
                Toast.makeText(
                    requireContext(),
                    getString(R.string.some_error_occur),
                    Toast.LENGTH_SHORT
                ).show()            }

        })
    }
    override fun onPause() {
        super.onPause()
        progressUtil.hideProgress()
    }
}