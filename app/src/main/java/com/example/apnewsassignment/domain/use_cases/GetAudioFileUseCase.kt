package com.example.apnewsassignment.domain.use_cases

import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.AudioFilesDTO
import com.example.apnewsassignment.domain.repository.AudioFilesRepository
import com.example.apnewsassignment.util.ResponseState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetAudioFileUseCase @Inject constructor(
    private val audioFilesRepository: AudioFilesRepository
) {
    operator fun invoke(): Flow<ResponseState<AudioFilesDTO>> = flow {
        try {
            emit(ResponseState.Loading<AudioFilesDTO>())
            val audioFiles = audioFilesRepository.getAudioFiles()
            emit(ResponseState.Success<AudioFilesDTO>(audioFiles))
        } catch (e: HttpException) {
            emit(ResponseState.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(ResponseState.Error("Please check your internet connection"))
        }
    }
}