package com.outlook.mini_frog.essayhelper.Configuration

class ConfigurationTypeRange<T: Comparable<T>>: ConfigurationBaseType<T> {
    lateinit var rangeMin: T
    lateinit var rangeMax: T
}