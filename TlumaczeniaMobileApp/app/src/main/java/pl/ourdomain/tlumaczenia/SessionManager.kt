package pl.ourdomain.tlumaczenia

import java.lang.Exception

class SessionManager {

    private var authToken: String? = null
        get() = field

    init {
        /* Read the file that should store authentication token from local memory */
        //TODO reading
    }

    fun useCredentials(username: String, password: String) {
        try {
            authToken = fetchAuthToken(username, password)
        } catch (e: Exception) {
            throw e
        }
    }
}