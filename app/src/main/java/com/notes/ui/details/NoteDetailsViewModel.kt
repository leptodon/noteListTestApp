package com.notes.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class NoteDetailsViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {

    private val _note = MutableLiveData<NoteDbo>()
    val note: LiveData<NoteDbo> = _note

    fun getNote(id:Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _note.postValue(noteDatabase.noteDao().getNoteById(id))
        }
    }

    fun saveNote(title: String, content: String) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().insertAll(
                NoteDbo(
                    title = title,
                    content = content,
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now()
                )
            )
        }
    }

    fun updateNote(noteDbo: NoteDbo) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().insertAll(
                noteDbo
            )
        }
    }

    fun deleteNote(it: NoteDbo) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().deleteById(
                it.id
            )
        }
    }
}