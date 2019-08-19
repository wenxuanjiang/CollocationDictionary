package com.outlook.mini_frog.essayhelper

import android.content.Context
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.*
import java.util.stream.Collectors.toList
import kotlin.collections.ArrayList


object WordManager {

    val words = ArrayList<Word>()

    fun init(context: Context) {
        words.clear()
        val inReader = InputStreamReader(context.assets.open("words.list"), "UTF-8")
        val bufferedReader = BufferedReader(inReader)
        while (true) {
            val line = bufferedReader.readLine() ?: break
            words.add(Word(line))
        }
    }

    private fun wordToFileName(word: Word): String {
        return word.toString().replace(" ", "+") + ".json"
    }

    fun getDescription(context: Context, word: Word): JSONObject {
        val fileName = wordToFileName(word)
        val inReader = InputStreamReader(context.assets.open("words/$fileName"), "UTF-8")
        val bufferedReader = BufferedReader(inReader)
        val builder = StringBuilder()
        while (true) {
            val line = bufferedReader.readLine() ?: break
            builder.append(line)
        }
        bufferedReader.close()
        inReader.close()
        return JSONObject(builder.toString())
    }
}
