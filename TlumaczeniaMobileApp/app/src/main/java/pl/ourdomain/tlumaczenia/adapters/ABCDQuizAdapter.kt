package pl.ourdomain.tlumaczenia.adapters

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.abcd_quiz_row.view.*
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import java.util.*
import kotlin.coroutines.coroutineContext

class ABCDQuizAdapter(private val translations: List<Translation>) :
    RecyclerView.Adapter<ABCDQuizViewHolder>() {

    private val shuffled: MutableList<Translation> = mutableListOf()
    private var displayResults: Boolean = false

    init {
        // shuffle received list in groups of 4
        var shufflingList: MutableList<Translation> = mutableListOf()
        for (i in translations.indices) {
            shufflingList.add(translations[i])

            if (i % 4 == 3) {
                // Shuffle before adding to the list of all answers
                shufflingList.shuffle()
                shuffled.addAll(shufflingList)

                // Clear temporary list
                shufflingList = mutableListOf()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ABCDQuizViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cellForRow = inflater.inflate(R.layout.abcd_quiz_row, parent, false)

        return ABCDQuizViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        // There is 4 times translations as questions
        return translations.size / 4
    }

    override fun onBindViewHolder(holder: ABCDQuizViewHolder, position: Int) {
        // TODO remove this many .toLowerCase()

        val indexInList = position * 4

        val question =
            (position + 1).toString() + ". " + translations[indexInList].translated.toLowerCase(
                Locale.getDefault()
            ) + " to:"
        holder.view.question.text = question

        // Fill radio buttons with answers
        holder.view.radio_button_1.text =
            shuffled[indexInList].word.toLowerCase(Locale.getDefault())
        holder.view.radio_button_2.text =
            shuffled[indexInList + 1].word.toLowerCase(Locale.getDefault())
        holder.view.radio_button_3.text =
            shuffled[indexInList + 2].word.toLowerCase(Locale.getDefault())
        holder.view.radio_button_4.text =
            shuffled[indexInList + 3].word.toLowerCase(Locale.getDefault())

        if (displayResults) {
            holder.view.radio_button_1.isEnabled = false
            holder.view.radio_button_2.isEnabled = false
            holder.view.radio_button_3.isEnabled = false
            holder.view.radio_button_4.isEnabled = false

            // Give feedback with colors
            val selectedRadioId = holder.view.radio_group_answers.checkedRadioButtonId
            val selectedRadio: RadioButton? = holder.view.findViewById<RadioButton>(selectedRadioId)
            if (selectedRadio?.text == translations[indexInList].word) {
                holder.view.quiz_row_holder.setBackgroundResource(R.drawable.rounded_text_field_positive)
            } else {
                holder.view.quiz_row_holder.setBackgroundResource(R.drawable.rounded_text_field_negative)

                // Highlight the correct answer

                val color = ContextCompat.getColor(holder.view.context, R.color.colorTextCorrect)
                when {
                    holder.view.radio_button_1.text.toString()
                        .toLowerCase(Locale.getDefault()) == translations[indexInList].word.toLowerCase(
                        Locale.getDefault()
                    ) -> {
                        holder.view.radio_button_1.setTextColor(color)
                    }
                    holder.view.radio_button_2.text.toString()
                        .toLowerCase(Locale.getDefault()) == translations[indexInList].word.toLowerCase(
                        Locale.getDefault()
                    ) -> {
                        holder.view.radio_button_2.setTextColor(color)
                    }
                    holder.view.radio_button_3.text.toString()
                        .toLowerCase(Locale.getDefault()) == translations[indexInList].word.toLowerCase(
                        Locale.getDefault()
                    ) -> {
                        holder.view.radio_button_3.setTextColor(color)
                    }
                    holder.view.radio_button_4.text.toString()
                        .toLowerCase(Locale.getDefault()) == translations[indexInList].word.toLowerCase(
                        Locale.getDefault()
                    ) -> {
                        holder.view.radio_button_4.setTextColor(color)
                    }
                }
            }
        }
    }

    fun finishQuiz() {
        displayResults = true
        notifyDataSetChanged()
    }

}

class ABCDQuizViewHolder(val view: View) : RecyclerView.ViewHolder(view)