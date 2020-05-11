package pl.ourdomain.tlumaczenia.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lesson_row.view.*
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.dataclasses.Lesson

class LessonAdapter(private val lessons: List<Lesson>):
    RecyclerView.Adapter<LessonViewHolder>(){

    private var previouslyExpandedPosition = -1
    private var expandedPosition = -1

    override fun getItemCount(): Int {
        return lessons.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LessonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cellForRow = inflater.inflate(R.layout.lesson_row, parent, false)

        return LessonViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: LessonViewHolder, position: Int) {
        holder.view.title.text = lessons[position].name
        holder.view.lesson.text = lessons[position].text

        // Handle expand
        val isExpanded = position == expandedPosition
        holder.view.lesson.visibility = if (isExpanded) View.VISIBLE else View.GONE

        if (isExpanded)
            previouslyExpandedPosition = position

        holder.itemView.setOnClickListener {
            expandedPosition = if (isExpanded) -1 else position
            notifyItemChanged(previouslyExpandedPosition)
            notifyItemChanged(position)
        }
    }
}

class LessonViewHolder(val view: View) : RecyclerView.ViewHolder(view)