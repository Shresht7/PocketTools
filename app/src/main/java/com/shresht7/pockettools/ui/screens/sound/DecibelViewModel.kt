package com.shresht7.pockettools.ui.screens.sound

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("MissingPermission") // Permission check is handled in the UI layer
class DecibelViewModel(): ViewModel() {
    private val _state = MutableStateFlow(DecibelState())
    val state = _state.asStateFlow()

    private var recorder: AudioRecord? = null
    private var isRecording = false

    // Configuration for the audio recorder
    private val audioSource = MediaRecorder.AudioSource.MIC
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_IN_MONO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)
    private val buffer = ShortArray(bufferSize)

    /**
     * Starts the audio recording process.
     *
     * This function initializes and starts an [AudioRecord] instance if it's not already recording.
     * It then launches a coroutine in the I/O dispatcher to continuously read audio data from the
     * microphone. The maximum amplitude from each buffer is calculated and used to update the
     * [DecibelState.amplitude] state flow, which can be observed by the UI to display real-time
     * sound levels.
     *
     * The function will do nothing if a recording is already in progress.
     *
     * @see stopRecording
     * @see AudioRecord
     */
    fun startRecording() {
        if (isRecording) return

        // Initialize the recorder
        recorder = AudioRecord(audioSource, sampleRate, channelConfig, audioFormat, bufferSize)

        // Start recording and launch a coroutine to process the audio stream
        recorder?.startRecording()
        isRecording = true

        viewModelScope.launch(Dispatchers.IO) {
            while (isRecording) {
                val readSize = recorder?.read(buffer, 0, buffer.size) ?: 0
                if (readSize > 0) {
                    // Find the max amplitude in the buffer
                    val maxAmplitude = buffer.maxOfOrNull { abs(it.toInt()) } ?: 0
                    _state.update { it.copy(amplitude = maxAmplitude) }
                }
            }
        }
    }

    /**
     * Stops the audio recording process.
     *
     * This function checks if a recording is currently in progress. If so, it stops the
     * [AudioRecord] instance, releases its resources, and sets the recorder to null to
     * free up memory. It also sets the `isRecording` flag to false. If no recording is
     * active, the function does nothing.
     */
    fun stopRecording() {
        if (!isRecording) return

        isRecording = false
        recorder?.stop()
        recorder?.release()
        recorder = null
    }

    // Cleanup the recorder when the ViewModel is destroyed
    override fun onCleared() {
        super.onCleared()
        stopRecording()
    }
}