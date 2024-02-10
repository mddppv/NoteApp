package com.example.noteapp.ui.note

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NoteFragment : Fragment() {

    private lateinit var binding: FragmentNoteBinding
    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteBinding.inflate(inflater, container, false)

        initRecyclerView()

        binding.btnCreate.setOnClickListener {
            findNavController().navigate(R.id.action_noteFragment_to_createFragment)
        }

        return binding.root
    }

    private fun initRecyclerView() {
        adapter = NoteAdapter(
            onNoteClickListener = { note ->
                findNavController().navigate(R.id.action_noteFragment_to_createFragment)
            },
            onNoteLongClickListener = { note ->
                showDialogToDelete(note)
            }
        )
        binding.rvNotes.adapter = adapter
        binding.rvNotes.layoutManager = LinearLayoutManager(requireContext())

        App.database.noteDao().getAllNoteModels()
            .observe(viewLifecycleOwner) { notes -> adapter.submitList(notes) }
    }

    private fun showDialogToDelete(note: NoteModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Note")
            .setMessage("Are you sure to delete this note?")
            .setPositiveButton("Yes") { dialog, _ ->
                deleteNoteInBackground(note)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteNoteInBackground(note: NoteModel) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                App.database.noteDao().delete(note)
            }
        }
    }
}