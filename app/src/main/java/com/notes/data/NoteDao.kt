package com.notes.data

import androidx.room.*

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes")
    fun getAll(): List<NoteDbo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg notes: NoteDbo)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Long): NoteDbo

    @Query("DELETE FROM notes WHERE id = :id")
    fun deleteById(id: Long)

}