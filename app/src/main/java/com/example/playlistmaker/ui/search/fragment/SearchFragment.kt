package com.example.playlistmaker.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.model.historyTracks
import com.example.playlistmaker.ui.player.activity.PlayerActivity
import com.example.playlistmaker.ui.search.adapters.HistoryAdapter
import com.example.playlistmaker.ui.search.adapters.searchAdapter
import com.example.playlistmaker.ui.search.view_model.SearchFragmentViewModel
import com.example.playlistmaker.util.MusicHistory
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {
    private var inputText: String? = null
    private val viewModel by viewModel<SearchFragmentViewModel>()
    lateinit var binding: FragmentSearchBinding
    private lateinit var searchAdapter: searchAdapter
    lateinit var historyAdapter: HistoryAdapter
    lateinit var query: String
    private lateinit var musicHistory: MusicHistory

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
            binding.noInternet.visibility = if (noInternet) View.VISIBLE else View.GONE
        })
        viewModel.searchResultsListLiveData.observe(viewLifecycleOwner, Observer{ tracks ->
            searchAdapter.newTracks(tracks)
        })
        initial()
        history()
        binding.SearchForm.requestFocus()

        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(binding.SearchForm, InputMethodManager.SHOW_IMPLICIT)



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
            viewModel.clearSearch()
        }
        binding.updateButton.setOnClickListener {
            viewModel.searchTrack(query)
        }
        binding.buttonHistory.setOnClickListener {
            musicHistory.clearSharedPreferences(requireContext())
            binding.recyclerViewHistory.visibility = View.GONE
            binding.buttonHistory.visibility = View.GONE
            binding.historyText.visibility = View.GONE
            historyAdapter.notifyDataSetChanged()
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clear.visibility = clearButtonVisibility(s)
                binding.history.visibility = View.GONE
                query = binding.SearchForm.text.toString()
                viewModel.searchDebounce(query)
                if (query.isEmpty()) {
                    clearEditText()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        binding.SearchForm.addTextChangedListener(simpleTextWatcher)


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

    companion object {
        const val TEXT_SEARCH = "TEXT_SEARCH"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }


    private fun initial() {
        val recyclerViewSearch = binding.recyclerViewSearch
        searchAdapter = searchAdapter(requireContext()){ track->
            if (clickDebounce()) {
                findNavController().navigate(R.id.action_searchFragment_to_playerActivity,
                    PlayerActivity.createArgs(
                        track.trackName,
                        track.previewUrl,
                        track.artistName,
                        track.trackTimeMillis,
                        track.artworkUrl100,
                        track.collectionName,
                        track.releaseDate,
                        track.primaryGenreName,
                        track.country
                    )
                )

            }
        }
        recyclerViewSearch.adapter = searchAdapter
        val recyclerViewHistory = binding.recyclerViewHistory
        historyAdapter = HistoryAdapter(requireContext()){track ->
            if (clickDebounce()) {
                findNavController().navigate(R.id.action_searchFragment_to_playerActivity,
                    PlayerActivity.createArgs(
                        track.trackName,
                        track.previewUrl,
                        track.artistName,
                        track.trackTimeMillis,
                        track.artworkUrl100,
                        track.collectionName,
                        track.releaseDate,
                        track.primaryGenreName,
                        track.country
                    ))
            }
        }
        binding.recyclerViewHistory.adapter = historyAdapter
    }

    private fun history() {
        if (historyTracks.isNotEmpty()) {
            binding.recyclerViewHistory.visibility = View.VISIBLE
            binding.buttonHistory.visibility = View.VISIBLE
            binding.historyText.visibility = View.VISIBLE
        } else {
            binding.recyclerViewHistory.visibility = View.GONE
            binding.historyText.visibility = View.GONE
            binding.buttonHistory.visibility = View.GONE
        }
    }

    private fun clearEditText() {
        val keyboard =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        keyboard.hideSoftInputFromWindow(binding.SearchForm.windowToken, 0)
        binding.SearchForm.clearFocus()
        binding.progressBar.visibility = View.GONE
        binding.recyclerViewSearch.visibility = View.GONE
        binding.noInternet.visibility = View.GONE
        binding.noResult.visibility = View.GONE
        if(historyTracks.isNotEmpty()){
            binding.history.visibility = View.VISIBLE
            binding.recyclerViewHistory.visibility = View.VISIBLE
            binding.buttonHistory.visibility = View.VISIBLE
        }
    }
    private var isClickAllowed = true

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}


