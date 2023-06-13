package com.enable.misense

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.enable.misense.databinding.ActivityImportImageBinding
import androidx.activity.result.contract.ActivityResultContracts


class ImportImage : AppCompatActivity() {
    private lateinit var binding: ActivityImportImageBinding
    private var i = 0
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpengallery.setOnClickListener { startGallery() }
        binding.btnPrev.setOnClickListener {
            finish()
        }
        binding.btnNext.setOnClickListener {
            setupProgressBar()
        }

    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@ImportImage)
                binding.imageView.setImageURI(uri)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun setupProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        i = binding.progressBar.progress
        Thread(Runnable {
            while (i < 100 ) {
                i += 1
                handler.post(Runnable {
                    binding.progressBar.progress = i
                    binding.progressCounter!!.text = i.toString() + "/" + binding.progressBar.max
                })
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            binding.progressBar.visibility = View.INVISIBLE
        }).start()
    }
}