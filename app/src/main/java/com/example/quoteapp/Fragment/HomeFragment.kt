package com.example.quoteapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.quoteapp.Activity.PersonalThoughtActivity
import com.example.quoteapp.ModelClass.MOdelClassQuotesResponse
import com.example.quoteapp.R
import com.example.quoteapp.Utils.*
import com.example.quoteapp.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var progressUtil: ProgressUtil
    private lateinit var connection:NetworkChangeListener
    override fun onResume() {
        super.onResume()

        Utililty().updateTextSizeRecursive(binding.textChange)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.alldata.visibility=View.GONE
        connection= NetworkChangeListener(requireContext())
        connection.start()
        progressUtil = ProgressUtil(requireActivity())
        progressUtil.showProgress()

        val formatter = DateTimeFormatter.ofPattern("dd MMM, yyyy", Locale.ENGLISH)
        Calendar.getInstance().time
        val formattedDate = formatter.format(LocalDate.now())

        val dayOfWeekString = Calendar.getInstance().getDisplayName(
            Calendar.DAY_OF_WEEK,
            Calendar.LONG,
            Locale.getDefault()
        )

        binding.todayDate.text = formattedDate
        binding.todayDay.text = dayOfWeekString

        setClickListeners()
        apiCall()
        return binding.root
    }
    private fun setClickListeners() {
        val intent = Intent(requireContext(), PersonalThoughtActivity::class.java)
        binding.thoughtsOfStartDay.setOnClickListener {
            intent.putExtra("flag",0)
            startActivity(intent)
        }
        binding.thoughtsOfEndDay.setOnClickListener {
            intent.putExtra("flag",1)
            startActivity(intent)
        }
    }

    private fun apiCall() {
        val quote = RetrofitHelper().retrofitobj()
        quote.Quotes(Constants.token, LocalDate.now().toString())
            .enqueue(object : retrofit2.Callback<MOdelClassQuotesResponse> {
                override fun onResponse(
                    call: Call<MOdelClassQuotesResponse>,
                    response: Response<MOdelClassQuotesResponse>
                ) {
                    if (response.isSuccessful && response.body()?.payload != null) {
                        binding.alldata.visibility=View.VISIBLE
                        progressUtil.hideProgress()
                        binding.alldata.visibility=View.VISIBLE
                        binding.noData.visibility=View.GONE
                        binding.nodataTxt.visibility=View.GONE
                        val payload = response.body()!!.payload
                        binding.quote.text = payload.quote
                        binding.writer.text = payload.name + " "
                        binding.pptext.text = payload.personal_perspective
                        binding.biotext.text = payload.bio
//                        Constants.todaysThoughts=payload
//                        Constants.name=payload.name

                    } else {
                        progressUtil.hideProgress()
                        binding.noData.visibility=View.VISIBLE
                        binding.nodataTxt.visibility=View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            response.body()?.message ?: "Unknown error occurred",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MOdelClassQuotesResponse>, t: Throwable) {
                    progressUtil.hideProgress()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.some_error_occur),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        connection.stop()
    }

    override fun onPause() {
        super.onPause()
        progressUtil.hideProgress()
    }

}
