package com.example.apnewsassignment.domain.repository

import android.content.Context
import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.AudioFilesDTO

interface AudioFilesRepository {
    suspend fun getAudioFiles(): AudioFilesDTO
    suspend fun downloadAudioFile(context: Context, audioUrl: String, name: String): String?
}