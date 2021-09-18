package com.raywenderlich.AdityaKumar.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    internal lateinit var gameScoreTextView: TextView
    internal var gameScore: Int = 0
    internal lateinit var timeRemainingTextView: TextView
    internal lateinit var tapMeButton: Button
    internal val time:Long = 60 * 1000 // 60 seconds
    internal val internal:Long = 1000 // 1 second
    internal lateinit var timer: CountDownTimer
    internal var gameStarted: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        gameScoreTextView.text = getString(R.string.gameScore, gameScore)
        timeRemainingTextView = findViewById(R.id.timeRemainingTextView)
        resetGame()
        tapMeButton.setOnClickListener { view ->
            incrementScore()
        }
    }

    private fun resetGame(){
        gameScore = 0
        timeRemainingTextView.text = getString(R.string.timeRemaining, time/1000)
        gameScoreTextView.text = getString(R.string.gameScore, gameScore)
        timer = object : CountDownTimer(time, internal){
            override fun onTick(p0: Long) {
                if (p0 / 1000 > 1){
                    timeRemainingTextView.text = getString(R.string.timeRemaining, p0 / 1000)
                }
                else{
                    timeRemainingTextView.text = getString(R.string.timeRemainingOther, p0 / 1000)
                }
            }

            override fun onFinish() {
                endGame()
            }
        }
        gameStarted = false
    }

    fun incrementScore(){
        if (gameStarted == false){ startGame() }

        gameScore++
        val newScore = getString(R.string.gameScore, gameScore)
        gameScoreTextView.text = newScore
    }

    private fun startGame(){
        timer.start()
        gameStarted = true
    }

    private fun endGame(){
        Toast.makeText(this, getString(R.string.gameOverMsg, gameScore), Toast.LENGTH_LONG).show()
        gameStarted = false
    }
}