package pl.ourdomain.tlumaczenia

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import pl.ourdomain.tlumaczenia.API.fetchAuthToken
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class SessionManager(receivedContext: Context) {

    companion object {
        var username: String? = null
        var password: String? = null
        var authToken: String? = null

        const val credentialsFileName = "authToken.txt"
    }

    private var context: Context = receivedContext

    init {
        if (username == null || password == null || authToken == null) {
            loadFromMemory()
        }
    }

    private fun loadFromMemory() {
        try {
            // Read data from file
            val file = File(context.filesDir, credentialsFileName)
            val sessionData = JSONObject(file.readText())

            // Move data to class fields
            username = sessionData.get("username") as String?
            password = sessionData.get("password") as String?
            authToken = sessionData.get("token") as String?
        } catch (e: FileNotFoundException) {
            Log.w("SessionManager", "File with credentials not found in the local memory.")
        } catch (e: JSONException) {
            Log.w("SessionManager", "File with credentials has data in unsupported format!.")
        }
    }

    fun useCredentials(username: String, password: String) {
        SessionManager.username = username
        SessionManager.password = password

        fetchToken()
    }

    private fun fetchToken() {
        val username = username
        val password = password
        if (username == null || password == null) {
            throw Exception("Set credentials first before fetching a token!")
        } else {
            try {
                // Fetch a token and prepare data to be saved to a JSON file
                val sessionData = JSONObject()
                sessionData.put("token", fetchAuthToken(username, password))
                sessionData.put("username", username)
                sessionData.put("password", password)

                // save the data to a JSON file
                context.openFileOutput(credentialsFileName, Context.MODE_PRIVATE)
                    .use {
                        it?.write(sessionData.toString().toByteArray())
                    }

                // update class fields
                loadFromMemory()
            } catch (e: Exception) {
                throw e
            }
        }
    }
}