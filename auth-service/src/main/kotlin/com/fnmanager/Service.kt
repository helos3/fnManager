package com.fnmanager

import com.fnmanager.domain.User
import org.jetbrains.exposed.sql.transactions.transaction
import java.security.SecureRandom

interface IUserService {
    fun auth(cred: Credentials): String?
    fun register(cred: Credentials): User
    fun auth(token: String): Boolean
}

class UserService : IUserService {

    internal val tokenStore by lazy { TokenStore() }

    override fun auth(cred: Credentials): String? {
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

    override fun register(cred: Credentials): User = transaction { User.create(cred.login, cred.password) }

    override fun auth(token: String): Boolean = tokenStore.isPresent(token)
}