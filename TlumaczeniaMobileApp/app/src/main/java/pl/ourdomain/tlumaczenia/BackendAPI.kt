package pl.ourdomain.tlumaczenia

import android.content.Context
import android.content.SharedPreferences
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.Response
import com.google.gson.GsonBuilder
import org.json.JSONObject
import pl.ourdomain.tlumaczenia.dataclasses.Language
import pl.ourdomain.tlumaczenia.dataclasses.Lesson
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import pl.ourdomain.tlumaczenia.exceptions.InvalidCredentials
import pl.ourdomain.tlumaczenia.exceptions.TakenUsername


class API(receivedContext: Context) {

    private var context: Context = receivedContext

    private var preferences: SharedPreferences

    init {
        preferences = context.getSharedPreferences(
            context.getString(R.string.file_name_preferences),
            Context.MODE_PRIVATE
        )
    }

    private fun post(data: JSONObject, path: String): Response {
        val ip = preferences.getString(
            context.getString(R.string.settings_server_ip_key),
            context.getString(R.string.settings_server_ip_default)
        )

        val (_, response, _) = Fuel.post(ip + path)
            .header("Content-Type" to "application/json")
            .body(data.toString())
            .response()
        return response
    }

    private fun get(data: JSONObject, path: String): Response {
        val ip = preferences.getString(
            context.getString(R.string.settings_server_ip_key),
            context.getString(R.string.settings_server_ip_default)
        )

        val myParams = mutableListOf<Pair<String, Any?>>()
        for (key in data.keys()) {
            myParams.add(Pair(key, data.get(key)))
        }

        val (_, response, _) = Fuel.get(ip + path, myParams)
            .response()

        return response
    }


    fun translate(text: String, srcLang: String, dstLang: String, token: String): String {
        val data = JSONObject()
        data.put("text", text)
        data.put("src_lang", srcLang)
        data.put("destination_lang", dstLang)
        data.put("token", token)

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

    fun saveTranslation(
        token: String,
        src_text: String,
        dst_text: String,
        src_lang: String
    ): Boolean {
        val data = JSONObject()
        data.put("token", token)
        data.put("src_lang", src_lang)
        data.put("src_text", src_text)
        data.put("dst_text", dst_text)

        val response = post(data, "vocabulary/save/")

        when (response.statusCode) {
            200 -> {
                return true
            }
            else -> {
                throw java.lang.Exception("Error saving translation!")
            }
        }
    }

    fun fetchQuiz(token: String, lang: String): List<Translation> {
        return fetchTranslations(token, "quiz/", lang)
    }

    fun fetchSavedTranslations(token: String): List<Translation> {
        return fetchTranslations(token, "vocabulary/saved/", null)
    }

    private fun fetchTranslations(token: String, url: String, lang: String?): List<Translation> {
        val data = JSONObject()
        if (lang != null) {
            data.put("lang", lang)
        }
        data.put("token", token)

        val response = get(data, url)

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

    fun fetchLessons(token: String, lang: String): List<Lesson>{
        val data = JSONObject()
        data.put("token", token)
        data.put("lang", lang)

        val response = get(data, "lessons/")

        // Scrap languages data from response
        val regex = Regex("\\{(.*?)\\}")
        val matches = regex.findAll(String(response.data))
        val jsonList = mutableListOf<String>()
        matches.forEach { f ->
            jsonList.add(f.value)
        }

        // Map to Kotlin Objects
        val lessons = mutableListOf<Lesson>()
        for (json in jsonList) {
            val gson = GsonBuilder().create()
            val language = gson.fromJson(json, Lesson::class.java)
            lessons.add(language)
        }

        when (response.statusCode) {
            200 -> {
                return lessons
            }
            else -> {
                throw java.lang.Exception("Error getting supported languages list!")
            }
        }
    }

    fun fetchSupportedLanguages(token: String): List<Language> {
        val data = JSONObject()
        data.put("token", token)

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
