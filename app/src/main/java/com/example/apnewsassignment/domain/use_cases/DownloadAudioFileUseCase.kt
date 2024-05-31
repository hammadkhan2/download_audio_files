package com.example.apnewsassignment.domain.use_cases

import android.content.Context
import com.example.apnewsassignment.domain.repository.AudioFilesRepository
import com.example.apnewsassignment.util.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class DownloadAudioFileUseCase @Inject constructor(
    private val audioFilesRepository: AudioFilesRepository
) {
    operator fun invoke(context: Context, audioUrl: String, name: String): Flow<ResponseState<String?>> = flow {
        try {
            emit(ResponseState.Loading<String?>())
            val audioFiles = audioFilesRepository.downloadAudioFile(
                context = context,
                audioUrl = audioUrl,
                name = name
            )
            emit(ResponseState.Success<String?>(audioFiles))
        } catch (e: HttpException) {
            emit(ResponseState.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(ResponseState.Error("Please check your internet connection"))
        }
    }
}