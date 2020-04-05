package pl.ourdomain.tlumaczenia

import pl.ourdomain.tlumaczenia.dataclasses.Translation

class QuizState {
    companion object{
        var translations: List<Translation>? = null
        var answers: List<String>? = null
        var isCorrect: List<Boolean>? = null
    }
}