package com.fnmanager

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import kotlin.experimental.xor


internal fun hash(original: ByteArray) = MessageDigest.getInstance("SHA-512").digest(original)

fun encodeWithoutSalt(original: ByteArray): String = encodeBase64(hash(original))

fun randomBytes(number: Int): ByteArray = SecureRandom().generateSeed(number)

fun encodeWithSalt(original: ByteArray, salt: ByteArray): String =
    //hash(hash(original) XOR salt)
    encodeBase64(hash(xorWithKey(hash(original), salt)))


fun encodeBase64(input: ByteArray): String = Base64.getEncoder().encode(input).toString(Charsets.UTF_8)

fun decodeBase64(input: String): ByteArray = Base64.getDecoder().decode(input)

private fun xorWithKey(a: ByteArray, key: ByteArray): ByteArray {
    val out = ByteArray(a.size)
    for (i in a.indices) {
        out[i] = (a[i] xor key[i % key.size])
    }
    return out
}

