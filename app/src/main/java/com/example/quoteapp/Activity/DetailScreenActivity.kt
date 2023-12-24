package com.example.quoteapp.Activity

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.example.quoteapp.Database.ThoughtsDatabase
import com.example.quoteapp.Database.ThoughtsRepository
import com.example.quoteapp.ModelClass.MOdelClassQuotesResponse
import com.example.quoteapp.ModelClass.ModelClassSearchPayload
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants
import com.example.quoteapp.Utils.ProgressUtil
import com.example.quoteapp.Utils.RetrofitHelper
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.ActivityDetailScreenBinding
import retrofit2.Call
import retrofit2.Response
import java.util.*

class DetailScreenActivity : AppCompatActivity() {
    lateinit var binding:ActivityDetailScreenBinding
    lateinit var thoughtsDatabase: ThoughtsDatabase
    lateinit var thoughtsRepository: ThoughtsRepository
    private lateinit var progressUtil: ProgressUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_detail_screen)
        thoughtsDatabase = Room.databaseBuilder(
            this,
            ThoughtsDatabase::class.java,
            "thoughts_database").allowMainThreadQueries().build()
        progressUtil = ProgressUtil(this)



        thoughtsRepository = ThoughtsRepository(thoughtsDatabase.thoughtsDao())
        val data:ModelClassSearchPayload? = intent.getSerializableExtra("data") as? ModelClassSearchPayload
        val fact:String? = intent.getStringExtra("fact")
        val date:String? = intent.getStringExtra("date")
        binding.back.setOnClickListener {
            finish()
        }
        if (data != null) {
            binding.title.text=formatDate(data.date)
            if (data.response_type=="quotes"){
                binding.titleall.text="Quote"
                binding.quotecard.visibility=View.VISIBLE
                binding.quote.text=data.quote
                binding.writer.text=data.name
                binding.pptext.text=data.personal_perspective
                binding.biotext.text=data.bio
                getPersonalTHought(data.date)
            }
        }
        if (fact != null){
            binding.titleall.text="Fact"
            binding.factcard.visibility=View.VISIBLE
            binding.fact.text=fact
        }
        if (date != null){
            binding.title.text= formatDate(date)
            progressUtil.showProgress()
            apiCall(date)
            getPersonalTHought(date)

        }

    }
    override fun onResume() {
        super.onResume()
     Utililty().updateTextSizeRecursive(window.decorView.rootView)
    }

    fun formatDate(inputDate: String): String {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "EEE, dd MMM yyyy"

        val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(outputPattern, Locale.ENGLISH)

        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
    private fun apiCall(date: String) {
        binding.titleall.text= "Quote"
        binding.quotecard.visibility=View.VISIBLE
        val quote = RetrofitHelper().retrofitobj()
        quote.Quotes(Constants.token, date)
            .enqueue(object : retrofit2.Callback<MOdelClassQuotesResponse> {
                override fun onResponse(
                    call: Call<MOdelClassQuotesResponse>,
                    response: Response<MOdelClassQuotesResponse>
                ) {
                    if (response.isSuccessful && response.body()?.payload != null) {
                        progressUtil.hideProgress()
                        val payload = response.body()!!.payload
                        binding.quote.text = payload.quote
                        binding.writer.text = payload.name + " "
                        binding.pptext.text = payload.personal_perspective
                        binding.biotext.text = payload.bio
                    } else {
                        progressUtil.hideProgress()
                        Toast.makeText(
                            this@DetailScreenActivity,
                            response.body()?.message ?: "Unknown error occurred",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MOdelClassQuotesResponse>, t: Throwable) {
                    progressUtil.hideProgress()
                    Toast.makeText(
                        this@DetailScreenActivity,
                        getString(R.string.some_error_occur),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }
    fun getPersonalTHought(date: String){
        val personal=thoughtsRepository.getThoughtsByDate(date)

        if (personal.isNotEmpty()){
            for (i in personal){
                if (i.thoughtType=="morning"){
                    binding.personalmorning.visibility=View.VISIBLE
                    binding.personalmorningLL.visibility=View.VISIBLE
                    binding.line4.visibility=View.VISIBLE
                    binding.personalmorning.text=i.thought
                }
                if (i.thoughtType=="evening"){
                    binding.personalevening.visibility=View.VISIBLE
                    binding.personaleveningLL.visibility=View.VISIBLE
                    binding.personalevening.text=i.thought
                    binding.line5.visibility=View.VISIBLE
                }

            }

        }else{
            binding.personalmorning.visibility=View.GONE
            binding.personalmorningLL.visibility=View.GONE
            binding.personalevening.visibility=View.GONE
            binding.personaleveningLL.visibility=View.GONE
        }



    }
}