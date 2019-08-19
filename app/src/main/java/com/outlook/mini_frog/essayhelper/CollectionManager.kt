package com.outlook.mini_frog.essayhelper

import android.content.Context
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.*

object CollectionManager {

    val collections = ArrayList<Collection>()
    val wordComparator = Comparator<Collection> { collection1, collection2 ->
        collection1.word.compareTo(collection2.word)
    }
    val dateComparator = Comparator<Collection> { collection1, collection2 ->
        (collection1.date.time - collection2.date.time).toInt()
    }
    var usedComparator = wordComparator

    fun init(context: Context) {
        if (! collectionFileExist(context)) {
            syncCollectionRamToDisk(context)
        }
        syncCollectionDiskToRam(context)
    }

    fun insert(collection: Collection) {
        collections.add(collection)
    }

    fun delete(collection: Collection) {
        collections.remove(collection)
    }

    fun delete(word: Word) {
        for (collection in collections) {
            if (collection.word.toString() == word.toString()) {
                collections.remove(collection)
            }
        }
    }

    fun contain(word: Word): Boolean {
        for (collection in collections) {
            if (word.toString() == collection.word.toString()) {
                return true
            }
        }
        return false
    }

    fun contain(word: String): Boolean {
        for (collection in collections) {
            if (word == collection.word.toString()) {
                return true
            }
        }
        return false
    }

    fun render(context: Context) {
        syncCollectionRamToDisk(context)
    }

    private fun collectionFileExist(context: Context): Boolean {
        val collectionPath = context.filesDir.path + "/collections.json"
        val collectionFile = File(collectionPath)
        return collectionFile.exists()
    }

    private fun syncCollectionDiskToRam(context: Context) {
        val collectionPath = context.filesDir.path + "/collections.json"
        val collectionFile = File(collectionPath)
        val bufferedReader = collectionFile.bufferedReader()
        val builder = StringBuilder()
        while (true) {
            val line = bufferedReader.readLine() ?: break
            builder.append(line)
        }
        bufferedReader.close()
        if (builder.isNotEmpty()) {
            val jsonObject = JSONObject(builder.toString())
            collections.clear()
            for (key in jsonObject.keys()) {
                collections.add(Collection(Word(key), Date(jsonObject[key] as Long), usedComparator))
            }
        }
    }

    private fun syncCollectionRamToDisk(context: Context) {
        val collectionPath = context.filesDir.path + "/collections.json"
        val collectionFile = File(collectionPath)
        if (! collectionFile.exists()) {
            collectionFile.createNewFile()
        }
        val outputStream = FileOutputStream(collectionFile, false)
        val jsonObject = JSONObject()
        for (collection in collections) {
            jsonObject.putOpt(collection.word.toString(), collection.date.time)
        }
        outputStream.write(jsonObject.toString().toByteArray())
        outputStream.close()
    }
}