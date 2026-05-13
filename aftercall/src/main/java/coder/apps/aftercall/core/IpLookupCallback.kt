package coder.apps.aftercall.core

interface IPCallback {
    fun ipCallback(isSuccess: Boolean, countryCode: String?)
}