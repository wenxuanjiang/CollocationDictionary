package com.outlook.mini_frog.essayhelper

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CollectFragment : Fragment() {

    private lateinit var collectionListAdapter: CollectionListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.collection_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val collectionRecyclerView: RecyclerView = view.findViewById(R.id.collection_list)
        collectionListAdapter = CollectionListAdapter()
        collectionRecyclerView.adapter = collectionListAdapter
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.collection_actionbar_menu, menu)
        val selection = menu!!.findItem(R.id.selection)
        selection.setOnMenuItemClickListener { menuItem ->
            if (collectionListAdapter.isSelectionsOpen()) {
                collectionListAdapter.closeSelections()
            } else {
                collectionListAdapter.openSelections()
            }
            true
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    inner class CollectionListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private val list = ArrayList<Collection>()
        private val sortedList = TreeSet<Collection>()

        private var selectionsOpen = false
        private val selectedList = ArrayList<Boolean>()

        init {
            sortedList.addAll(CollectionManager.collections)
            for (i in 0 until sortedList.size) {
                selectedList.add(false)
            }
            list.addAll(sortedList)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
            p0 as CollectionView
            p0.wordTextView.text = list[p1].word.toString()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)
            p0.dateTextView.text = dateFormat.format(list[p1].date)
            if (selectedList[p1]) {
                p0.itemView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            } else {
                p0.itemView.setBackgroundColor(Color.rgb(250, 250, 250))
            }
            p0.itemView.setOnClickListener {
                if (selectionsOpen) {
                    if (selectedList[p1]) {
                        p0.itemView.setBackgroundColor(Color.rgb(250, 250, 250))
                    } else {
                        p0.itemView.setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
                    }
                    selectedList[p1] = selectedList[p1].not()
                }
            }
            p0.itemView.setOnClickListener {
                val word = Word(p0.wordTextView.text.toString())
                val description = WordManager.getDescription(context!!, word)
                val intent = Intent(context, WordDetailActivity::class.java)
                intent.putExtra("detail", description.toString())
                context!!.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(p0: ViewGroup, p1: Int): RecyclerView.ViewHolder {
            val view = layoutInflater.inflate(R.layout.collection, p0, false)
            return CollectionView(view)
        }

        inner class CollectionView(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val wordTextView: TextView = itemView.findViewById(R.id.word)
            val dateTextView: TextView = itemView.findViewById(R.id.date)
        }

        fun openSelections() {
            selectionsOpen = true
        }

        fun closeSelections() {
            selectionsOpen = false
            for (i in 0 until selectedList.size) {
                selectedList[i] = false
            }
            notifyDataSetChanged()
        }

        fun isSelectionsOpen(): Boolean {
            return selectionsOpen
        }
    }
}