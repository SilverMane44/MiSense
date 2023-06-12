package com.enable.misense

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDopamine: Button = findViewById(R.id.btn_dopamine)
        btnDopamine.setOnClickListener {
            val moveToInstructionDopamine = Intent(this@MainActivity, InstructionDopamine::class.java)
            startActivity(moveToInstructionDopamine)
        }

        val btnGlucose: Button = findViewById(R.id.btn_glucose)
        btnGlucose.setOnClickListener {
            val moveToInstructionGlucose = Intent( this@MainActivity, InstructionGlucose::class.java)
            startActivity(moveToInstructionGlucose)
        }



    }
}