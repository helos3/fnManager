package com.fnmanager

import com.fnmanager.domain.User
import com.google.common.cache.Cache
import com.google.common.cache.CacheBuilder
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


//infernal workaround over bidirectional cache
class TokenStore {
    internal val tokenMap: Cache<String, User> = CacheBuilder.newBuilder()
        .expireAfterAccess(1, TimeUnit.HOURS)
        .build<String, User>()
    internal val userMap: MutableMap<User, String> = ConcurrentHashMap()

    fun putIfAbsent(token: String, user: User) {
        if (isPresent(user)) return
        userMap.put(user, token)
        tokenMap.put(token, user)
    }

    fun isPresent(user: User): Boolean {
        val userExists = userMap[user]?.let { true } ?: false
        val tokenExists = tokenMap.takeIf { userExists }
            ?.getIfPresent(userMap[user])
            ?.let { true } ?: false

        userMap.takeIf { userExists && !tokenExists }?.remove(user)

        return userExists && tokenExists
    }

    fun isPresent(token: String): Boolean = tokenMap.getIfPresent(token)?.let { true } ?: false

    fun get(token: String): User? = tokenMap.getIfPresent(token)

    fun get(user: User): String? = userMap.takeIf { isPresent(user) }?.get(user)


}