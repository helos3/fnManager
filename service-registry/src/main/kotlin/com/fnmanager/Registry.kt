package com.fnmanager

import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.QueryParams
import com.ecwid.consul.v1.agent.model.NewService
import com.ecwid.consul.v1.health.model.HealthService
import com.google.common.net.HostAndPort
import com.orbitz.consul.Consul
import com.orbitz.consul.NotRegisteredException
import com.orbitz.consul.model.State
import com.orbitz.consul.model.agent.*
import com.orbitz.consul.model.health.ServiceHealth
import org.jetbrains.kotlin.utils.addToStdlib.check
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock

/**
 * Created by berlogic on 19.04.17.
 */
object Registry {
    //TODO: add tags updating
    internal val consul = Consul.builder().build()
    internal val locks = ConcurrentHashMap<String, ReentrantLock>()

    fun register(name: String?,
                 address: String?,
                 tags: List<String>,
                 port: String?): Registration? {


        val registration = ImmutableRegistration.builder()
                .address(address ?: return null)
                .port(port?.toInt() ?: return null)
                .id(UUID.randomUUID().toString())
                .name(name ?: return null)
                .tags(tags)
                .check(ImmutableRegCheck.builder()
                        .tcp("${address!!}:${port!!}")
                        .interval("7s")
                        .build()
                )
                .build()

        synchronized(registration.name) {
            consul.agentClient().register(registration)
            Thread.sleep(10000)
        }

        return registration

    }

    fun discover(serviceName: String, tags: List<String>): List<ServiceHealth> =
            synchronized(serviceName, {
                if (tags.isEmpty())
                    consul.healthClient().getHealthyServiceInstances(serviceName).response ?: emptyList()
                else
                    tags
                            .map { consul.healthClient().getHealthyServiceInstances(serviceName) }
                            .filter { it.response.size > 0 }
                            .map { it.response }
                            .fold(mutableListOf()) { list1, list2 -> list1.addAll(list2); list1 }
            })


}