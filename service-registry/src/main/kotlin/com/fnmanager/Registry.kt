package com.fnmanager

import com.ecwid.consul.v1.ConsulClient
import com.ecwid.consul.v1.QueryParams
import com.ecwid.consul.v1.agent.model.NewService
import com.ecwid.consul.v1.health.model.HealthService
import com.google.common.net.HostAndPort
import com.orbitz.consul.Consul
import com.orbitz.consul.NotRegisteredException
import com.orbitz.consul.model.State
import com.orbitz.consul.model.agent.Check
import com.orbitz.consul.model.agent.ImmutableCheck
import com.orbitz.consul.model.agent.ImmutableRegistration
import com.orbitz.consul.model.agent.Registration
import com.orbitz.consul.model.health.ServiceHealth
import org.jetbrains.kotlin.utils.addToStdlib.check
import java.util.*

/**
 * Created by berlogic on 19.04.17.
 */
object Registry {
    //TODO: add tags updating
    internal val consul = Consul.builder().build()
//    internal val client = ConsulClient()

    fun register(name: String?,
                 address: String?,
                 tags: List<String>,
                 port: String?): Registration? {



        val registration = ImmutableRegistration.builder()
            .address(address ?: return null)
            .port(port?.toInt() ?: return null)
            .id(UUID.randomUUID().toString())
            .name(name ?: return null)
//            .checks(listOf(Registration.RegCheck.script("/usr/bin/telnet ${address!!} ${port!!}", 3, 10)))
//            .tags(tags)
//            .check(Registration.RegCheck.ttl(20))
            .build().withCheck(Registration.RegCheck.tcp("${address!!}:${port!!}", 3))



        consul.agentClient().register(registration)

//        consul.agentClient().register(registration.port.get(),
//            Registration.RegCheck.tcp("${address!!}:${port!!}", 3),
//            registration.name, registration.id)

//        consul.agentClient().registerCheck(ImmutableCheck.builder()
//            .ttl("5s")
////            .tcp(HostAndPort.fromParts(registration.address.get(), registration.port.get()).toString())
//            .script("/usr/bin/telnet ${registration.address.get()} ${registration.port.get()}")
//            .serviceId(registration.id)
//            .interval("2s")
//            .name("${registration.id} check")
//            .id(registration.id)
//            .build().withInterval("2s")
//        )
//        consul.agentClient().registerCheck(registration.id, registration.id,
//            HostAndPort.fromParts(registration.address.get(), registration.port.get()), 10L)
//        consul.agentClient().passCheck(registration.id)
//        consul.agentClient().pass(registration.id)
//        try {
//        } catch (e: NotRegisteredException) {
//            return null
//        }
//        consul.agentClient().pass(registration.id)
//        consul.agentClient().check(registration.id, State.PASS, "nop")

        return registration
    }

    fun discover(serviceName: String?, tags: List<String>): List<ServiceHealth> =
        if (tags.isEmpty())
            consul.healthClient().getHealthyServiceInstances(serviceName)?.response ?: emptyList()
        else
            tags
                .map { consul.healthClient().getHealthyServiceInstances(serviceName) }
                .filter { it.response.size > 0 }
                .map { it.response }
                .fold(mutableListOf()) { list1, list2 -> list1.addAll(list2); list1 }


}