package fnmanager


/**
 * exception + optional
 */
class Exceptional<T> {
    companion object {
        fun <T> of(statement: () -> T) : Exceptional<T> {
            return Exceptional<T>(statement).exec()
        }
    }

    constructor(statement: () -> T) {
        this.statement = statement
    }

    constructor(isSuccess: Boolean, exception: Exception) {
        this.isSuccess = isSuccess
        this.exception = exception
    }


    internal var isSuccess = true
    internal lateinit var exception : Exception
    internal lateinit var statement : () -> T

    fun exec() : Exceptional<T> {

        try {
            statement.invoke()
        } catch (e: Exception) {
            isSuccess = false
            exception = exception
        }
        return this
    }

    fun <A> map(statement: (T) -> A) : Exceptional<A> {
        exec()
        return if (isSuccess)
            Exceptional.of { statement.invoke(this.statement.invoke()) }
        else
            Exceptional(false, this.exception)
    }

    fun <B> on(isSuccess: Boolean, statement: () -> B) : Exceptional<T> {
        if (isSuccess and isSuccess)
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
        return statement.invoke()
    }


}