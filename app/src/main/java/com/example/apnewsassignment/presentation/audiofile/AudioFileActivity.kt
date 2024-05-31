package com.example.apnewsassignment.presentation.audiofile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.apnewsassignment.R
import com.example.apnewsassignment.data.data_source.dto.audiofilesdto.Result
import com.example.apnewsassignment.databinding.ActivityAudioFileBinding
import com.example.apnewsassignment.presentation.audiofile.adapters.TrackAdapter
import com.example.apnewsassignment.presentation.audiofile.viewmodel.DownloadAudioFileViewModel
import com.example.apnewsassignment.presentation.audiofile.viewmodel.GetAudioFileViewModel
import com.example.apnewsassignment.util.Constants.AUDIO_URL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AudioFileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioFileBinding
    private val getAudioFileViewModel: GetAudioFileViewModel by viewModels()
    private val downloadAudioFileViewModel: DownloadAudioFileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAudioFileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeGetAudioFileViewModel()
    }

    private fun observeGetAudioFileViewModel() {
        CoroutineScope(Dispatchers.Main).launch {
            getAudioFileViewModel.getAudioFileState.collectLatest { audioFileValue ->
                if (audioFileValue.isLoading) {
                    runOnUiThread {
                        binding.mainContentGroup.visibility = View.GONE
                        binding.progressCircular.visibility = View.VISIBLE
                    }
                } else if (audioFileValue.error.isNotBlank()) {
                    runOnUiThread {
                        binding.apply {
                            progressCircular.visibility = View.GONE
                            errorTextView.text = audioFileValue.error
                            errorContentGroup.visibility = View.VISIBLE
                            retryButton.setOnClickListener {
                                progressCircular.visibility = View.VISIBLE
                                errorContentGroup.visibility = View.GONE
                                getAudioFileViewModel.getAudioFiles()
                            }
                        }
                    }
                } else {
                    runOnUiThread {
                        if (audioFileValue.audioFile.results.isNotEmpty()) {
                            setUI(
                                audioFileValue.audioFile.results[0].image,
                                audioFileValue.audioFile.results
                            )
                            binding.mainContentGroup.visibility = View.VISIBLE
                            binding.progressCircular.visibility = View.GONE
                        }
                    }
                }
            }
        }
        getAudioFileViewModel.getAudioFiles()
    }

    private fun setUI(imageUrl: String, tracks: List<Result>?) {
        Glide.with(this)
            .load(imageUrl)
            .into(binding.artistImageView)

        val adapter = TrackAdapter(
            itemList = tracks ?: emptyList()
        ) {
            CoroutineScope(Dispatchers.Main).launch {
                downloadAudioFileViewModel.downloadAudioFileState.collectLatest { state ->
                    if (state.isLoading) {
                        runOnUiThread {

                        }
                    } else if (state.error.isNotBlank()) {
                        runOnUiThread {
                            Toast.makeText(applicationContext, state.error, Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "Downloaded successfully at ${state.filePath}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
            downloadAudioFileViewModel.downloadAudioFiles(
                context = this@AudioFileActivity,
                audioUrl = AUDIO_URL,
                name = it.name
            )
        }
        binding.tracksRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.tracksRecyclerView.adapter = adapter
    }
}