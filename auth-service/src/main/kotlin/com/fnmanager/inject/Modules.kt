package com.fnmanager.inject

import com.fnmanager.IUserService
import com.fnmanager.UserService

class UserModule {
    val loginService: IUserService = UserService()
}