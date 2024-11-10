package com.example.tebakgambar


import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    // api/questions merupakan endpoint yang akan digunakan untuk emngambil data pertanyaan
    @GET("api/questions")
    fun getQuestions(): Call<List<Question>>
}