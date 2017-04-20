package com.fnmanager

import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.QueryParams
import com.ecwid.consul.v1.agent.model.NewService
import com.ecwid.consul.v1.health.model.HealthService
import java.util.*

/**
 * Created by berlogic on 19.04.17.
 */
object Registry {
    val client = ConsulClient()

    fun register(name: String?,
                 address: String?,
                 tags: List<String>,
                 port: String?): NewService? {
        val service = NewService()
        service.address = address ?: return null
        service.id = UUID.randomUUID().toString()
        service.name = name ?: return null
        service.tags = tags
        service.port = port?.toInt() ?: return null


        val check = NewService.Check()
        check.script = "ping -c1 ${service.address}:${service.port}"
        check.interval = "30"
        service.check = check

        client.agentServiceRegister(service)
        return service
    }

    fun discover(serviceName: String?, tags: List<String>): List<HealthService> =
        if (tags.isEmpty())
            client.getHealthServices(serviceName, true, QueryParams.DEFAULT)?.value ?: emptyList()
        else
            tags
                    .map { client.getHealthServices(serviceName, it, true, QueryParams.DEFAULT) }
                    .filter { it.value.size > 0 }
                    .map { it.value }
                    .fold(mutableListOf()) { list1, list2 -> list1.addAll(list2); list1 }


}