package com.enable.misense

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.enable.misense.databinding.ActivityImportImageBinding
import androidx.activity.result.contract.ActivityResultContracts


class ImportImage : AppCompatActivity() {
    private lateinit var binding: ActivityImportImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpengallery.setOnClickListener { startGallery() }

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



}