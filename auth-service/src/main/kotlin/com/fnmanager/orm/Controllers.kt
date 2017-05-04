package com.fnmanager.orm

import spark.Request
import spark.Response
import spark.Route
import spark.Spark
import spark.Spark.*
import java.util.*

val auth = {req : Request, res : Response ->
    req.headers("Authorization")
    ""
}

val register = {req : Request, res : Response -> ""}

val login = {req : Request, res : Response -> ""}


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
