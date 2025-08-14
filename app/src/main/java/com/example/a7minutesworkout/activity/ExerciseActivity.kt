package com.example.a7minutesworkout.activity

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.viewModels
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.*
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import com.example.a7minutesworkout.R
import com.example.a7minutesworkout.WorkOutApp
import com.example.a7minutesworkout.ui.adapter.ExerciseStatusAdapter
import com.example.a7minutesworkout.ui.event.ExerciseUIEvent
import com.example.a7minutesworkout.utils.URIConstants
import com.example.a7minutesworkout.viewmodel.ExerciseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private val viewModel by viewModels<ExerciseViewModel>()

    private var binding : ActivityExerciseBinding? = null


    private var tts : TextToSpeech? = null
    private var player : MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as WorkOutApp).apply {
            appComponent.inject(this@ExerciseActivity)
            appComponent.run {
                inject(viewModel)
            }
        }

        binding = ActivityExerciseBinding.inflate(layoutInflater)
        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this@ExerciseActivity
        lifecycle.addObserver(viewModel)
        lifecycleScope.launch (block = uiEventsBlock)

        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        tts = TextToSpeech(this, this)
        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }

        exerciseAdapter = ExerciseStatusAdapter()
        binding?.rvExerciseStatus?.adapter = exerciseAdapter

        viewModel.fetchExerciseList()
        setupObservers()
        setupRestView()
    }

    private fun setupObservers() {
        binding?.lifecycleOwner?.let { lifecycleOwner ->
            viewModel.exerciseList.observe(lifecycleOwner){ exerciseList ->
                exerciseList?.let { exerciseAdapter?.updateItems(it) }
            }
        }
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

    private val uiEventsBlock: suspend CoroutineScope.() -> Unit = {
        viewModel.uiEventBus.events.collect { event ->
            when(event) {
                ExerciseUIEvent.FinishExercise -> {
                    finishActivity()
                    Log.d("Taga", "Activity finished")
                }
            }
        }
    }

    private fun finishActivity() {
        finish()
        val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
        startActivity(intent)
    }

    private fun setupRestView(){

        try {
            val soundURI =
                (URIConstants.RESOURCE_URI + R.raw.press_start).toUri()
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()

        } catch(e : Exception){
            e.printStackTrace()
        }
        viewModel.startResting()
    }

//    private fun setupActivityView(){
//
//        speakOut(currentExercise.getName())
//    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
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
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}