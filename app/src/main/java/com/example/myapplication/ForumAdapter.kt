package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class ForumAdapter : ListAdapter<ForumQuestion, ForumAdapter.QuestionViewHolder>(QuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = getItem(position)
        holder.bind(question)
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)

        fun bind(question: ForumQuestion) {
            titleTextView.text = question.title
            contentTextView.text = question.content
            authorTextView.text = question.author
            dateTextView.text = formatDate(question.timestamp)
        }

        private fun formatDate(timestamp: Timestamp): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(timestamp.toDate())
        }
    }

    class QuestionDiffCallback : DiffUtil.ItemCallback<ForumQuestion>() {
        override fun areItemsTheSame(oldItem: ForumQuestion, newItem: ForumQuestion): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ForumQuestion, newItem: ForumQuestion): Boolean {
            return oldItem == newItem
        }
    }
} 