package com.conamobile.pdpfirebase1dars.managers

import java.lang.Exception

interface AuthHandler {
    fun onSuccess()
    fun onError(exception: Exception?)
}
