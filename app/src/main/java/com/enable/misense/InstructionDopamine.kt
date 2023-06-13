package com.enable.misense

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InstructionDopamine : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction_dopamine)

        val btntoimportimg: Button = findViewById(R.id.btn_toimportimg)
        btntoimportimg.setOnClickListener {
            val moveToImportImage = Intent( this@InstructionDopamine, ImportImage::class.java)
            startActivity(moveToImportImage)
        }

        val btnprevious: Button = findViewById(R.id.btn_prev)
        btnprevious.setOnClickListener {
            val moveToMainActivity = Intent( this@InstructionDopamine, MainActivity::class.java)
            startActivity(moveToMainActivity)
        }


    }
}