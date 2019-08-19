package com.outlook.mini_frog.essayhelper

import java.util.*
import kotlin.Comparator

class Collection(val word: Word, val date: Date, var comparator: Comparator<Collection>): Comparable<Collection> {

    override fun compareTo(other: Collection): Int {
        return comparator.compare(this, other)
    }
}