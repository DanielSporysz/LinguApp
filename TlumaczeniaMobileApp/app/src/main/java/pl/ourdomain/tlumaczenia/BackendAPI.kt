package pl.ourdomain.tlumaczenia

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import com.google.gson.GsonBuilder
import org.json.JSONObject
import pl.ourdomain.tlumaczenia.dataclasses.Language
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import pl.ourdomain.tlumaczenia.exceptions.InvalidCredentials
import pl.ourdomain.tlumaczenia.exceptions.TakenUsername

object API {

    private const val SERVER_IP = "http://192.168.1.15:5000/"

    private fun post(data: JSONObject, path: String): Response {
        val (_, response, _) = Fuel.post(SERVER_IP + path)
            .header("Content-Type" to "form-data")
            .body(data.toString())
            .response()
        return response
    }

    private fun get(data: JSONObject, path: String): Response {
        val myParams = mutableListOf<Pair<String, Any?>>()
        for (key in data.keys()){
            myParams.add(Pair(key, data.get(key)))
        }

        //TODO remove body
        val (sent, response, _) = Fuel.get(SERVER_IP + path, myParams)
            .body(data.toString())
            .header("Content-Type" to "multipart/form-data")
            .response()

        //TODO remove test
        val test  = sent.toString()
        return response
    }

    fun translate(text: String, srcLang: String, dstLang: String): String {
        val data = JSONObject()
        data.put("text", text)
        data.put("src_lang", srcLang)
        data.put("destination_lang", dstLang)

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

    fun fetchSavedWords(): List<Translation> {
        val data = JSONObject()
        data.put("lang", "english")

        val response = get(data, "vocabulary/")

        // Scrap languages data from response
        val regex = Regex("\\{(.*?)\\}")
        val matches = regex.findAll(String(response.data))
        val jsonList = mutableListOf<String>()
        matches.forEach { f ->
            // rename fields to match Translation.kt
            val m = f.value
                .replace("translation", "translated")
            jsonList.add(m)
        }

        // Map to Kotlin Objects
        val translations = mutableListOf<Translation>()
        for (json in jsonList) {
            val gson = GsonBuilder().create()
            val translation = gson.fromJson(json, Translation::class.java)
            translations.add(translation)
        }

        when (response.statusCode) {
            200 -> {
                return translations
            }
            else -> {
                throw java.lang.Exception("Error getting saved translations list!")
            }
        }
    }

    fun fetchSupportedLanguages(): List<Language> {
        val data = JSONObject()

        val response = get(data, "languages/")

        // Scrap languages data from response
        val regex = Regex("\\{(.*?)\\}")
        val matches = regex.findAll(String(response.data))
        val jsonList = mutableListOf<String>()
        matches.forEach { f ->
            // covert to camel case names
            val m = f.value
                .replace("polish_name", "polishName")
                .replace("short_name", "shortName")
            jsonList.add(m)
        }

        // Map to Kotlin Objects
        val languages = mutableListOf<Language>()
        for (json in jsonList) {
            val gson = GsonBuilder().create()
            val language = gson.fromJson(json, Language::class.java)
            languages.add(language)
        }

        when (response.statusCode) {
            200 -> {
                return languages
            }
            else -> {
                throw java.lang.Exception("Error getting supported languages list!")
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
