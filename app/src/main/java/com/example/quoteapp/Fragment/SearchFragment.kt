package com.example.quoteapp.Fragment

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.quoteapp.Activity.DetailScreenActivity
import com.example.quoteapp.Adapters.AdapterRvSearch
import com.example.quoteapp.Database.ThoughtsDatabase
import com.example.quoteapp.Database.ThoughtsRepository
import com.example.quoteapp.Interface.RvClickInterface
import com.example.quoteapp.ModelClass.ModelCLassSearchResponse
import com.example.quoteapp.ModelClass.ModelClassSearchPayload
import com.example.quoteapp.R
import com.example.quoteapp.Utils.Constants
import com.example.quoteapp.Utils.ProgressUtil
import com.example.quoteapp.Utils.RetrofitHelper
import com.example.quoteapp.Utils.Utililty
import com.example.quoteapp.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Response
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


class SearchFragment : Fragment(),RvClickInterface {
    private lateinit var progressUtil: ProgressUtil
    private lateinit var thoughtsDatabase: ThoughtsDatabase
    private lateinit var thoughtsRepository: ThoughtsRepository
    lateinit var searchString: String
    lateinit var binding:FragmentSearchBinding
    var isFirstSearch=true
    var allList=ArrayList<Triple<String,String,String>>()
    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false
    private var lastPage=0
    private var allResponse= arrayListOf<ModelClassSearchPayload>()
    lateinit var adapter : AdapterRvSearch
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_search, container, false)
        progressUtil= ProgressUtil(requireActivity())
        thoughtsDatabase = Room.databaseBuilder(
            requireContext(),
            ThoughtsDatabase::class.java,
            "thoughts_database").allowMainThreadQueries().build()

        thoughtsRepository = ThoughtsRepository(thoughtsDatabase.thoughtsDao())

        binding.search.setOnClickListener {
            searchString=binding.searchtext.text.toString()
            isFirstSearch=true
            currentPage=1

            performSearch()

        }

        binding.searchtext.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else {
                false
            }
        }
        return binding.root
    }

    // checking the search string is empty or blank
    private fun performSearch() {
        progressUtil.showProgress()
        if (searchString.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Please enter a search query",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        isLoading=true
        isLastPage=true
        currentPage=1
        isFirstSearch=true
        lastPage=0
        allList.clear()
        allResponse.clear()
        apiCall()

    }


    // api call

    private fun apiCall() {

        isLoading=true
        val search=RetrofitHelper().retrofitobj()

        search.Search(Constants.token,searchString ,currentPage).enqueue(object :retrofit2.Callback<ModelCLassSearchResponse>{
            override fun onResponse(
                call: Call<ModelCLassSearchResponse>,
                response: Response<ModelCLassSearchResponse>
            ) {
                    setAdapter()
                if (response.isSuccessful && response.body()?.payload?.data?.isNotEmpty() == true) {
                    progressUtil.hideProgress()
                    adapter.setLoading(false)
                    binding.quoteRv.adapter?.let { binding.quoteRv.layoutManager?.scrollToPosition(it.itemCount-3) }
                    isLoading=false

                    lastPage = response.body()!!.payload.meta.last_page
                    isLastPage =
                        response.body()!!.payload.meta.to == response.body()!!.payload.meta.total


                    binding.quoteRv.visibility = View.VISIBLE
                    binding.noData.visibility = View.GONE
                    binding.nodataTxt.visibility = View.GONE
                    if (response.body()?.payload?.data!!.isNotEmpty()) {
                        allList.addAll(
                            searchInArrayList(
                                response.body()?.payload?.data!!,
                                searchString
                            )
                        )
                        allResponse.addAll(response.body()?.payload?.data!!)
                        binding.recentSearch.text="Search Results (${allList.size})"
                    } else {

                        isLastPage = true

                    }

                }
                else {
                    adapter.setLoading(false)
                    progressUtil.hideProgress()

                    binding.recentSearch.text=resources.getString(R.string.recent_search)
                    binding.quoteRv.visibility=View.GONE
                    binding.noData.visibility=View.VISIBLE
                    binding.nodataTxt.visibility=View.VISIBLE
                    isLoading=false
                }
            }

            override fun onFailure(call: Call<ModelCLassSearchResponse>, t: Throwable) {
                progressUtil.hideProgress()
                adapter.setLoading(false)
                binding.recentSearch.text=resources.getString(R.string.recent_search)
                isLoading=false
                binding.quoteRv.visibility=View.GONE
                binding.noData.visibility=View.VISIBLE
                binding.nodataTxt.visibility=View.VISIBLE
                Toast.makeText(
                    requireContext(),
                    getString(R.string.some_error_occur),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
fun makeTheCall(){
    if (isFirstSearch){
        apiCall()
    }
    else{
        if (currentPage<=lastPage){
            apiCall()
        }
    }
}
    // setting the adapter

    private fun setAdapter() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.quoteRv.layoutManager = layoutManager

        if (isFirstSearch) {
            val personalTemp = searchINPersonalThought()
            for (i in personalTemp) {
                allList.add(i)
            }
            binding.quoteRv.scrollToPosition(-2)
            isFirstSearch = false
        }

        adapter = AdapterRvSearch(allList, this)
        binding.quoteRv.adapter = adapter

        binding.quoteRv.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                binding.quoteRv.scrollToPosition(lastVisibleItem)

                // Unregister the listener to avoid multiple calls
                binding.quoteRv.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        binding.quoteRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val isLastItemVisible = visibleItemCount + firstVisibleItemPosition >= totalItemCount
                val isNotLoadingAndNotLastPage = !isLoading && !isLastPage

                if (isLastItemVisible && isNotLoadingAndNotLastPage) {
                    if (isFirstSearch || currentPage <= lastPage) {
                        currentPage++
                        adapter.setLoading(true)
                        apiCall()
                    }
                }
            }
        })

        adapter.notifyDataSetChanged()
    }


    // searching in the response
    private fun searchInArrayList(
        data: ArrayList<ModelClassSearchPayload>,
        searchString: String
    ): ArrayList<Triple<String, String, String>> {
        val searchResults = ArrayList<Triple<String, String, String>>()
        searchResults.clear()

        val uniqueFields = HashSet<Pair<String, String>>()

        for (item in data) {
            val itemFields = item.javaClass.declaredFields
            for (field in itemFields) {
                field.isAccessible = true
                val fieldName = field.name
                val fieldValue = field.get(item)?.toString()
                if (fieldName != "name" && (fieldValue != null) && fieldValue.contains(searchString, ignoreCase = true)) {
                    val fieldPair = Pair(fieldName, fieldValue)

                    if (!uniqueFields.contains(fieldPair)) {
                        uniqueFields.add(fieldPair)
                        val date = item.date ?: "" // Add null check for item.date
                        if (fieldName == "fact") {
                            searchResults.add(Triple(fieldName, fieldValue, formatDate(LocalDate.now().toString())))
                        } else {
                            searchResults.add(Triple(fieldName, fieldValue, date))
                        }
                    }
                }
            }
        }

        return searchResults
    }



    private fun searchINPersonalThought(): ArrayList<Triple<String, String, String>> {
        val evening = thoughtsRepository.searchByWordType(searchString,"evening")
        val morning = thoughtsRepository.searchByWordType(searchString,"morning")
        val list = arrayListOf<Triple<String, String, String>>()

        for (i in evening) {
            if (i.thought.isNotBlank() && i.thought.contains(searchString, ignoreCase = true)) {
                list.add(Triple(getString(R.string.tittle_morning), i.thought, i.date))
            }
        }
        for (i in morning) {
            if (i.thought.isNotBlank() && i.thought.contains(searchString, ignoreCase = true)) {
                list.add(Triple(getString(R.string.tittle_morning), i.thought, i.date))
            }
        }
        Log.e("personal thought",list.toString())
        return list
    }

    override fun onItemClick(date: String, title: String, fact: String) {
        Log.e("date", date)
        val intent = Intent(requireContext(), DetailScreenActivity::class.java)

        when (title) {
            getString(R.string.tittle_morning), getString(R.string.tittle_morning) -> {
                intent.putExtra("date", date)
            }
            "Fact" -> {
                intent.putExtra("fact", fact)
            }
            else -> {
                val data = searchInAllResponse(date.first().toString())
                intent.putExtra("data", data)
            }
        }

        startActivity(intent)
    }

    private fun searchInAllResponse(searchString: String?): ModelClassSearchPayload? {
        var searchResults: ModelClassSearchPayload? = null

        if (!searchString.isNullOrEmpty()) {
            for (item in allResponse) {
                val date = item.date ?: "" // Add null check for item.date
                if (date.contains(searchString, ignoreCase = true)) {
                    searchResults = item
                    break
                }
            }
        }

        return searchResults
    }

/*
private fun searchInAllResponse(searchString: String): ModelClassSearchPayload? {
        var searchResults: ModelClassSearchPayload? = null

        for (item in allResponse) {

            if (item.date.contains(searchString, ignoreCase = true)) {
                searchResults=item
                break
            }
        }
        return searchResults
    }
 */
    override fun onResume() {
        super.onResume()
        Utililty().updateTextSizeRecursive(binding.root)
    }


    fun formatDate(inputDate: String): String {
        val inputPattern = "yyyy-MM-dd"
        val outputPattern = "dd MMM"

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


    override fun onPause() {
        super.onPause()
        progressUtil.hideProgress()
    }

}