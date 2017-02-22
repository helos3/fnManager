package com.fnmanager.domain.orm


/**
 * exception + optional
 */
class Exceptional<T : Any> {
    companion object {
        fun <T : Any> of(statement: () -> T) : Exceptional<T> {
            return Exceptional<T>(statement)
        }
    }

    constructor(statement: () -> T) {
        this.statement = statement
        exec()
    }

    internal constructor(isSuccess: Boolean, exception: Exception) {
        this.isSuccess = isSuccess
        this.exception = exception
    }


    internal var isSuccess = true
    internal lateinit var exception : Exception
    internal lateinit var statement : () -> T
    internal lateinit var result : T

    internal fun exec() : Exceptional<T> {
        try {
            result = statement.invoke()
        } catch (e: Exception) {
            isSuccess = false
            exception = e
        }
        return this
    }

    fun <A:Any> map(statement: (T) -> A) : Exceptional<A> {
        return if (isSuccess)
            Exceptional.of { statement.invoke(result) }
        else
            Exceptional(false, this.exception)
    }

    fun <B> on(isSuccess: Boolean, statement: () -> B) : Exceptional<T> {
        if (this.isSuccess and isSuccess)
            statement.invoke()
        return this
    }

    fun peek(statement: () -> Unit) : Exceptional<T> {
        statement.invoke()
        return this
    }


    fun orElseThrow() : T {
        if (!isSuccess)
            throw exception
        return result
    }


}