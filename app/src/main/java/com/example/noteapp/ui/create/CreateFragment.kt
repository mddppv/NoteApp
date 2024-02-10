package com.example.noteapp.ui.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.noteapp.App
import com.example.noteapp.R
import com.example.noteapp.databinding.FragmentCreateBinding
import com.example.noteapp.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateFragment : Fragment() {

    private lateinit var binding: FragmentCreateBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateBinding.inflate(inflater, container, false)
        initClicker()
        return binding.root
    }

    private fun initClicker() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etDescription.text.toString().trim()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = NoteModel(title = title, description = content)
                saveNoteToDatabase(note)
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNoteToDatabase(note: NoteModel) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                App.database.noteDao().insert(note)
            }
            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.action_createFragment_to_noteFragment)
                Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()
                binding.etTitle.text.clear()
                binding.etDescription.text.clear()
            }
        }
    }
}