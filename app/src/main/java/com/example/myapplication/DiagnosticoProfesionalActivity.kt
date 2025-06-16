package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DiagnosticoProfesionalActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ForumAdapter
    private lateinit var fabNewQuestion: FloatingActionButton
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diagnostico_profesional)

        recyclerView = findViewById(R.id.recyclerViewForum)
        fabNewQuestion = findViewById(R.id.fabNewQuestion)

        setupRecyclerView()
        setupFab()
        loadQuestions()
    }

    private fun setupRecyclerView() {
        adapter = ForumAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun setupFab() {
        fabNewQuestion.setOnClickListener {
            // Mostrar diálogo para nueva pregunta
            NewQuestionDialog().show(supportFragmentManager, "newQuestion")
        }
    }

    private fun loadQuestions() {
        db.collection("forum_questions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }

                val questions = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(ForumQuestion::class.java)?.apply {
                        id = doc.id
                    }
                } ?: emptyList()

                adapter.submitList(questions)
            }
    }
} 