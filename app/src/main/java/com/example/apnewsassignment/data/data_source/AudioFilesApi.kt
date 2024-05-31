package com.example.apnewsassignment.data.data_source

import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.AudioFilesDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface AudioFilesApi {

    @GET("tracks/")
    suspend fun searchTracks(
        @Query("client_id") clientId: String,
        @Query("format") format: String = "json",
        @Query("limit") limit: Int = 10,
        @Query("artist_name") tags: String = "we+are+fm"
    ): AudioFilesDTO
}