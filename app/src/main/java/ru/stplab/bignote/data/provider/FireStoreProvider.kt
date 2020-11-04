package ru.stplab.bignote.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import ru.stplab.bignote.data.errors.NoAuthException
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.data.model.User

private const val NOTES_COLLECTION = "notes"
private const val USERS_COLLECTION = "users"

class FireStoreProvider(private val db: FirebaseFirestore, private val auth: FirebaseAuth) :
    DataProvider {

    private val notesReference
        get() = currentUser?.let {
            db.collection(USERS_COLLECTION).document(it.uid).collection(NOTES_COLLECTION)
        } ?: throw NoAuthException()

    private val currentUser
        get() = auth.currentUser

    override fun subscribeToAllNotes(): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.addSnapshotListener { snapshot, error ->
                value = error?.let {
                    NoteResult.Error(it)
                } ?: snapshot?.let { querySnapshot ->
                    val notes = querySnapshot.documents.map { it.toObject(Note::class.java) }
                    NoteResult.Success(notes)
                }
            }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun getNoteById(id: String): LiveData<NoteResult> =
        MutableLiveData<NoteResult>().apply {
            try {
                notesReference.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        value = NoteResult.Success((snapshot.toObject(Note::class.java)) as Note)
                    }
                    .addOnFailureListener {
                        value = NoteResult.Error(it)
                    }
            } catch (e: Throwable) {
                value = NoteResult.Error(e)
            }
        }

    override fun saveNote(note: Note): LiveData<NoteResult> = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.document(note.id)
                .set(note).addOnSuccessListener {
                    value = NoteResult.Success(note)
                }
                .addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun deleteNote(id: String) = MutableLiveData<NoteResult>().apply {
        try {
            notesReference.document(id).delete()
                .addOnSuccessListener {
                    value = NoteResult.Success(null)
                }.addOnFailureListener {
                    value = NoteResult.Error(it)
                }
        } catch (e: Throwable) {
            value = NoteResult.Error(e)
        }
    }

    override fun getCurrentUser() = MutableLiveData<User?>().apply {
        value = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
    }
}