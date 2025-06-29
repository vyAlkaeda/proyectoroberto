package com.example.myapplication.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

fun cargarEnfermedadesDesdeJson(context: Context): List<Enfermedad> {
    val json = context.assets.open("enfermedades.json").bufferedReader().use { it.readText() }
    val jsonObject = JSONObject(json)
    val enfermedadesArray = jsonObject.getJSONArray("enfermedades")
    val gson = Gson()
    val type = object : TypeToken<List<Enfermedad>>() {}.type
    return gson.fromJson(enfermedadesArray.toString(), type)
} 