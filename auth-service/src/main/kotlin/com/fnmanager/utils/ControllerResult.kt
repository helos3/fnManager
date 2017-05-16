package com.fnmanager.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import spark.Response

data class ControllerResult<T>(var result: T? = null,
                                   var code: Int = 200,
                                   var errorMsg: String? = null) {

    fun isSuccess() = result == null || code >=400

    fun apply(res : Response) : ControllerResult<T> { res.status(code); return this }

    companion object {
        fun <T> result(params: ControllerResult<T>.() -> Unit) = ControllerResult<T>().also(params)
        fun <T> success(result: T) : ControllerResult<T> = ControllerResult(result, 200, null)
        fun <T> success(result: T, code: Int) : ControllerResult<T> = ControllerResult(result, code, null)
        fun <T> error(errorMsg: String) : ControllerResult<T> = ControllerResult(null, 200, errorMsg)
        fun <T> error(errorMsg: String, code: Int) : ControllerResult<T> = ControllerResult(null, code, errorMsg)

        val gson: Gson = GsonBuilder().create()
    }
}

