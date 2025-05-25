package com.Vali98.ChatterUI

import android.media.*
import com.facebook.react.bridge.*
import com.Vali98.ChatterUI.utils.*

class CustomTTSModule(private val reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext), LifecycleEventListener {

    private var audioTrack: AudioTrack? = null

    override fun getName() = "CustomTTS"

    @ReactMethod
    fun initTTS(promise: Promise) {
        try {
            OnnxRuntimeManager.initialize(reactContext)
            promise.resolve("Initialized")
        } catch (e: Exception) {
            promise.reject("TTS_INIT_FAILED", e)
        }
    }

    @ReactMethod
    fun speakText(text: String, voice: String, speed: Double, promise: Promise) {
        try {
            val session = OnnxRuntimeManager.getSession()

            // ✅ Instantiate phonemizer properly
            val converter = PhonemeConverter(reactContext)
            val phonemes = converter.phonemize(text)

            val (audioData, sampleRate) = createAudio(
                phonemes = phonemes,
                voice = voice,
                speed = speed.toFloat(),
                session = session,
                context = reactContext
            )

            playAudio(audioData, sampleRate)
            promise.resolve("Playback complete")
        } catch (e: Exception) {
            promise.reject("TTS_SPEAK_TEXT_ERROR", e)
        }
    }

    @ReactMethod
    fun stop() {
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
    }

    private fun playAudio(audioData: FloatArray, sampleRate: Int) {
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_FLOAT
        )

        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_FLOAT,
            bufferSize,
            AudioTrack.MODE_STREAM
        )

        audioTrack?.play()
        audioTrack?.write(audioData, 0, audioData.size, AudioTrack.WRITE_BLOCKING)
    }

    override fun onHostDestroy() {
        stop()
    }

    override fun onHostPause() {}
    override fun onHostResume() {}

    init {
        reactContext.addLifecycleEventListener(this)
    }
}
