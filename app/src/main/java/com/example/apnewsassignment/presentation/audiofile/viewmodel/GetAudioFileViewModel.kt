package com.example.apnewsassignment.presentation.audiofile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.AudioFilesDTO
import com.example.apnewsassignment.domain.use_cases.GetAudioFileUseCase
import com.example.apnewsassignment.presentation.audiofile.states.GetAudioFileState
import com.example.apnewsassignment.util.ResponseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetAudioFileViewModel @Inject constructor(
    private val getAudioFileUseCase: GetAudioFileUseCase
) : ViewModel() {

    private val _getAudioFileState = MutableStateFlow(GetAudioFileState())
    val getAudioFileState: StateFlow<GetAudioFileState> = _getAudioFileState

    fun getAudioFiles() {
        viewModelScope.launch {
            getAudioFileUseCase().collect { it ->
                when (it) {
                    is ResponseState.Success<*> -> {
                        val getFiles = it.data as? AudioFilesDTO
                        getFiles?.let {audioFilesDTONonNull ->
                            _getAudioFileState.value = GetAudioFileState(audioFile = audioFilesDTONonNull)
                        }
                    }
                    is ResponseState.Error<*> -> {
                        val errorMessage = it.message?.toString() ?: "Unknown Error"
                        _getAudioFileState.value = GetAudioFileState(error = errorMessage)
                    }
                    is ResponseState.Loading<*> -> {
                        _getAudioFileState.value = GetAudioFileState(isLoading = true)
                    }
                }
            }
        }
    }
}