package pl.ourdomain.tlumaczenia

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.core.ResponseResultOf
import org.json.JSONObject
import pl.ourdomain.tlumaczenia.exceptions.InvalidCredentials
import pl.ourdomain.tlumaczenia.exceptions.TakenUsername

object API {

    private const val SERVER_IP = "http://192.168.1.15:5000/"

    private fun post(data: JSONObject, path: String): Response {
        val (_, response, _) = Fuel.post(SERVER_IP + path)
            .header("Content-Type" to "application/json")
            .body(data.toString())
            .response()
        return response
    }

    fun translate(text: String): String {
        val data = JSONObject()
        data.put("text", text)

        val response = post(data, "translate/")

        when (response.statusCode) {
            200 -> {
                return JSONObject(String(response.data)).get("translation").toString()
            }
            else -> {
                throw java.lang.Exception("Error translating a text!")
            }
        }
    }

    fun fetchAuthToken(username: String, password: String): String {
        val data = JSONObject()
        data.put("username", username)
        data.put("password", password)

        val response = post(data, "login/")

        when (response.statusCode) {
            200 -> {
                return JSONObject(String(response.data)).get("token").toString()
            }
            401 -> {
                throw InvalidCredentials("401")
            }
            else -> {
                throw java.lang.Exception("Couldn't fetch a token!")
            }
        }
    }

    fun registerUser(username: String, password: String): String {
        val data = JSONObject()
        data.put("username", username)
        data.put("password", password)

        val response = post(data, "register/")

        when (response.statusCode) {
            201 -> {
                return JSONObject(String(response.data)).get("token").toString()
            }
            409 -> {
                throw TakenUsername("409")
            }
            else -> {
                throw java.lang.Exception("Couldn't fetch a token!")
            }
        }
    }
}
