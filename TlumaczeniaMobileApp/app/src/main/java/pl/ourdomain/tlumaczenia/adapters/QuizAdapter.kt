package pl.ourdomain.tlumaczenia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.translation_row.view.*
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.dataclasses.Translation

class QuizAdapter(receivedTranslations: List<Translation>) :
    RecyclerView.Adapter<QuizViewHolder>() {

    val holderList = receivedTranslations.map { String() }.toTypedArray()

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

        // in case the view was recycled
        holder.view.dstText.text = holderList[position]

        holder.view.dstText.doOnTextChanged { text, start, count, after ->
            holderList[position] = text.toString()
        }
    }
}

class QuizViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}