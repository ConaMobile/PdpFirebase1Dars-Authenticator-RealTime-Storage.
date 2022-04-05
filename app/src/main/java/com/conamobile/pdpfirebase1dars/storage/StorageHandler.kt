package com.conamobile.pdpfirebase1dars.storage

interface StorageHandler {
    fun onSuccess(imgUrl: String)
    fun onError(exception: Exception?)
}