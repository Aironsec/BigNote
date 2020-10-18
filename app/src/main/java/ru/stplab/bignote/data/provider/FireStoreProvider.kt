package ru.stplab.bignote.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.*
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult

private const val NOTES_COLLECTION = "notes"

class FireStoreProvider : DataProvider {

    private val db = FirebaseFirestore.getInstance()
    private val notesReference = db.collection(NOTES_COLLECTION)

    override fun subscribeToAllNotes(): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.addSnapshotListener { snapshot, error ->
           error?.let {
                result.value = NoteResult.Error(error)
               return@addSnapshotListener
            }
            snapshot?.let { it ->
                val notes = it.documents.map { it.toObject(Note::class.java) }
                result.value = NoteResult.Success(notes)
            }
        }

        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(id).get()
            .addOnSuccessListener { snapshot -> result.value =
                    NoteResult.Success((snapshot.toObject(Note::class.java)) as Note)
            }.addOnFailureListener { result.value = NoteResult.Error(it) }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()

        notesReference.document(note.id)
            .set(note).addOnSuccessListener {
                result.value = NoteResult.Success(note)
            }
            .addOnFailureListener {
                result.value = NoteResult.Error(it)
            }

        return result
    }
}