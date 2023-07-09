package com.example.playlistmaker.Activity

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.Adapter.HistoryAdapter
import com.example.playlistmaker.Adapter.searchAdapter
import com.example.playlistmaker.MusicHistory
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.historyTracks
import com.example.playlistmaker.tracks
import com.example.playlistmaker.tracksResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var progressBar:ProgressBar
    private var inputText:String? = null
    private lateinit var editText: EditText
    private lateinit var back:ImageView
    private lateinit var clearButton:ImageView
    private lateinit var noResultPlaceholderMessage:FrameLayout
    private lateinit var UpdateButton:Button
    lateinit var binding: ActivitySearchBinding
    lateinit var searchAdapter: searchAdapter
    lateinit var historyAdapter: HistoryAdapter
    lateinit var recyclerViewSearch: RecyclerView
    lateinit var recyclerViewHistory: RecyclerView
    lateinit var removeHistory:Button
    lateinit var history:LinearLayout
    val musicHistory = MusicHistory(this)
    val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val searchApi = retrofit.create(com.example.playlistmaker.searchApi::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val handler = Handler(Looper.getMainLooper())
        back = findViewById(R.id.back)
        clearButton = findViewById(R.id.clear)
        editText = findViewById(R.id.SearchForm)
        UpdateButton = findViewById(R.id.update_button)
        noResultPlaceholderMessage = findViewById(R.id.no_result)
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        removeHistory = findViewById(R.id.button_history)
        history = findViewById(R.id.history)
        progressBar =findViewById(R.id.progressBar)

        initial()
        historyVisible()

        editText.requestFocus()
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)


        val searchRunnable = Runnable { searchTrack() }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()) {
                    searchTrack()
                }
                true
            }
            false
        }

        back.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            editText.setText("")
        }
        UpdateButton.setOnClickListener {
            searchTrack()
        }
        removeHistory.setOnClickListener {
            musicHistory.clearSharedPreferences(this)
            recyclerViewHistory.visibility = View.GONE
            removeHistory.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                history.visibility = View.GONE
                searchDebounce()
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        editText.addTextChangedListener(simpleTextWatcher)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEXT_SEARCH, editText.text.toString())
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputText = savedInstanceState.getString(TEXT_SEARCH)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private fun initial() {
        recyclerViewSearch = binding.recyclerViewSearch
        searchAdapter = searchAdapter(this)
        recyclerViewSearch.adapter = searchAdapter
        recyclerViewHistory = binding.recyclerViewHistory
        historyAdapter = HistoryAdapter()
        recyclerViewHistory.adapter = historyAdapter
    }

    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    private fun searchTrack() {
        if (editText.text.isNotEmpty()) {
            progressBar.visibility = View.VISIBLE
            recyclerViewSearch.visibility = View.GONE
            searchApi.search(editText.text.toString()).enqueue(object :
                Callback<tracksResponse> {
                override fun onResponse(
                    call: Call<tracksResponse>,
                    response: Response<tracksResponse>
                ) {
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        tracks.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            recyclerViewSearch.visibility = View.VISIBLE
                            tracks.addAll(response.body()?.results!!)
                            searchAdapter.notifyDataSetChanged()
                        }
                        if (tracks.isEmpty()) {
                            noResultPlaceholderMessage.visibility = View.VISIBLE
                            history.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<tracksResponse>, t: Throwable) {
                    if (!checkInternetConnection()) {
                        val noInternetPlaceholderMessage = findViewById<View>(R.id.no_internet)
                        noInternetPlaceholderMessage.visibility = View.VISIBLE
                        history.visibility = View.GONE
                        progressBar.visibility = View.GONE
                    }
                }
            })
        }
    }
    private fun historyVisible(){
        if(historyTracks.isNotEmpty()){
            recyclerViewHistory.visibility = View.VISIBLE
            removeHistory.visibility = View.VISIBLE
        }
        else{
            recyclerViewHistory.visibility = View.GONE
            removeHistory.visibility = View.GONE
        }
    }

}
