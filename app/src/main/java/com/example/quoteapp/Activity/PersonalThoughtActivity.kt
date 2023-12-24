package com.example.quoteapp.Activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.room.Room
import com.example.quoteapp.Database.ThoughtsDatabase
import com.example.quoteapp.Database.ThoughtsRepository
import com.example.quoteapp.ModelClass.ModelClassThoughts
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.ActivityPersonalThoughtBinding
import com.example.quoteapp.databinding.LayoutCustomDailogeBinding
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PersonalThoughtActivity : AppCompatActivity() {
    var type=""
    lateinit var thoughtsDatabase: ThoughtsDatabase
    lateinit var thoughtsRepository: ThoughtsRepository
    lateinit var binding:ActivityPersonalThoughtBinding
    lateinit var allEvent: List<ModelClassThoughts>
    var flag = -1
    var saveflag=false
    var updateFlag=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Utililty().disableSystemFontScaling(this)
        // to change the color of status `bar`
//        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
//        this.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        binding=DataBindingUtil.setContentView(this,R.layout.activity_personal_thought)
        flag=intent.getIntExtra("flag",-1)
        thoughtsDatabase = Room.databaseBuilder(
            this,
            ThoughtsDatabase::class.java,
            "thoughts_database")
            .allowMainThreadQueries().build()

        thoughtsRepository = ThoughtsRepository(thoughtsDatabase.thoughtsDao())
        binding.datetime.text=getCurrentDateTime()


        if (flag==0){
            type="morning"
            val data = thoughtsRepository.getThoughtsByDatetype(LocalDate.now().toString(),type)
            if (data.isNotEmpty()){
                updateFlag=true
                binding.thought.setText(data[0].thought)
            }
        }
        if (flag==1){
            type="evening"
            val data = thoughtsRepository.getThoughtsByDatetype(LocalDate.now().toString(),type)
            if (data.isNotEmpty()){
                updateFlag=true
                binding.thought.setText(data[0].thought)
            }
        }



        //INSERT into thoughts values(2,"have a good day","evening","2023-06-02")

        allClickListener()


    }

    fun allClickListener(){
        binding.back.setOnClickListener {
            if (saveflag){
                finish()
            }
            else{
                dailogeBox()
            }
        }

        binding.done.setOnClickListener {

            if (binding.thought.text.isBlank()) {
                Toast.makeText(this,"Thought Must Not Be Empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (updateFlag){
                if (flag==0){
                    thoughtsRepository.updateThought(binding.thought.text.toString(),LocalDate.now().toString(),"morning")
                    binding.thought.text.clear()
                    saveflag=true
                    Toast.makeText(this,getString(R.string.thoughts_change_successfully),Toast.LENGTH_SHORT).show()
                    finish()
                }
                if (flag==1){
                    thoughtsRepository.updateThought(binding.thought.text.toString(),LocalDate.now().toString(),"evening")
                    binding.thought.text.clear()
                    Toast.makeText(this,getString(R.string.thoughts_change_successfully),Toast.LENGTH_SHORT).show()
                    saveflag=true
                    finish()
                }

            }
            else{
                if (flag==0){
                    insertThoughts(binding.thought.text.toString(),"morning")
                    binding.thought.text.clear()
                    saveflag=true
                    Toast.makeText(this,getString(R.string.thoughts_change_successfully),Toast.LENGTH_SHORT).show()
                    finish()
                }
                if (flag==1){
                    insertThoughts(binding.thought.text.toString(),"evening")
                    saveflag=true
                    binding.thought.text.clear()
                    Toast.makeText(this,getString(R.string.thoughts_change_successfully),Toast.LENGTH_SHORT).show()
                    finish()
                }

            }


        }

    }

    fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy h:mm a", Locale.ENGLISH)
        return currentDateTime.format(formatter)
    }
    private fun insertThoughts(thought:String, type: String) {
            thoughtsRepository.insert(
                ModelClassThoughts(
                    0,
                    thought,
                    type,
                    LocalDate.now().toString()
                )
            )
    }
//    override fun onResume() {
//        super.onResume()
//        allEvent  = thoughtsRepository.getThoughtsByDate(LocalDate.now().toString())
//    }

    private fun dailogeBox(){
        val alertDialog = Dialog(this)
        val factory = LayoutInflater.from(this)
        val bindingAlert: LayoutCustomDailogeBinding = DataBindingUtil.inflate(
            factory,
            R.layout.layout_custom_dailoge, null, false
        )
        bindingAlert.title.text = getString(R.string.wont_be_add)
        bindingAlert.body.text = getString(R.string.dont_save_title)
        alertDialog.setContentView(bindingAlert.root)
        alertDialog.show()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog.window?.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        bindingAlert.noButton.setOnClickListener {

            alertDialog.dismiss()

        }
        bindingAlert.yesButton.setOnClickListener {

            finish()
            alertDialog.dismiss()

        }
    }
    override fun onBackPressed() {
        if (saveflag) {
            super.onBackPressed()
        } else {
            dailogeBox()
        }
    }
    override fun onResume() {
        super.onResume()
        Utililty().updateTextSizeRecursive(binding.thought)
    }

}