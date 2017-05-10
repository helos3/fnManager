package com.fnmanager

import spark.Request
import spark.Response
import spark.Route
import spark.Spark
import spark.Spark.*
import java.util.*

internal val service = UserService()

fun abc() {
    Spark.get("", ::auth)
}

fun auth(req: Request, res: Response) : String {
    val auth = req.headers("Authorization") ?: return {
        res.status(400)
        "No authorization header"
    }.invoke()

    return if (auth.contains("Basic"))
         service.auth(Credentials.from(auth.split(" ")[1])) ?: "Failed to authenticate with credentials"
    else if (auth.contains("Token")) {
        val token = auth.split(" ")[1]
        if (service.auth(token)) token else "Failed to authenticate with token"
    }
    else { res.status(400); "Invalid authorization header" }

}


val register = { req : Request, res : Response -> ""}

val login = { req : Request, res : Response -> ""}




//var serveLoginPage = { request: Request, response: Response ->
//    val model = HashMap<String, Any>()
//    model.put("loggedOut", removeSessionAttrLoggedOut(request))
//    model.put("loginRedirect", removeSessionAttrLoginRedirect(request))
//    ViewUtil.render(request, model, Path.Template.LOGIN)
//}
//
//var handleLoginPost = { request: Request, response: Response ->
//    val model = HashMap<String, Any>()
//    if (!UserController.authenticate(getQueryUsername(request), getQueryPassword(request))) {
//        model.put("authenticationFailed", true)
//        return ViewUtil.render(request, model, Path.Template.LOGIN)
//    }
//    model.put("authenticationSucceeded", true)
//    request.session().attribute("currentUser", getQueryUsername(request))
//    if (getQueryLoginRedirect(request) != null) {
//        response.redirect(getQueryLoginRedirect(request))
//    }
//    ViewUtil.render(request, model, Path.Template.LOGIN)
//}
//
//var handleLogoutPost = { request: Request, response: Response ->
//    request.session().removeAttribute("currentUser")
//    request.session().attribute("loggedOut", true)
//    response.redirect(Path.Web.LOGIN)
//    null
//}
//
//// The origin of the request (request.pathInfo()) is saved in the session so
//// the user can be redirected back after login
//fun ensureUserIsLoggedIn(request: Request, response: Response) {
//    if (request.session().attribute<Any>("currentUser") == null) {
//        request.session().attribute("loginRedirect", request.pathInfo())
//        response.redirect(Path.Web.LOGIN)
//    }
//};
