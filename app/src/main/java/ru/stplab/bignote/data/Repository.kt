package ru.stplab.bignote.data

import ru.stplab.bignote.data.model.Note

object Repository {
    val notes: List<Note> = listOf(
        Note("Заметка 1", "Техт заметки 1", 0xfff08080.toInt()),
        Note("Заметка 2", "Техт заметки 2. Как прекрасен этот мир посмотри", 0xffffb6c1.toInt()),
        Note("Заметка 3", "Техт заметки 3", 0xffff7f50.toInt()),
        Note("Заметка 4", "Техт заметки 4", 0xffffe4b5.toInt()),
        Note("Заметка 5", "Техт заметки 5", 0xffd8bfd8.toInt()),
        Note("Заметка 6", "Техт заметки 6", 0xffffe4c4.toInt()),
        Note("Заметка 7", "Техт заметки 7", 0xfffaf0e6.toInt())
    )

}