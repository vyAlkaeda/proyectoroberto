package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EpicarditisDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_detail)
    }
}

class EpicarditisDetailActivity : BaseSymptomDetailActivity() {
    override val title = "EPICARDITIS FIBRINO PURULENTA"
    override val description = "La epicarditis fibrino purulenta es una inflamación del pericardio con presencia de fibrina y pus"
    override val type = "LESIÓN"
} 