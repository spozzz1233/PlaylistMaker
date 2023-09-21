package com.example.playlistmaker.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.historyTracks
import com.example.playlistmaker.ui.search.adapters.HistoryAdapter
import com.example.playlistmaker.ui.search.adapters.searchAdapter
import com.example.playlistmaker.ui.search.view_model.SearchFragmentViewModel
import com.example.playlistmaker.util.MusicHistory
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {


    private var inputText: String? = null
    private val viewModel by viewModel<SearchFragmentViewModel>()
    lateinit var binding: FragmentSearchBinding
    lateinit var historyAdapter: HistoryAdapter
    lateinit var searchAdapter: searchAdapter
    lateinit var recyclerViewSearch: RecyclerView
    lateinit var recyclerViewHistory: RecyclerView
    private lateinit var musicHistory: MusicHistory
    lateinit var query: String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            inputText = savedInstanceState.getString(TEXT_SEARCH)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        musicHistory = MusicHistory(requireContext())
        binding = FragmentSearchBinding.inflate(layoutInflater)

        val handler = Handler(Looper.getMainLooper())

        viewModel.searchResultsLiveData.observe(viewLifecycleOwner, { searchResults ->
            searchAdapter.updateData()
            binding.recyclerViewSearch.visibility = if (searchResults) View.VISIBLE else View.GONE

        })

        viewModel.loadingLiveData.observe(viewLifecycleOwner, { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        viewModel.noResultLiveData.observe(viewLifecycleOwner, { noResult ->
            binding.noResult.visibility = if (noResult) View.VISIBLE else View.GONE
        })

        viewModel.noInternetLiveData.observe(viewLifecycleOwner, { noInternet ->
            binding.noInetPlaceholder.visibility = if (noInternet) View.VISIBLE else View.GONE
        })

        initial()
        history()
        binding.SearchForm.requestFocus()

        val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.SearchForm, InputMethodManager.SHOW_IMPLICIT)



        val searchRunnable = Runnable { viewModel.searchTrack(query) }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clear.visibility = clearButtonVisibility(s)
                binding.history.visibility = View.GONE
                query = binding.SearchForm.text.toString()
                Log.d("SearchFragment", "Query: $query")
                searchDebounce()
                if(query.isEmpty()){
                    clearEditText()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.SearchForm.addTextChangedListener(simpleTextWatcher)
        binding.SearchForm.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.SearchForm.text.isNotEmpty()) {
                    viewModel.searchTrack(query)
                }
                true
            }
            false
        }


        binding.clear.setOnClickListener {
            binding.SearchForm.setText("")
            clearEditText()
        }
        binding.updateButton.setOnClickListener {
            viewModel.searchTrack(query)
        }
        binding.buttonHistory.setOnClickListener {
            musicHistory.clearSharedPreferences(requireContext())
            binding.recyclerViewHistory.visibility = View.GONE
            binding.buttonHistory.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(TEXT_SEARCH, binding.SearchForm.text.toString())
        super.onSaveInstanceState(outState)
    }


    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }



    private fun initial() {
        recyclerViewSearch = binding.recyclerViewSearch
        searchAdapter = searchAdapter(requireContext())
        binding.recyclerViewSearch.adapter = searchAdapter
        recyclerViewHistory = binding.recyclerViewHistory
        historyAdapter = HistoryAdapter(requireContext())
        binding.recyclerViewHistory.adapter = historyAdapter
    }

    private fun history() {
        if (historyTracks.isNotEmpty()) {
            binding.recyclerViewHistory.visibility = View.VISIBLE
            binding.buttonHistory.visibility = View.VISIBLE
        } else {
            binding.recyclerViewHistory.visibility = View.GONE
            binding.buttonHistory.visibility = View.GONE
        }
    }
    private fun clearEditText(){
        val keyboard = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(binding.SearchForm.windowToken, 0)
        binding.SearchForm.clearFocus()
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewSearch.visibility = View.GONE
        binding.history.visibility = View.VISIBLE
        binding.recyclerViewHistory.visibility = View.VISIBLE
        binding.buttonHistory.visibility = View.VISIBLE
        binding.noInternet.visibility = View.GONE
        binding.noResult.visibility = View.GONE
    }
}