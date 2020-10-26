package ru.stplab.bignote.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note.view.*
import ru.stplab.bignote.R
import ru.stplab.bignote.common.getColorInt
import ru.stplab.bignote.data.model.Color
import ru.stplab.bignote.data.model.Color.*
import ru.stplab.bignote.data.model.Note

class MainAdapter(val onClickListener: ((Note) -> Unit)? = null) : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(note: Note) = with(itemView) {
            title_note.text = note.title
            body_note.text = note.note

            (itemView as CardView).setCardBackgroundColor(note.color.getColorInt(context))

            itemView.setOnClickListener {
                onClickListener?.invoke(note)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note, parent, false)
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(notes[position])

    override fun getItemCount() = notes.size
}