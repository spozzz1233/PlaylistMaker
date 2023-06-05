package com.example.playlistmaker

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response
import okhttp3.OkHttpClient
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {
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
    val searchApi = retrofit.create(searchApi::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        back = findViewById(R.id.back)
        clearButton = findViewById(R.id.clear)
        editText = findViewById(R.id.SearchForm)
        UpdateButton = findViewById(R.id.update_button)
        noResultPlaceholderMessage = findViewById(R.id.no_result)
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch)
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory)
        removeHistory =findViewById(R.id.button_history)
        history = findViewById(R.id.history)
        initial()
        musicHistory.getHistoryTracks(this)

        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()){
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
        removeHistory.setOnClickListener{
            historyTracks.clear()
            musicHistory.clearSharedPreferences(this)
            historyAdapter.notifyDataSetChanged()
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                history.visibility=View.GONE

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
    }
    private fun initial(){
        recyclerViewSearch = binding.recyclerViewSearch
        searchAdapter = searchAdapter(this)
        recyclerViewSearch.adapter = searchAdapter
        recyclerViewHistory =binding.recyclerViewHistory
        historyAdapter = HistoryAdapter()
        recyclerViewHistory.adapter = historyAdapter
    }
    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
    private fun searchTrack(){
        searchApi.search(editText.text.toString()).enqueue(object :
            Callback<tracksResponse>{
            override fun onResponse(call: Call<tracksResponse>,response: Response<tracksResponse>) {
                if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        recyclerViewSearch.visibility = View.VISIBLE
                        tracks.addAll(response.body()?.results!!)
                        searchAdapter.notifyDataSetChanged()

                    }
                    if (tracks.isEmpty()) {
                        noResultPlaceholderMessage.visibility=View.VISIBLE
                        history.visibility=View.GONE

                    }

                }

            }
            override fun onFailure(call: Call<tracksResponse>, t: Throwable) {
                if (!checkInternetConnection()) {
                    val noInternetPlaceholderMessage = findViewById<View>(R.id.no_internet)
                    noInternetPlaceholderMessage.visibility = View.VISIBLE
                    history.visibility=View.GONE
                }


            }
        })
    }
}