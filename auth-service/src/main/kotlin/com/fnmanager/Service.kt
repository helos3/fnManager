package com.fnmanager

import com.fnmanager.domain.User
import com.google.common.cache.CacheBuilder
import com.google.common.collect.BiMap
import com.google.common.collect.Maps
import org.h2.mvstore.MVMap
import org.jetbrains.kotlin.com.intellij.util.containers.BidirectionalMap
import space.traversal.kapsule.Injects
import spark.Request
import sun.security.util.Password
import java.security.SecureRandom

class UserService {

    internal val tokenStore by lazy { TokenStore() }

    fun auth(cred: Credentials): String? {
        val found = User.findById(cred.login)?.takeIf {
            encodeWithSalt(
                cred.password.toByteArray(Charsets.UTF_8),
                decodeBase64(it.salt)
            ) == it.password
        } ?: return null

        return tokenStore.get(found) ?: {
            val token = encodeWithSalt(
                found.login.toByteArray(Charsets.UTF_8),
                SecureRandom().generateSeed(16))
            tokenStore.putIfAbsent(token, found)
            token
        }.invoke()
    }

    fun register(cred: Credentials): User = User.create(cred.login, cred.password)

    fun auth(token: String): Boolean = tokenStore.isPresent(token)
}