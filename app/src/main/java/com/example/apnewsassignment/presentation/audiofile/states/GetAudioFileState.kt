package com.example.apnewsassignment.presentation.audiofile.states

import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.AudioFilesDTO
import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.Headers

data class GetAudioFileState(
    val isLoading: Boolean = false,
    val audioFile: AudioFilesDTO = AudioFilesDTO(Headers(), emptyList()),
    val error: String = ""
)