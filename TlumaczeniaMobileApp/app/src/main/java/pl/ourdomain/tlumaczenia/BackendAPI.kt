package pl.ourdomain.tlumaczenia

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import org.json.JSONObject
import pl.ourdomain.tlumaczenia.exceptions.InvalidCredentials
import pl.ourdomain.tlumaczenia.exceptions.TakenUsername

object API {

    private const val SERVER_IP = "http://192.168.1.21:5000/"

    fun fetchAuthToken(username: String, password: String): String {
        val data = JSONObject()
        data.put("username", username)
        data.put("password", password)

        val (_, response, _) = Fuel.post(SERVER_IP + "login/")
            .header("Content-Type" to "application/json")
            .body(data.toString())
            .response()

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

        val (_, response, _) = Fuel.post(SERVER_IP + "register/")
            .header("Content-Type" to "application/json")
            .body(data.toString())
            .response()

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
