package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NewQuestionDialog : DialogFragment() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_new_question, null)
        val titleEditText = view.findViewById<TextInputEditText>(R.id.editTextTitle)
        val descriptionEditText = view.findViewById<TextInputEditText>(R.id.editTextDescription)

        return AlertDialog.Builder(requireContext())
            .setTitle("Nueva Pregunta")
            .setView(view)
            .setPositiveButton("Publicar") { _, _ ->
                val title = titleEditText.text.toString()
                val description = descriptionEditText.text.toString()
                
                if (title.isNotEmpty() && description.isNotEmpty()) {
                    val user = auth.currentUser
                    if (user != null) {
                        val question = ForumQuestion(
                            title = title,
                            description = description,
                            userId = user.uid,
                            userName = user.displayName ?: "Usuario"
                        )
                        
                        db.collection("forum_questions")
                            .add(question)
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
} 