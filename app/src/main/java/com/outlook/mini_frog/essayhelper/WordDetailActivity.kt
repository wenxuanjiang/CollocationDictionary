package com.outlook.mini_frog.essayhelper

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList


class WordDetailActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var contentBox: LinearLayout
    private lateinit var detail: JSONObject
    private lateinit var word: Word
    private var collected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_detail)
        detail = JSONObject(intent.extras!!["detail"] as String)
        word = Word(detail["word"] as String)
        collected = CollectionManager.contain(word)
        initView()
        render()
    }

    private fun initView() {
        contentBox = findViewById(R.id.content)
    }

    private fun render() {
        intent.extras ?: return
        contentBox.addView(renderTitle(detail["word"] as String))

        // meanings
        val meanings = detail["meanings"] as JSONArray
        for (i in 0 until meanings.length()) {
            val meaning = meanings[i] as JSONObject
            contentBox.addView(renderMeaning(meaning["meaning"] as String))

            // collocations
            val collocations = meaning["collocation"] as JSONArray
            for (j in 0 until collocations.length()) {
                val collocation = collocations[j] as JSONObject
                contentBox.addView(renderFormat(collocation["format"] as String))

                // groups
                val groups = collocation["groups"] as JSONArray
                for (k in 0 until groups.length()) {
                    for (textView in renderGroup(groups[k] as JSONObject)) {
                        contentBox.addView(textView)
                    }
                }
            }
        }
    }

    private fun renderTitle(title: String): TextView {
        // set text
        val spannable = SpannableString(title)


        val spanSize = AbsoluteSizeSpan(60)
        spannable.setSpan(spanSize, 0, title.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // set view
        titleTextView = TextView(this)
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(50, 40, 50, 0)
        titleTextView.setOnClickListener(TitleOnClickListener())
        titleTextView.layoutParams = layoutParams
        titleTextView.text = spannable
        if (CollectionManager.contain(title)) {
            titleTextView.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary))
        } else {
            titleTextView.setTextColor(Color.rgb(71, 71, 71))
        }
        return titleTextView
    }

    private fun renderMeaning(meaning: String): TextView {
        // set text
        val spannable = SpannableString(meaning)
        val spanColor = ForegroundColorSpan(Color.rgb(71, 71, 71))
        val spanSize = AbsoluteSizeSpan(50)
        spannable.setSpan(spanColor, 0, meaning.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(spanSize, 0, meaning.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // set view
        val textView = TextView(this)
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(50, 40, 50, 0)
        textView.layoutParams = layoutParams
        textView.text = spannable
        return textView
    }

    private fun renderFormat(format: String): TextView {
        // set text
        val spannable = SpannableString(format)
        val spanColor = ForegroundColorSpan(Color.rgb(0, 111, 191))
        val spanSize = AbsoluteSizeSpan(40)
        spannable.setSpan(spanColor, 0, format.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannable.setSpan(spanSize, 0, format.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // set view
        val textView = TextView(this)
        val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(50, 25, 50, 0)
        textView.layoutParams = layoutParams
        textView.text = spannable
        return textView
    }

    private fun renderGroup(group: JSONObject): ArrayList<TextView> {
        val textViewList = ArrayList<TextView>()
        val words = group["words"] as JSONArray
        val examples = group["examples"] as JSONArray

        // build words string
        val wordsStringBuilder = StringBuilder()
        for (i in 0 until words.length()) {
            wordsStringBuilder.append((words[i] as String) + ", ")
        }
        val wordsString = wordsStringBuilder.substring(0, wordsStringBuilder.length - 2)

        // set words string
        val wordsStringSpannable = SpannableString(wordsString)
        val wordsStringSpanColor = ForegroundColorSpan(Color.rgb(71, 71, 71))
        val wordsStringSpanSize = AbsoluteSizeSpan(30)
        val wordsStringSpanStyle = StyleSpan(Typeface.BOLD)
        wordsStringSpannable.setSpan(wordsStringSpanColor, 0, wordsString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordsStringSpannable.setSpan(wordsStringSpanSize, 0, wordsString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        wordsStringSpannable.setSpan(wordsStringSpanStyle, 0, wordsString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        // set words text view
        val wordsStringTextView = TextView(this)
        val wordsStringLayoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        wordsStringLayoutParams.setMargins(50, 15, 50, 0)
        wordsStringTextView.layoutParams = wordsStringLayoutParams
        wordsStringTextView.text = wordsStringSpannable
        textViewList.add(wordsStringTextView)

        // examples
        for (i in 0 until examples.length()) {
            // set example string
            val exampleSpannable = SpannableString(examples[i] as String)
            val exampleSpannableSpanColor = ForegroundColorSpan(Color.rgb(185, 181, 181))
            val exampleSpannableSpanSize = AbsoluteSizeSpan(30)
            val exampleSpannedStyle = StyleSpan(Typeface.ITALIC)
            exampleSpannable.setSpan(exampleSpannableSpanColor, 0, exampleSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            exampleSpannable.setSpan(exampleSpannableSpanSize, 0, exampleSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            exampleSpannable.setSpan(exampleSpannedStyle, 0, exampleSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            // set example view
            val exampleTextView = TextView(this)
            val exampleLayoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            )
            exampleLayoutParams.setMargins(50, 5, 50, 0)
            exampleTextView.layoutParams = exampleLayoutParams
            exampleTextView.text = exampleSpannable
            textViewList.add(exampleTextView)
        }

        return textViewList
    }

    inner class TitleOnClickListener: View.OnClickListener {

        override fun onClick(p0: View?) {
            if (collected) {
                CollectionManager.delete(word)
                Toast.makeText(this@WordDetailActivity, "取消收藏", Toast.LENGTH_SHORT).show()
                titleTextView.setTextColor(Color.rgb(71, 71, 71))
            } else {
                CollectionManager.insert(Collection(word, Date(), CollectionManager.usedComparator))
                Toast.makeText(this@WordDetailActivity, "收藏单词", Toast.LENGTH_SHORT).show()
                titleTextView.setTextColor(ContextCompat.getColor(this@WordDetailActivity, R.color.colorPrimary))
            }
            collected = collected.not()
        }
    }
}