package com.aurafx.player

import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private var equalizer: Equalizer? = null
    private var bassBoost: BassBoost? = null
    private var reverb: PresetReverb? = null
    private var isPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPlay = findViewById<ImageButton>(R.id.btnPlay)
        val trackName = findViewById<TextView>(R.id.trackName)
        val speedSlider = findViewById<SeekBar>(R.id.speedSlider)

        btnPlay.setOnClickListener {
            if (mediaPlayer == null) {
                // For demonstration, we would load an actual URI here
                // mediaPlayer = MediaPlayer.create(this, R.raw.sample_track)
                // setupAudioEffects()
            }
            if (isPlaying) {
                mediaPlayer?.pause()
                isPlaying = false
                btnPlay.setImageResource(android.R.drawable.ic_media_play)
                trackName.text = "Пауза"
            } else {
                mediaPlayer?.start()
                isPlaying = true
                btnPlay.setImageResource(android.R.drawable.ic_media_pause)
                trackName.text = "Загрузите музыку для проигрывания"
            }
        }
        
        speedSlider.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    val speed = progress / 100f
                    mediaPlayer?.let {
                        if (it.isPlaying) {
                            val params = it.playbackParams
                            params.speed = speed
                            it.playbackParams = params
                        }
                    }
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupAudioEffects() {
        mediaPlayer?.let {
            val sessionId = it.audioSessionId
            equalizer = Equalizer(0, sessionId)
            equalizer?.enabled = true

            bassBoost = BassBoost(0, sessionId)
            bassBoost?.enabled = true
            bassBoost?.setStrength(500) // 50%

            reverb = PresetReverb(0, sessionId)
            reverb?.preset = PresetReverb.PRESET_LARGEHALL
            reverb?.enabled = true
            
            it.attachAuxEffect(reverb!!.id)
            it.setAuxEffectSendLevel(1.0f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        equalizer?.release()
        bassBoost?.release()
        reverb?.release()
        mediaPlayer?.release()
    }
}
