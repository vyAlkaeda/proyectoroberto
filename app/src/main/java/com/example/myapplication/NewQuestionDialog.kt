package com.example.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class NewQuestionDialog : DialogFragment() {
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var submitButton: Button
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_new_question, container, false)
        
        titleEditText = view.findViewById(R.id.titleEditText)
        contentEditText = view.findViewById(R.id.contentEditText)
        submitButton = view.findViewById(R.id.submitButton)

        submitButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val question = ForumQuestion(
                    title = title,
                    content = content,
                    timestamp = Timestamp.now(),
                    author = "Usuario" // Aquí podrías obtener el usuario actual si tienes autenticación
                )

                db.collection("forum_questions")
                    .add(question)
                    .addOnSuccessListener {
                        dismiss()
                    }
                    .addOnFailureListener {
                        // Manejar el error
                    }
            }
        }

        return view
    }
} 