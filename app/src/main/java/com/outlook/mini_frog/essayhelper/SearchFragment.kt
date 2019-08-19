package com.outlook.mini_frog.essayhelper

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList
import android.view.MenuInflater
import android.widget.SearchView


class SearchFragment : Fragment() {

    private lateinit var wordRecyclerView: RecyclerView
    private lateinit var wordListAdapter: WordListAdapter
    private lateinit var searchTextView: SearchView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.search_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wordRecyclerView = view.findViewById(R.id.word_list)
        wordListAdapter = WordListAdapter(context!!)
        wordRecyclerView.adapter = wordListAdapter
        wordRecyclerView.layoutManager = LinearLayoutManager(context)
        wordRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    inner class MyOnQueryTextListener : SearchView.OnQueryTextListener {

        override fun onQueryTextChange(p0: String?): Boolean {
            wordListAdapter.update(p0.toString())
            return true
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            return true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.search_actionbar_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        searchTextView = searchItem?.actionView as SearchView
        searchTextView.setOnQueryTextListener(MyOnQueryTextListener())
        super.onCreateOptionsMenu(menu, inflater)
    }

    inner class WordListAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val sortedList = TreeSet<Word>()
        private val list = ArrayList<Word>()
        private var source = ""

        init {
            sortedList.addAll(WordManager.words)
            update("")
        }

        override fun getItemCount(): Int {
            return list.size
        }

        fun clear() {
            list.clear()
            notifyDataSetChanged()
        }

        fun update(source: String) {
            list.clear()
            this.source = source
            if (source.isEmpty()) {
                list.addAll(sortedList)
            } else {
                val wordMin = Word(source)
                val wordMax = wordMin.nextGroup()
                list.addAll(sortedList.subSet(wordMin, wordMax).asIterable())
            }
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, index: Int) {
            viewHolder as WordView
            viewHolder.wordTextView.text = list[index].toString()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.word, parent, false)
            view.setOnClickListener(WordViewOnClickListener(context))
            return WordView(view)
        }

        inner class WordView(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val wordTextView: TextView = itemView.findViewById(R.id.word)
        }

        override fun getItemViewType(position: Int): Int {
            return 0
        }

        inner class WordViewOnClickListener(private val context: Context) : View.OnClickListener {

            override fun onClick(p0: View?) {
                val wordTextView: TextView = p0!!.findViewById(R.id.word)
                val word = Word(wordTextView.text.toString())
                val description = WordManager.getDescription(context, word)
                val intent = Intent(context, WordDetailActivity::class.java)
                intent.putExtra("detail", description.toString())
                context.startActivity(intent)
            }
        }
    }
}