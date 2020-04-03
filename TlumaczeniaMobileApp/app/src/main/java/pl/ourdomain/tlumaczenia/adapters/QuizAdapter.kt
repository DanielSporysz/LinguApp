package pl.ourdomain.tlumaczenia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.translation_row.view.*
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.dataclasses.Translation

class QuizAdapter(receivedTranslations: List<Translation>) :
    RecyclerView.Adapter<QuizViewHolder>() {

    private val translations = receivedTranslations

    override fun getItemCount(): Int {
        return translations.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cellForRow = inflater.inflate(R.layout.quiz_row, parent, false)

        return QuizViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.view.srcText.text = translations[position].word
    }
}

class QuizViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}