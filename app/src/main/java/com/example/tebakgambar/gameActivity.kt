package com.example.tebakgambar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.withStyledAttributes
import com.bumptech.glide.Glide
import com.example.tebakgambar.databinding.ActivityGameBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class gameActivity : AppCompatActivity() {

    //lateinit var adalah variabel yang tidak akan di inisialisasi sampe variabel itu dipanggil

    private lateinit var questionImageView: ImageView // Declaring a lateinit variable to hold an ImageView reference for displaying a question image.
    private lateinit var answerEditText: EditText // Declaring a lateinit variable to hold an EditText reference for user input.
    private lateinit var submitButton: Button // Declaring a lateinit variable to hold a Button reference for submitting the answer.

    // Declaring a lateinit variable to hold an instance of ApiInterface for making API requests.
    private lateinit var api: ApiInterface
    private var score:Int = 0
    // Declaring a variable to hold a list of Question objects. Initially, it's assigned an empty list.
    private var questionsList: List<Question> = emptyList()

    private var currentIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            questionImageView = binding.ivGambar
            answerEditText = binding.ptInputJawaban
            submitButton = binding.btJawaban
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://tebakgambar.dilesin.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

       api = retrofit.create(ApiInterface::class.java)
        
        loadQuestion()
        
        answerEditText.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let { 
                    val text = it.toString()
                    answerEditText.removeTextChangedListener(this)
                    answerEditText.setText(text.toUpperCase())
                    answerEditText.setSelection(text.length)
                    answerEditText.addTextChangedListener(this)
                }
            }
        })
    }

    private fun loadQuestion() {
        // Display a toast message to inform the user that the image is being loaded
        Toast.makeText(this, "Loading image....", Toast.LENGTH_SHORT).show()

        // Make an asynchronous API call to fetch the list of questions
        api.getQuestions().enqueue(object : Callback<List<Question>> {
            override fun onResponse(
                call: Call<List<Question>>,
                response: Response<List<Question>>
            ) {
                // If the response is successful, update the questions list and display the current question
                questionsList = response.body()!!
                DisplayQuestion(questionsList[currentIndex])
            }

            override fun onFailure(call: Call<List<Question>>, t: Throwable) {
                // Log the error with a tag "gameActivity" and the error message
                Log.e("gameActivity", "Error loading question", t)
            }
        })
    }
    
    private fun DisplayQuestion(question: Question){
        //library untuk menampilkan gambar
        Glide.with(this)
            .load(question.image)
            .into(questionImageView)

        //membuat function ketika button submit di klik
        submitButton.setOnClickListener {
            val answer = answerEditText.text.toString()

            if (answer.equals(question.answer, ignoreCase = true)) {
                Toast.makeText(this, "Jawaban Benar", Toast.LENGTH_SHORT).show()
                answerEditText.setText("")
                score += 100
                currentIndex++

                if (currentIndex < questionsList.size) {
                    DisplayQuestion(questionsList[currentIndex])
                } else {
                    val intent = Intent(this, EndGame::class.java)
                    intent.putExtra("score", score.toString()) //harus di convert sebelum passing data
                    startActivity(intent)
                    Toast.makeText(this, "Selamat Anda Menyelesaikan Semua Pertanyaan. Skor: $score", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else if (answer.isEmpty()) {
                Toast.makeText(this, "Silahkan Masukan Jawaban", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Jawaban Salah Coba Lagi", Toast.LENGTH_SHORT).show()
                answerEditText.setText("")
            }
        }
    }
}