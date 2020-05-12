package pl.ourdomain.tlumaczenia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.translation_row.view.*
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import java.util.*

class TranslationsAdapter(receiveWords: List<Translation>) :
    RecyclerView.Adapter<TranslationViewHolder>() {

    private val words = receiveWords

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TranslationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cellForRow = inflater.inflate(R.layout.translation_row, parent, false)

        return TranslationViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: TranslationViewHolder, position: Int) {
        holder.view.lang.text = words[position].lang.toLowerCase(Locale.getDefault())
        holder.view.srcText.text = words[position].word.toLowerCase(Locale.getDefault())
        holder.view.dstText.text = words[position].translated.toLowerCase(Locale.getDefault())
    }
}

class TranslationViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

}