package pl.ourdomain.tlumaczenia.dataclasses

data class RegisterInfo(
    var username: String = "",
    var password: String = "",
    var rePassword: String = "",
    var email: String = "",
    var reEmail: String = ""
)