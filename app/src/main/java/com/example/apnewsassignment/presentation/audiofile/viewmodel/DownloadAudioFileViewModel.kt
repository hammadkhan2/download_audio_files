package com.example.apnewsassignment.presentation.audiofile.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnewsassignment.domain.use_cases.DownloadAudioFileUseCase
import com.example.apnewsassignment.presentation.audiofile.states.DownloadAudioFileState
import com.example.apnewsassignment.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadAudioFileViewModel @Inject constructor(
    private val downloadAudioFileUseCase: DownloadAudioFileUseCase
) : ViewModel() {

    private val _downloadAudioFileState =
        MutableStateFlow<DownloadAudioFileState>(DownloadAudioFileState())
    val downloadAudioFileState: StateFlow<DownloadAudioFileState> = _downloadAudioFileState

    fun downloadAudioFiles(context: Context, audioUrl: String, name: String) {
        viewModelScope.launch {
            downloadAudioFileUseCase(context, audioUrl, name).collect { it ->
                when (it) {
                    is ResponseState.Success<*> -> {
                        val filePath = it.data as? String
                        filePath?.let { path ->
                            _downloadAudioFileState.value = DownloadAudioFileState(filePath = path)
                        }
                    }

                    is ResponseState.Error<*> -> {
                        val errorMessage = it.message?.toString() ?: "Unknown Error"
                        _downloadAudioFileState.value = DownloadAudioFileState(error = errorMessage)
                    }

                    is ResponseState.Loading<*> -> {
                        _downloadAudioFileState.value = DownloadAudioFileState(isLoading = true)
                    }
                }
            }
        }
    }
}