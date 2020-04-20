package pl.ourdomain.tlumaczenia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.abcd_quiz_row.view.*
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.dataclasses.Translation

class ABCDQuizAdapter(private val translations: List<Translation>): RecyclerView.Adapter<ABCDQuizViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ABCDQuizViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cellForRow = inflater.inflate(R.layout.abcd_quiz_row, parent, false)

        return ABCDQuizViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        // There is 4 times translations as questions
        return translations.size % 4
    }

    override fun onBindViewHolder(holder: ABCDQuizViewHolder, position: Int) {
        val indexInList = position * 4

        val question = translations[indexInList].translated + " to:"

        // Fill radio buttons with answers
        holder.view.radio_button_1.text = translations[indexInList].word
        holder.view.radio_button_2.text = translations[indexInList + 1].word
        holder.view.radio_button_3.text = translations[indexInList + 2].word
        holder.view.radio_button_4.text = translations[indexInList + 3].word
    }

}

class ABCDQuizViewHolder(val view: View): RecyclerView.ViewHolder(view)