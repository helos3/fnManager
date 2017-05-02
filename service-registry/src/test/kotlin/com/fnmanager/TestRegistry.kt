package com.fnmanager

import com.orbitz.consul.Consul
import org.junit.BeforeClass
import org.junit.Test
import java.util.*
import kotlin.test.assertTrue


class TestRegistry {

    @Test
    fun serviceSuccess() {
//        val serviceName = UUID.randomUUID().toString()

        val service = Registry.register("test1", "87.240.165.82", listOf("East", "West"), "80")

        assertTrue { service?.let { true } ?: false }
//        Thread.sleep(10000)

        val found = Registry.discover("test1", listOf())
        println(found)
    }

    @Test
    fun serviceFail() {
        val service = Registry.register("test3", "172.16.0.5", listOf("East, West"), "444")

        assertTrue { service?.let { false } ?: true }

        val found = Registry.discover("test3", listOf())
        println()
    }
}

