package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private var inputText:String? = null
    private lateinit var editText: EditText
    private lateinit var back:ImageView
    private lateinit var clearButton:ImageView
    lateinit var binding: ActivitySearchBinding
    lateinit var adapter: searchAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        back = findViewById(R.id.back)
        clearButton = findViewById(R.id.clear)
        editText = findViewById(R.id.SearchForm)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initial()
        back.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            editText.setText("")
        }
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

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
        recyclerView = binding.recyclerViewSearch
        adapter = searchAdapter()
        recyclerView.adapter = adapter

    }

}