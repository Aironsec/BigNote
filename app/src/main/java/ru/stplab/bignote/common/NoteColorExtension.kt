package ru.stplab.bignote.common

import android.content.Context
import androidx.core.content.res.ResourcesCompat
import ru.stplab.bignote.R
import ru.stplab.bignote.data.model.Color

fun Color.getColorRes() = when (this) {
    Color.WHITE -> R.color.white
    Color.YELLOW -> R.color.yellow
    Color.GREEN -> R.color.green
    Color.BLUE -> R.color.blue
    Color.RED -> R.color.red
    Color.VIOLET -> R.color.violet
}

fun Color.getColorInt(context: Context) = ResourcesCompat.getColor(context.resources, getColorRes(), null)