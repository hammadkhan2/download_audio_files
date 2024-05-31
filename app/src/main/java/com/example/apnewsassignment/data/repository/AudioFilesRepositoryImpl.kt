package com.example.apnewsassignment.data.repository

import android.content.Context
import android.os.Environment
import com.example.apnewsassignment.data.data_source.AudioFilesApi
import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.AudioFilesDTO
import com.example.apnewsassignment.domain.repository.AudioFilesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import javax.inject.Inject
import kotlin.random.Random

class AudioFilesRepositoryImpl @Inject constructor(
    private val audioFilesApi: AudioFilesApi
) : AudioFilesRepository {

    override suspend fun getAudioFiles(): AudioFilesDTO {
        val clientId = "48250a2a"
        return audioFilesApi.searchTracks(clientId)
    }

    override suspend fun downloadAudioFile(context: Context, audioUrl: String, name: String) = withContext(Dispatchers.IO) {
        try {
            val url = URL(audioUrl)
            val connection = url.openConnection()
            connection.connect()

            val inputStream = connection.getInputStream()
            val audioFile = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$name.mp3")
            val outputStream = FileOutputStream(audioFile)

            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }

            outputStream.close()
            inputStream.close()

            audioFile.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}