package com.example.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {
    var inputText:String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val back = findViewById<ImageView>(R.id.back)
        val clearButton = findViewById<ImageView>(R.id.clear)
        var inputEditText = findViewById<EditText>(R.id.SearchForm)
        back.setOnClickListener {
            finish()
        }
        clearButton.setOnClickListener {
            inputEditText.setText("")
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
        inputEditText.addTextChangedListener(simpleTextWatcher)
        inputEditText.setText(inputText)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var inputEditText = findViewById<EditText>(R.id.SearchForm)
        outState.putString(TEXT_SEARCH, inputEditText.text.toString())
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


}