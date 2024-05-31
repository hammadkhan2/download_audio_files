package com.example.apnewsassignment.presentation.audiofile.states

import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.AudioFilesDTO
import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.Headers

data class DownloadAudioFileState(
    val isLoading: Boolean = false,
    val filePath: String = "",
    val error: String = "",
    val progress: Int = 0
)