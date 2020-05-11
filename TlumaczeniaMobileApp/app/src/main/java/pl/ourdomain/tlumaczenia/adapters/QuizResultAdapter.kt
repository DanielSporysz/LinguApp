package pl.ourdomain.tlumaczenia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.quiz_answer_row.view.*
import kotlinx.android.synthetic.main.quiz_answer_row.view.srcText
import kotlinx.android.synthetic.main.translation_row.view.*
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import java.util.*

class QuizResultAdapter(
    private val translations: List<Translation>,
    private val answers: List<String>,
    private val isCorrect: List<Boolean>
) :
    RecyclerView.Adapter<QuizAnswerViewHolder>() {

    override fun getItemCount(): Int {
        return translations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizAnswerViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cellForRow = inflater.inflate(R.layout.quiz_answer_row, parent, false)

        return QuizAnswerViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: QuizAnswerViewHolder, position: Int) {
        holder.view.srcText.text = translations[position].word.toLowerCase(Locale.getDefault())
        holder.view.translationText.text = translations[position].translated.toLowerCase(Locale.getDefault())
        holder.view.answerText.text = answers[position].toLowerCase(Locale.getDefault())

        if (isCorrect[position]) {
            holder.view.contents.setBackgroundResource(R.drawable.rounded_text_field_positive)
            holder.view.labelAnswer.isVisible = false
            holder.view.answerText.isVisible = false
        } else {
            holder.view.contents.setBackgroundResource(R.drawable.rounded_text_field_negative)
        }
    }
}

class QuizAnswerViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}