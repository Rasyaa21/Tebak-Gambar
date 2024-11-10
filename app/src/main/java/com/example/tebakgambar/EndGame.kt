package com.example.tebakgambar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tebakgambar.databinding.ActivityEndGameBinding

class EndGame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityEndGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getStringExtra("score")

        val scoreText = if (score!=null){
            "Score : $score"
        } else {
            "Score not available"
        }

        binding.tvScore.text = scoreText

        binding.btBackButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}