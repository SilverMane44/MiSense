package com.enable.misense

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.enable.misense.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var uriget: Uri
    private lateinit var fullbm: Bitmap
    private lateinit var bmcrop1: Bitmap
    private lateinit var bmcrop2: Bitmap
    private lateinit var bmcrop3: Bitmap
    private lateinit var bmcrop4: Bitmap
    private var conc1= 0.0
    private var conc2= 0.0
    private var conc3= 0.0
    private var conc4= 0.0
    private var val1= 0.0
    private var val2= 0.0
    private var val3= 0.0
    private var val4= 0.0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getValue()
        dispValue()

        binding.btnNext.setOnClickListener {
            val moveToMainActivity = Intent( this@ResultActivity, MainActivity::class.java)
            startActivity(moveToMainActivity)
        }

        binding.btnPrev.setOnClickListener {
            finish()
        }

    }

    private fun getValue(){
        conc1 = intent.getDoubleExtra("con2disp1", conc1)
        conc2 = intent.getDoubleExtra("con2disp2", conc2)
        conc3 = intent.getDoubleExtra("con2disp3", conc3)
        conc4 = intent.getDoubleExtra("con2disp4", conc4)
        val1 = intent.getDoubleExtra("val2disp1", val1)
        val2 = intent.getDoubleExtra("val2disp2", val2)
        val3 = intent.getDoubleExtra("val2disp3", val3)
        val4 = intent.getDoubleExtra("val2disp4", val4)
        uriget = Uri.parse(intent.getStringExtra("Uri"))
    }

    private fun dispValue(){

        fullbm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriget)
        val imgwidth = fullbm.width/4
        val imgheight = fullbm.height/4
        val midpx = fullbm.width/2
        val midpy = fullbm.height/2

        bmcrop1= Bitmap.createBitmap(fullbm, midpx-imgwidth, midpy-imgheight, imgwidth, imgheight)
        bmcrop2= Bitmap.createBitmap(fullbm, midpx, midpy-imgheight, imgwidth, imgheight)
        bmcrop3= Bitmap.createBitmap(fullbm, midpx-imgwidth, midpy, imgwidth, imgheight)
        bmcrop4= Bitmap.createBitmap(fullbm, midpx, midpy, imgwidth, imgheight)

        binding.imgResult1.setImageBitmap(bmcrop1)
        binding.imgResult2.setImageBitmap(bmcrop2)
        binding.imgResult3.setImageBitmap(bmcrop3)
        binding.imgResult4.setImageBitmap(bmcrop4)

        binding.val1.setText(String.format("%.2f", val1))
        binding.val2.setText(String.format("%.2f", val2))
        binding.val3.setText(String.format("%.2f", val3))
        binding.val4.setText(String.format("%.2f", val4))
        binding.concdisp1.setText(String.format("%.2f", conc1))
        binding.concdisp2.setText(String.format("%.2f", conc2))
        binding.concdisp3.setText(String.format("%.2f", conc3))
        binding.concdisp4.setText(String.format("%.2f", conc4))
    }
}