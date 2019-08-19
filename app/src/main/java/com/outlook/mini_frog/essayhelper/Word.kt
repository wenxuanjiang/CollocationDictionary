package com.outlook.mini_frog.essayhelper

class Word(private val word: String) : Comparable<Word> {

    val length
        get() = word.length

    fun nextGroup(): Word {
        if (word.last() == 'z') {
            return Word(word + "a")
        }
        return Word(word.substring(0 until word.length - 1) + (word.last() + 1))
    }

    override fun compareTo(other: Word): Int {
        /**
         * word.length == 0 means a special label
         * it needs to be placed as the last element of a tree
         * that means it should be larger than any other word
         */
        if (other.length == 0)
            return -1
        val length = when (other.length > this.length) {
            true -> this.length
            else -> other.length
        }
        for (i in 0 until length) {
            if (this.word[i] > other.word[i]) {
                return 1
            } else if (this.word[i] < other.word[i]) {
                return -1
            }
        }
        return when {
            this.length == other.length -> 0
            this.length > other.length -> 1
            else -> -1
        }
    }

    override fun toString(): String {
        return word
    }
}