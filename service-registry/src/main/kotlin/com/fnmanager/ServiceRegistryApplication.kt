package com.fnmanager

import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.QueryParams
import com.ecwid.consul.v1.agent.model.NewService
import com.ecwid.consul.v1.health.model.HealthService
import org.jetbrains.kotlin.resolve.MultiTargetPlatform.Common.compareTo
import spark.Spark
import java.util.*
import javax.imageio.spi.ServiceRegistry

/**
 * Created by berlogic on 18.04.17.
 */



object ServiceRegistryApplication {
    fun main(args: Array<String>) {

        registerControllers()
    }
}

