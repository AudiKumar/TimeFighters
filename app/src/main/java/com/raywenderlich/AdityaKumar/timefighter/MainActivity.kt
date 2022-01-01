package com.raywenderlich.AdityaKumar.timefighter

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    internal lateinit var gameScoreTextView: TextView
    internal var gameScore: Int = 0
    internal lateinit var timeRemainingTextView: TextView
    internal lateinit var tapMeButton: Button
    internal val time:Long = 60 * 1000 // 60 seconds
    internal val internal:Long = 1000 // 1 second
    internal var timeLeft: Long = 60000
    internal lateinit var timer: CountDownTimer
    internal var gameStarted: Boolean = false

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG,"onCreate() called. Score is: $gameScore")
        tapMeButton = findViewById(R.id.tapMeButton)
        gameScoreTextView = findViewById(R.id.gameScoreTextView)
        gameScoreTextView.text = getString(R.string.gameScore, gameScore)
        timeRemainingTextView = findViewById(R.id.timeRemainingTextView)
        //resetGame()
        if (savedInstanceState != null){
            gameScore = savedInstanceState.getInt(SCORE_KEY)
            timeLeft = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        }
        else {resetGame()}
        tapMeButton.setOnClickListener { view ->
            val bounceAnim = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnim)
            incrementScore()
            val blinkAnim = AnimationUtils.loadAnimation(this, R.anim.blink)
            gameScoreTextView.startAnimation(blinkAnim)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionAbout){
            showInfo()
        }
        return true
    }

    private fun showInfo(){
        val dialogTitle = getString(R.string.aboutTitle)
        val dialogMsg = getString(R.string.aboutMsg)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMsg)
        builder.create().show()

    }

    private fun restoreGame() {
        gameScoreTextView.text = getString(R.string.gameScore, gameScore)

        val restoredTime = timeLeft / 1000
        timeRemainingTextView.text = getString(R.string.timeRemaining, restoredTime)

        timer = object : CountDownTimer(timeLeft, internal){
            override fun onTick(p0: Long) {
                val secondsLeft = p0 / 1000
                timeLeft = p0
                if (secondsLeft > 1){ timeRemainingTextView.text = getString(R.string.timeRemaining, p0 / 1000) }
                else{ timeRemainingTextView.text = getString(R.string.timeRemainingOther, p0 / 1000) }
            }

            override fun onFinish() {
                endGame()
            }
        }
        timer.start()
        gameStarted = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SCORE_KEY, gameScore)
        outState.putLong(TIME_LEFT_KEY, timeLeft)
        timer.cancel()
        Log.d(TAG, "onSaveInstanceState: Saving Score: $gameScore & Time Left: $timeLeft")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called.")
    }

    private fun resetGame(){
        gameScore = 0
        timeRemainingTextView.text = getString(R.string.timeRemaining, time/1000)
        gameScoreTextView.text = getString(R.string.gameScore, gameScore)
        timer = object : CountDownTimer(time, internal){
            override fun onTick(p0: Long) {
                val secondsLeft = p0 / 1000
                timeLeft = p0
                if (secondsLeft > 1){ timeRemainingTextView.text = getString(R.string.timeRemaining, p0 / 1000) }
                else{ timeRemainingTextView.text = getString(R.string.timeRemainingOther, p0 / 1000) }
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