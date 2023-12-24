import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.quoteapp.Adapters.AdapterRvQuote
import com.example.quoteapp.Database.ThoughtsDatabase
import com.example.quoteapp.Database.ThoughtsRepository
import com.example.quoteapp.ModelClass.MOdelClassQuotesResponse
import com.example.quoteapp.ModelClass.ModelClassQuotePayload
import com.example.quoteapp.ModelClass.ModelClassThoughts
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants
import com.example.quoteapp.Utils.ProgressUtil
import com.example.quoteapp.Utils.RetrofitHelper
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.FragmentCalenderBinding
import com.shrikanthravi.collapsiblecalendarview.data.Day
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


class CalenderFragment : Fragment() {
    private lateinit var binding: FragmentCalenderBinding
    private var isExpanded: Boolean = false
    lateinit var thoughtsDatabase: ThoughtsDatabase
    lateinit var thoughtsRepository: ThoughtsRepository
    lateinit var allEvent: List<ModelClassThoughts>
    private lateinit var progressUtil: ProgressUtil
    val list= arrayListOf<ModelClassQuotePayload>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_calender, container, false
        )
//        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR

        // Optionally, change the status bar color
//        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)
        thoughtsDatabase = Room.databaseBuilder(
            requireContext(),
            ThoughtsDatabase::class.java,
            "thoughts_database")
            .allowMainThreadQueries().build()

        thoughtsRepository = ThoughtsRepository(thoughtsDatabase.thoughtsDao())
        progressUtil= ProgressUtil(requireActivity())
        progressUtil.showProgress()

        // setelcting todays date
        val currentDate = Calendar.getInstance()
        val selectedItem = Day(currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH))
        binding.calendarView.select(selectedItem)




//        // setting all days which have personal thoughts
//        TodaysEvent()

//        Log.e("data",todaysThought.toString())
//        list.add(Constants.todaysThoughts)


//        binding.quote.text=Constants.todaysThoughts.quote
//        binding.title.text=Constants.todaysThoughts.name

        binding.calendarView.setCalendarListener(object : CollapsibleCalendar.CalendarListener {
            override fun onDaySelect() {
                val date = formatDate("${binding.calendarView.selectedDay!!.year }-${binding.calendarView.selectedDay!!.month + 1 }-${binding.calendarView.selectedDay!!.day}")
                var hasMatch = false
                progressUtil.showProgress()
                for (i in allEvent) {
                    if (date == formatDate(i.date)) {
                        hasMatch = true
                        apiCall(date,0)
                    }
                }

                if (!hasMatch) {
                    apiCall(date, 1)
                }
            }

            override fun onItemClick(view: View) {

            }

            override fun onDataUpdate() {}

            override fun onMonthChange() {}

            override fun onWeekChange(i: Int) {}

            override fun onClickListener() {

            }

            override fun onDayChanged() {}
        })
//        setAdaptet(Constants.todaysThoughts, 0)
        return binding.root
    }
    fun setAdaptet(payload: ModelClassQuotePayload, flag: Int) {

        list.clear()
        list.add(payload)
        var final = getFieldList(list)
        if (flag==0){
            val value = addPersonalThoughts(payload.date)
            final.addAll(value)

        }
        final.removeIf { (fieldName, _) -> fieldName == "id" || fieldName == "date" || fieldName =="name" || fieldName=="quote"}

        binding.quotecalender.layoutManager=LinearLayoutManager(requireContext())
        binding.quotecalender.adapter=AdapterRvQuote(final)

    }

    fun setEvent(list:Calendar){
        binding.calendarView.addEventTag(list.get(Calendar.YEAR),list.get(Calendar.MONTH),list.get(Calendar.DAY_OF_MONTH),R.color.event_tag_color)
    }
    fun setDate() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        for (i in allEvent){

            val date = dateFormat.parse(i.date)
            calendar.time = date
            setEvent(calendar)
        }
    }
    fun formatDate(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        val date = LocalDate.parse(inputDate, inputFormatter)
        return date.format(outputFormatter)
    }
    private fun apiCall(date:String,flag:Int) {
        val quote = RetrofitHelper().retrofitobj()
        quote.Quotes(Constants.token, date)
            .enqueue(object : retrofit2.Callback<MOdelClassQuotesResponse> {
                override fun onResponse(
                    call: Call<MOdelClassQuotesResponse>,
                    response: Response<MOdelClassQuotesResponse>
                ) {
                    if (response.isSuccessful && response.body()?.payload != null) {
                        progressUtil.hideProgress()
                        binding.title.text= response.body()?.payload!!.name+" "
                        binding.quote.text= response.body()?.payload!!.quote
                        binding.alldata.visibility=View.VISIBLE
                        binding.noData.visibility=View.GONE
                        binding.nodataTxt.visibility= View.GONE
                        binding.date.text=formatDateDay(response.body()?.payload!!.date)
                        val payload = response.body()!!.payload
                        setAdaptet(payload,flag)

                    } else {
                        progressUtil.hideProgress()
                        binding.alldata.visibility=View.GONE
                        binding.noData.visibility=View.VISIBLE
                        binding.nodataTxt.visibility= View.VISIBLE
                        Toast.makeText(
                            requireContext(),
                            response.body()?.message ?: "Unknown error occurred",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<MOdelClassQuotesResponse>, t: Throwable) {
                    progressUtil.hideProgress()
                    binding.alldata.visibility=View.GONE
                    binding.noData.visibility=View.VISIBLE
                    binding.nodataTxt.visibility= View.VISIBLE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.some_error_occur),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
    }

    fun formatDateDay(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())

        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }



    private fun getFieldList(data: ArrayList<ModelClassQuotePayload>): ArrayList<Pair<String, String>> {
        val fieldList = ArrayList<Pair<String, String>>()

        for (item in data) {
            val itemFields = item.javaClass.declaredFields

            for (field in itemFields) {
                field.isAccessible = true
                val fieldName = field.name
                val fieldValue = field.get(item)?.toString()
                fieldList.add(Pair(fieldName, fieldValue) as Pair<String, String>)

            }
        }

        return fieldList
    }

    fun addPersonalThoughts(date: String): ArrayList<Pair< String, String>> {
        val morning = thoughtsRepository.getThoughtsByDatetype(date,"morning")
        val evening = thoughtsRepository.getThoughtsByDatetype(date,"evening")
        val list = arrayListOf<Pair<String, String>>()

        for (i in morning) {
            if (i.thought.isNotBlank()) {
                list.add(Pair(getString(R.string.tittle_morning), i.thought))
            }
        }
        for (i in evening) {
            if (i.thought.isNotBlank()) {
                list.add(Pair(getString(R.string.title_evening), i.thought))
            }
        }

        return list
    }

    override fun onResume() {
        super.onResume()
        allEvent  = thoughtsRepository.getAllThoughtsGivenDate()
        Utililty().updateTextSizeRecursive(binding.root)
        setDate()
    }

    override fun onStart() {
        super.onStart()

        apiCall(LocalDate.now().toString(),0)
    }
    override fun onPause() {
        super.onPause()
        progressUtil.hideProgress()
    }

}
