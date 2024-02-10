package com.example.noteapp.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noteapp.model.NoteModel

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(noteModel: NoteModel)

    @Query("SELECT * FROM notes")
    fun getAllNoteModels(): LiveData<List<NoteModel>>

    @Delete
    suspend fun delete(noteModel: NoteModel)
}