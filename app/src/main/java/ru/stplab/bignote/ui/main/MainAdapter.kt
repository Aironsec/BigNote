package ru.stplab.bignote.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_note.*
import ru.stplab.bignote.R
import ru.stplab.bignote.common.getColorInt
import ru.stplab.bignote.data.model.Note

class MainAdapter(val onClickListener: ((Note) -> Unit)? = null) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var notes: List<Note> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(note: Note) {
            with(note) {
                title_note.text = title
                body_note.text = text

                (containerView as CardView).setCardBackgroundColor(
                    color.getColorInt(
                        containerView.context
                    )
                )

                containerView.setOnClickListener {
                    onClickListener?.invoke(this)
                }
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