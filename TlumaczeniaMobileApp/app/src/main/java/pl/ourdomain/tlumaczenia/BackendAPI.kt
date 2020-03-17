package pl.ourdomain.tlumaczenia

object API {
    fun fetchAuthToken(username: String, password: String): String {
        try {
            //TODO actual token fetching from a server API
            return __mockup__fetchAuthToken(username, password)
        } catch (e: Exception) {
            throw e
        }
    }

    //TODO remove this mockup
    fun __mockup__fetchAuthToken(username: String, password: String): String {
        if (username == "Aaa" && password == "bbb") {
            return "niceToken"
        } else {
            throw Exception("Incorrect credentials")
        }
    }
}
