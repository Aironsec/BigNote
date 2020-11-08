package ru.stplab.bignote.data.provider

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import ru.stplab.bignote.data.errors.NoAuthException
import ru.stplab.bignote.data.model.Note
import ru.stplab.bignote.data.model.NoteResult
import ru.stplab.bignote.data.model.User
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    @ExperimentalCoroutinesApi
    override fun subscribeToAllNotes(): ReceiveChannel<NoteResult> =
        Channel<NoteResult>(Channel.CONFLATED).apply {
            var registration: ListenerRegistration? = null
            try {
                registration = notesReference.addSnapshotListener { snapshot, error ->
                    val value = error?.let {
                        NoteResult.Error(it)
                    } ?: snapshot?.let { querySnapshot ->
                        val notes = querySnapshot.documents.map { it.toObject(Note::class.java) }
                        NoteResult.Success(notes)
                    }
                    value?.let { offer(it) }
                }
            } catch (e: Throwable) {
                offer(NoteResult.Error(e))
            }

            invokeOnClose { registration?.remove() }
        }

    override suspend fun getNoteById(id: String): Note =
        suspendCoroutine { continuation ->
            try {
                notesReference.document(id).get()
                    .addOnSuccessListener { snapshot ->
                        val note = snapshot.toObject(Note::class.java) as Note
                        continuation.resume(note)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            } catch (e: Throwable) {
                continuation.resumeWithException(e)
            }
        }

    override suspend fun saveNote(note: Note): Note = suspendCoroutine { continuation ->
        try {
            notesReference.document(note.id)
                .set(note).addOnSuccessListener {
                    continuation.resume(note)
                }
                .addOnFailureListener {
                    continuation.resumeWithException(it)
                }
        } catch (e: Throwable) {
            continuation.resumeWithException(e)
        }
    }

    override suspend fun deleteNote(id: String): Unit = suspendCoroutine { continution ->
        try {
            notesReference.document(id).delete()
                .addOnSuccessListener {
                    continution.resume(Unit)
                }.addOnFailureListener {
                    continution.resumeWithException(it)
                }
        } catch (e: Throwable) {
            continution.resumeWithException(e)
        }
    }

    override suspend fun getCurrentUser(): User? = suspendCoroutine { continuation ->
        val user = currentUser?.let {
            User(it.displayName ?: "", it.email ?: "")
        }
        continuation.resume(user)
    }
}