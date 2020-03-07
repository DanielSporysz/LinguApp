package pl.ourdomain.tlumaczenia

import android.content.Context
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class SessionManager(receivedContext: Context?) {

    companion object {
        var authToken: String? = null
        var context: Context? = null
        const val authTokenFileName = "authToken.txt"
    }

    init {
        context = receivedContext

        /* Read the file that should store authentication token from local memory */
        try {
            val file = File(context?.filesDir, authTokenFileName)
            authToken = file.readText()
        } catch (e: FileNotFoundException) {
            println("AuthToken file not found in the local memory.")
        }
    }

    fun useCredentialsAndFetchAuthToken(username: String, password: String) {
        try {
            authToken = fetchAuthToken(username, password)

            // save the token to local memory
            if (authToken !== null) {
                context?.openFileOutput(authTokenFileName, Context.MODE_PRIVATE)
                    .use {
                        it?.write(authToken!!.toByteArray())
                    }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}