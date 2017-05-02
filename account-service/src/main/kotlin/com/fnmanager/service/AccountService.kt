package com.fnmanager.service

import com.fnmanager.config.IMicroserviceConfig
import com.fnmanager.config.MicroserviceConfig

/**
 * Created by berlogic on 06.04.17.
 */
class AccountService(abc: IMicroserviceConfig) : IMicroserviceConfig by abc

fun abc() = AccountService(MicroserviceConfig("application")).loadBoolProperty("sahsee")
