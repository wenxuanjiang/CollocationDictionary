package com.outlook.mini_frog.essayhelper.Configuration

import com.outlook.mini_frog.essayhelper.Configuration.ConfigurationModel

interface Configurable {

    fun register(): Array<ConfigurationModel<Boolean>>
}