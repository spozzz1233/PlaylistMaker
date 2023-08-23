package com.example.playlistmaker.ui.search.activity

import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.ui.search.adapters.HistoryAdapter
import com.example.playlistmaker.ui.search.adapters.searchAdapter
import com.example.playlistmaker.util.MusicHistory
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.search.model.historyTracks
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchActivity : AppCompatActivity(){

    private lateinit var progressBar: ProgressBar
    private var inputText: String? = null
    private lateinit var editText: EditText
    private lateinit var back: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var noResultPlaceholderMessage: FrameLayout
    private lateinit var UpdateButton: Button
    private val viewModel by viewModel<SearchViewModel>()
    lateinit var binding: ActivitySearchBinding
    lateinit var searchAdapter: searchAdapter
    lateinit var historyAdapter: HistoryAdapter
    lateinit var recyclerViewSearch: RecyclerView
    lateinit var recyclerViewHistory: RecyclerView
    lateinit var removeHistory: Button
    lateinit var history: LinearLayout
    lateinit var query: String
    private lateinit var noInternetPlaceholderMessage: View

    val musicHistory = MusicHistory(this)



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
        progressBar = findViewById(R.id.progressBar)
        noInternetPlaceholderMessage = findViewById(R.id.no_internet)



        viewModel.searchResultsLiveData.observe(this, { searchResults ->
            searchAdapter.updateData()
            recyclerViewSearch.visibility = if (searchResults) View.VISIBLE else View.GONE

        })

        viewModel.loadingLiveData.observe(this, { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.noResultLiveData.observe(this, { noResult ->
            noResultPlaceholderMessage.visibility = if (noResult) View.VISIBLE else View.GONE
        })

        viewModel.noInternetLiveData.observe(this, { noInternet ->
            noInternetPlaceholderMessage.visibility = if (noInternet) View.VISIBLE else View.GONE
        })

        initial()
        history()
        editText.requestFocus()

        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)


        val searchRunnable = Runnable { viewModel.searchTrack(query) }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }
        editText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (editText.text.isNotEmpty()) {
                    viewModel.searchTrack(query)
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
            clearEditText()
        }
        UpdateButton.setOnClickListener {
            viewModel.searchTrack(query)
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
                query = editText.text.toString()
                searchDebounce()
                if(query.isEmpty()){
                    clearEditText()
                }
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
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }


    private fun initial() {
        recyclerViewSearch = binding.recyclerViewSearch
        searchAdapter = searchAdapter(this)
        recyclerViewSearch.adapter = searchAdapter
        recyclerViewHistory = binding.recyclerViewHistory
        historyAdapter = HistoryAdapter(this )
        recyclerViewHistory.adapter = historyAdapter
    }

    private fun history() {
        if (historyTracks.isNotEmpty()) {
            recyclerViewHistory.visibility = View.VISIBLE
            removeHistory.visibility = View.VISIBLE
        } else {
            recyclerViewHistory.visibility = View.GONE
            removeHistory.visibility = View.GONE
        }
    }
    private fun clearEditText(){
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(editText.windowToken, 0)
        editText.clearFocus()
        progressBar.visibility = View.GONE
        recyclerViewSearch.visibility = View.GONE
        history.visibility = View.VISIBLE
        recyclerViewHistory.visibility = View.VISIBLE
        removeHistory.visibility = View.VISIBLE
        noInternetPlaceholderMessage.visibility = View.GONE
        noResultPlaceholderMessage.visibility = View.GONE
    }


}
