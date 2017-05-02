package com.fnmanager

import com.fnmanager.config.MicroserviceConfig


/**
 * Created by rushan on 2/12/2017.
 */

object ConfigApp {
    fun main(args: Array<String>) {
        MicroserviceConfig("application")
        registerControllers()

    }

}


