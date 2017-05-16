package com.fnmanager.controller

import com.fnmanager.Credentials
import com.fnmanager.inject.UserModule
import com.fnmanager.utils.ControllerResult
import com.fnmanager.utils.ControllerResult.Companion.error
import com.fnmanager.utils.ControllerResult.Companion.result
import com.fnmanager.utils.ControllerResult.Companion.success
import space.traversal.kapsule.Injects
import spark.Request
import spark.Response

abstract class Controller<T, R> : spark.Route, Injects<T> {
    abstract fun _handle(req: Request, res: Response): ControllerResult<R>

    override fun handle(req: Request, res: Response): ControllerResult<R> = _handle(req, res).apply(res)

}

class RegisterController : Controller<UserModule, String>() {
    val userService by required { loginService }

    override fun _handle(req: Request, res: Response): ControllerResult<String> {
        val cred = Credentials.fromJson(req.body())
        userService.register(cred)
        return success(userService.auth(cred)!!, 200)
    }
}

class AuthController : Controller<UserModule, String>() {
    val userService by required { loginService }

    override fun _handle(req: Request, res: Response): ControllerResult<String> {
        val auth = req.headers("Authorization") ?: return {
            res.status(400)
            result<String> {
                errorMsg = "No authorization header"
                code = 400
            }
        }.invoke()

        return when {
            auth.contains("Basic") -> auth.split(" ")[1]
                .let { Credentials.from(it) }
                .let { userService.auth(it) }
                ?.let { success(it, 200) }
                ?: error("Failed to authenticate with credentials", 400)

            auth.contains("Token") -> {
                val token = auth.split(" ")[1]
                if (userService.auth(token))
                    success(token, 200) else error("Token is not valid or expired", 400)
            }

            else -> error("Invalid authorization header", 400)
        }
    }
}
