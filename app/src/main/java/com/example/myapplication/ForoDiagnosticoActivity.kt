package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ForoDiagnosticoActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ForumAdapter
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foro_diagnostico)

        setupRecyclerView()
        loadQuestions()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewForum)
        adapter = ForumAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadQuestions() {
        db.collection("forum_questions")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
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