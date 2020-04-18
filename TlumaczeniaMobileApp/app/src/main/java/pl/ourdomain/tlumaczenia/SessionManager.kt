package pl.ourdomain.tlumaczenia

import android.content.Context
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.lang.Exception

class SessionManager(receivedContext: Context) {

    companion object {
        var username: String? = null
            private set
        var password: String? = null
            private set
        var authToken: String? = null
            private set

        private const val credentialsFileName = "authToken.txt"
    }

    private var context: Context = receivedContext

    init {
        if (username == null || password == null || authToken == null) {
            loadCredentials()
        }
    }

    fun useCredentials(new_username: String, new_password: String) {
        username = new_username
        password = new_password
        authToken = fetchToken()

        saveCredentials()
        loadCredentials()
    }

    fun useCredentials(new_username: String, new_password: String, new_token: String) {
        username = new_username
        password = new_password
        authToken = new_token

        saveCredentials()
        loadCredentials()
    }

    fun logout() {
        username = null
        password = null
        authToken = null

        destroyCredentialsFile()
    }

    fun isLoggedIn(): Boolean {
        return !(username == null || password == null || authToken == null)
    }

    private fun loadCredentials() {
        try {
            // Read data from file
            val file = File(context.filesDir, credentialsFileName)
            val sessionData = JSONObject(file.readText())

            // Move data to class fields
            username = sessionData.get("username") as String?
            password = sessionData.get("password") as String?
            authToken = sessionData.get("token") as String?
        } catch (e: FileNotFoundException) {
            // User was logged out or it's their first visit
            Log.w("SessionManager", "File with credentials not found in the local memory.")
        } catch (e: JSONException) {
            // Destroy faulty file
            destroyCredentialsFile()
        }
    }

    private fun saveCredentials() {
        // Put class files into a JSON
        val sessionData = JSONObject()
        sessionData.put("token", authToken)
        sessionData.put("username", username)
        sessionData.put("password", password)

        // Save to a file
        context.openFileOutput(credentialsFileName, Context.MODE_PRIVATE)
            .use {
                it?.write(sessionData.toString().toByteArray())
            }
    }

    private fun destroyCredentialsFile() {
        context.deleteFile(credentialsFileName)
    }

    private fun fetchToken(): String {
        val username = username
        val password = password
        return if (username == null || password == null) {
            throw Exception("Set credentials first before fetching a token!")
        } else {
            val api = API(context)
            api.fetchAuthToken(username, password)
        }
    }
}