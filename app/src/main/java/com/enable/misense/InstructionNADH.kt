package com.enable.misense

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InstructionNADH : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instruction_nadh)

        val btntoimportimg: Button = findViewById(R.id.btn_toimportimg)
        btntoimportimg.setOnClickListener {
            val moveToImportImage = Intent( this@InstructionNADH, ImportImage::class.java)
            var substance=2
            moveToImportImage.putExtra("SubType", substance)
            startActivity(moveToImportImage)
        }

        val btn_prevnadh: Button=findViewById(R.id.btn_prev)
        btn_prevnadh.setOnClickListener {
            val moveToMainActivity = Intent( this@InstructionNADH, MainActivity::class.java)
            startActivity(moveToMainActivity)
        }

    }
}