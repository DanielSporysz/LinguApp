package pl.ourdomain.tlumaczenia

import android.content.Context
import java.lang.Exception

class SessionManager(context: Context?) {

    companion object {
        var authToken: String? = null
    }

    init {
        /* Read the file that should store authentication token from local memory */
        //TODO reading
        val path = context?.filesDir
        //TODO remove this print
        println(path)
    }

    fun useCredentials(username: String, password: String) {
        try {
            authToken = fetchAuthToken(username, password)
        } catch (e: Exception) {
            throw e
        }
    }
}