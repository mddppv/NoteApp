package com.example.noteapp

import android.app.Application
import androidx.room.Room
import com.example.noteapp.db.NoteDatabase

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java, "note-db"
        ).build()
    }

    companion object {
        lateinit var database: NoteDatabase
    }
}