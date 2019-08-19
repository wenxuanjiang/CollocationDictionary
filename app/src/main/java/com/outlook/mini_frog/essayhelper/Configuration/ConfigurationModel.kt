package com.outlook.mini_frog.essayhelper.Configuration

abstract class ConfigurationModel<T> {

    companion object {
        const val CONFIGURATION_TYPE_UNDEFINED = 0
        const val CONFIGURATION_TYPE_RANGE = 1
        const val CONFIGURATION_TYPE_CHECKBOX = 2
        const val CONFIGURATION_TYPE_SELECT = 3
    }

    var runnable = Runnable {
        // just do nothing
    }
    abstract val configureName: String
    abstract val configureType: Int
    abstract val configure: ConfigurationBaseType<T>

    abstract fun getBool(): Boolean

    abstract fun getRange(): Array<Int>

    abstract fun getOptions(): Array<String>

    open fun refresh() {
        runnable.run()
    }
}