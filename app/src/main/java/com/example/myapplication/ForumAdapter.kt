package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ForumAdapter : ListAdapter<ForumQuestion, ForumAdapter.QuestionViewHolder>(QuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_forum_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val userNameTextView: TextView = itemView.findViewById(R.id.textViewUserName)
        private val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        private val answersCountTextView: TextView = itemView.findViewById(R.id.textViewAnswersCount)

        fun bind(question: ForumQuestion) {
            titleTextView.text = question.title
            descriptionTextView.text = question.description
            userNameTextView.text = question.userName
            
            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            dateTextView.text = dateFormat.format(question.timestamp.toDate())
            
            answersCountTextView.text = "${question.answers.size} respuestas"
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