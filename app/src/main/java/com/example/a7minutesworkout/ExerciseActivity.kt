package com.example.a7minutesworkout

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.net.toUri
import com.example.a7minutesworkout.viewmodel.ExerciseViewModel
import javax.inject.Inject

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    @Inject lateinit var viewModel: ExerciseViewModel

    private var binding : ActivityExerciseBinding? = null

    private var restTimer : CountDownTimer? = null

    private var activityTimer : CountDownTimer? = null
    private var activityProgress = 0

    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WorkOutApp).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        binding?.viewModel = viewModel

        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    private fun customDialogForBackButton(){
        val customDialog = Dialog(this@ExerciseActivity)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)

        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }
        customDialog.show()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        customDialogForBackButton()
    }

    private fun setupExerciseStatusRecyclerView(){
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = 0
        restTimer = object: CountDownTimer(viewModel.restingTime * 1000L, viewModel.timeInterval * 1000L){
            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = (millisUntilFinished/1000).toInt()
                binding?.progressBar?.progress = viewModel.restingTime - secondsUntilFinished
                binding?.tvTimer?.text = secondsUntilFinished.toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupActivityView()
            }

        }.start()
    }

    private fun setActivityProgressBar(){
        binding?.exerciseItem?.progressBarActivity?.progress = 0
        activityTimer = object: CountDownTimer(viewModel.exerciseTimerDuration * 1000L, viewModel.timeInterval * 1000L){
            override fun onTick(millisUntilFinished: Long) {
                val secondsUntilFinished = (millisUntilFinished/1000).toInt()
                binding?.exerciseItem?.progressBarActivity?.progress = viewModel.exerciseTimerDuration - secondsUntilFinished
                binding?.exerciseItem?.tvTimerActivity?.text = secondsUntilFinished.toString()
            }

            override fun onFinish() {
                exerciseList!![currentExercisePosition].setIsSelected(false)
                exerciseList!![currentExercisePosition].setIsCompleted(true)
                exerciseAdapter!!.notifyDataSetChanged()

                if(currentExercisePosition < exerciseList?.size!! - 1) {
                    setupRestView()
                } else {
                    finish()
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    startActivity(intent)
                }
            }

        }.start()
    }

    private fun setupRestView(){
        val nextExercise = exerciseList!![currentExercisePosition + 1]

        try {
            val soundURI =
                ("android.resource://com.example.a7minutesworkout/" + R.raw.press_start).toUri()
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()

        } catch(e : Exception){
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.exerciseItem?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.exerciseItem?.flExerciseView?.visibility = View.INVISIBLE
        binding?.exerciseItem?.ivImage?.visibility = View.INVISIBLE
        binding?.llUpcomingExercise?.visibility = View.VISIBLE
        binding?.tvNextExerciseName?.text = nextExercise.getName()
        if(restTimer != null){
            restTimer?.cancel()
        }
        setRestProgressBar()
    }

    private fun setupActivityView(){
        val currentExercise = exerciseList!![currentExercisePosition]
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.exerciseItem?.tvExerciseName?.visibility = View.VISIBLE
        binding?.exerciseItem?.flExerciseView?.visibility = View.VISIBLE
        binding?.exerciseItem?.ivImage?.visibility = View.VISIBLE
        binding?.llUpcomingExercise?.visibility = View.INVISIBLE

        if(activityTimer != null){
            activityTimer?.cancel()
            activityProgress = 0
        }

        speakOut(currentExercise.getName())

        binding?.exerciseItem?.ivImage?.setImageResource(currentExercise.getImage())
        binding?.exerciseItem?.tvExerciseName?.text = currentExercise.getName()

        setActivityProgressBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        if(restTimer != null){
            restTimer?.cancel()
        }
        if(activityTimer != null){
            activityTimer?.cancel()
            activityProgress = 0
        }
        if(tts != null) {
            tts?.stop()
            tts?.shutdown()
        }

        if(player != null) {
            player?.stop()
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported")
            }
        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }
    private fun speakOut(text: String){
        tts?.let {
            it.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
        }
    }
}