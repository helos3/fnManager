package com.fnmanager.utils

data class ControllerResult<T>(var result: T? = null,
                                   var code: Int = 200,
                                   var errorMsg: String? = null) {

    fun isSuccess() = result == null || code >=400

    companion object {
        fun <T> result(params: ControllerResult<T>.() -> Unit) = ControllerResult<T>().also(params)
        fun <T> success(result: T) : ControllerResult<T> = ControllerResult(result, 200, null)
        fun <T> success(result: T, code: Int) : ControllerResult<T> = ControllerResult(result, code, null)
        fun <T> error(errorMsg: String) : ControllerResult<T> = ControllerResult(null, 200, errorMsg)
        fun <T> error(errorMsg: String, code: Int) : ControllerResult<T> = ControllerResult(null, code, errorMsg)
    }
}

