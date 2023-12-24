package com.example.quoteapp.Utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.text.SimpleDateFormat
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import com.example.quoteapp.Activity.AboutActivity
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants.Companion.temp
import com.example.quoteapp.databinding.LayoutCustomDailogeBinding
import java.util.*

class Utililty {

    fun formatDate(dateString: String): String {
        if (dateString.isNullOrBlank()){
            return " "
        }
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
        val date = inputFormat.parse(dateString)

        // Get the current date
        val currentDate = Calendar.getInstance().time

        // Get the previous date
        val previousDate = Calendar.getInstance()
        previousDate.add(Calendar.DAY_OF_YEAR, -1)

        // Check if the date is today or yesterday
        val isToday = isSameDay(date, currentDate)
        val isYesterday = isSameDay(date, previousDate.time)

        return when {
            isToday -> "Today"
            isYesterday -> "Yesterday"
            else -> outputFormat.format(date)
        }
    }

    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date1
        cal2.time = date2

        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }

    fun disableSystemFontScaling(activity: Activity) {
        val configuration: Configuration = activity.getResources().configuration
        configuration.fontScale = 1.0f // Set the desired font scale here

        // Update the configuration
        activity.resources.updateConfiguration(configuration, activity.resources.displayMetrics)
    }

    fun updateTextSizeRecursive(view: View) {
        if (view is ViewGroup) {

            for (i in 0 until view.childCount) {
                val child = view.getChildAt(i)
                updateTextSizeRecursive(child)
            }
        } else if (view is TextView && view.tag=="text_change_size") {
            view.setTextSize(TypedValue.COMPLEX_UNIT_SP, Constants.textSize)
        }
    }



}
class ProgressUtil(private val activity: Activity) {
    private var alertDialog: Dialog? = null

    fun showProgress() {
        if (alertDialog == null) {
            alertDialog = Dialog(activity)
            alertDialog?.setContentView(R.layout.layout_progress)
            alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog?.window?.setLayout(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            Log.e("show the dai ",temp.toString()+"in show")
            temp++
        }
        alertDialog?.show()
    }

    fun hideProgress() {
        if (alertDialog!=null){
            alertDialog?.dismiss()
        }
        Log.e("show the dai ",temp.toString() + "in hide")
        temp++
    }
}