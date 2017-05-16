package com.fnmanager

import com.orbitz.consul.Consul
import org.junit.BeforeClass
import org.junit.Ignore
import org.junit.Test
import sun.nio.ch.ThreadPool
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.concurrent.thread
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestRegistry {

    fun serviceSuccess() {
        val serviceName = UUID.randomUUID().toString()

        val service = Registry.register(serviceName, "87.240.165.82", listOf("East", "West"), "80")
        assertTrue { service?.let { true } ?: false }

        val found = Registry.discover(serviceName, listOf())
        assertEquals(1, found.size)

        assertEquals(found.size, Registry.discover(serviceName, listOf()).size)
    }

    fun serviceFail() {
        val serviceName = UUID.randomUUID().toString()

        val service = Registry.register(serviceName, "172.16.0.5", listOf("East", "West"), "444")

        val found = Registry.discover(serviceName, listOf())
        assertEquals(0, found.size)
    }


    fun serviceConcurrent() {
        val serviceName = UUID.randomUUID().toString()
        val executor = Executors.newFixedThreadPool(2)

        thread(start = true){
            Registry.register(serviceName, "87.240.165.82", listOf("East", "West"), "80")
        }.join()

        thread(start = true){
            Thread.sleep(500)
            val found = Registry.discover(serviceName+"", listOf())
            assertEquals(1, found.size)
        }.join()
    }

    fun serviceConcurrentError() {
        val serviceName = UUID.randomUUID().toString()

        thread(start = true){
            Registry.register(serviceName, "87.240.165.82", listOf("East", "West"), "80")
        }.join()

        thread(start = true){
            Thread.sleep(500)
            val found = Registry.discover(serviceName+1, listOf())
            assertEquals(0, found.size)
        }.join()
    }

}

