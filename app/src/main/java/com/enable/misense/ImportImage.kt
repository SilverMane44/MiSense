package com.enable.misense

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.view.View
import com.enable.misense.databinding.ActivityImportImageBinding
import androidx.activity.result.contract.ActivityResultContracts
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Rect
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream


class ImportImage : AppCompatActivity() {
    private lateinit var binding: ActivityImportImageBinding
    private var i = 0
    private val handler = Handler()
    lateinit var urisend: Uri
    var substance=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImportImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        OpenCVLoader.initDebug()
        Log.d("Substance", substance.toString())
        substance = intent.getIntExtra("SubType", substance)

        binding.btnOpengallery.setOnClickListener { startGallery() }
        binding.btnPrev.setOnClickListener {
            finish()
        }
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnNext.setOnClickListener {
            thresholdImg()
//            setupProgressBar()
            sendvaluetodisplay()
//            val moveToResultActivity = Intent(this@ImportImage, ResultActivity::class.java)
//            moveToResultActivity.putExtra("Uri",urisend)
//            startActivity(moveToResultActivity)
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
                urisend=uri
            }
            bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImg)
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
            while (i < 50 ) {
                i += 1
                handler.post(Runnable {
                    binding.progressBar.progress = i
                })
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            binding.progressBar.visibility = View.INVISIBLE
        }).start()
    }

    private lateinit var bm: Bitmap
    private lateinit var mat: Mat
    private lateinit var graymat: Mat
    private lateinit var alphamat: Mat
    private lateinit var resultmat: Mat
    private lateinit var RGBA: ArrayList<Mat>
    private fun thresholdImg() {
        mat = Mat(bm.height, bm.width, CvType.CV_8U)
        graymat = Mat(bm.height, bm.width, CvType.CV_8U)
        resultmat= Mat()
        alphamat= Mat(bm.height, bm.width, CvType.CV_8U)
        RGBA= arrayListOf()

        //Grayscale Mask
        Utils.bitmapToMat(bm, mat)
        Imgproc.cvtColor(mat,graymat, Imgproc.COLOR_RGBA2GRAY)
        Imgproc.threshold(graymat, alphamat, 100.0, 255.0, Imgproc.THRESH_BINARY)

        //Apply Alpha Ch
        Core.split(mat,RGBA)
        RGBA.removeAt(3)
        RGBA.add(alphamat)
        Core.merge(RGBA, resultmat)

        Utils.matToBitmap(resultmat,bm)
        cropImage()

    }

    private lateinit var crop1: Mat
    private lateinit var crop2: Mat
    private lateinit var crop3: Mat
    private lateinit var crop4: Mat
    private lateinit var hsvC1: ArrayList<Mat>
    private lateinit var hsvC2: ArrayList<Mat>
    private lateinit var hsvC3: ArrayList<Mat>
    private lateinit var hsvC4: ArrayList<Mat>

    private fun cropImage() {
        //Crop Parameter
        val imgwidth = resultmat.width()/4
        val imgheight = resultmat.height()/4
        val midpx = resultmat.width()/2
        val midpy = resultmat.height()/2

        val roi1= Rect (midpx-imgwidth, midpy-imgheight, imgwidth, imgheight)
        val roi2= Rect (midpx, midpy-imgheight, imgwidth, imgheight)
        val roi3= Rect (midpx-imgwidth, midpy, imgwidth, imgheight)
        val roi4= Rect (midpx, midpy, imgwidth, imgheight)

        //Crop Image
        hsvC1= arrayListOf()
        hsvC2= arrayListOf()
        hsvC3= arrayListOf()
        hsvC4= arrayListOf()

        crop1 = Mat(resultmat,roi1)
        crop2 = Mat(resultmat,roi2)
        crop3 = Mat(resultmat,roi3)
        crop4 = Mat(resultmat,roi4)

        getHSV()
    }

    private var conc1= 0.0
    private var conc2= 0.0
    private var conc3= 0.0
    private var conc4= 0.0
    private var hue1= 0.0
    private var hue2= 0.0
    private var hue3= 0.0
    private var hue4= 0.0
    private var sat1= 0.0
    private var sat2= 0.0
    private var sat3= 0.0
    private var sat4= 0.0

    private fun getHSV() {

        //Change to HSV
        Imgproc.cvtColor(crop1,crop1, Imgproc.COLOR_RGB2HSV)
        Imgproc.cvtColor(crop2,crop2, Imgproc.COLOR_RGB2HSV)
        Imgproc.cvtColor(crop3,crop3, Imgproc.COLOR_RGB2HSV)
        Imgproc.cvtColor(crop4,crop4, Imgproc.COLOR_RGB2HSV)

        //Get Average HSV value
        Core.split(crop1,hsvC1)
        hue1 = Core.mean(hsvC1[0]).toString().replace("[","0, ").split(", ")[1].toDouble()*2
        sat1 = Core.mean(hsvC1[1]).toString().replace("[","0, ").split(", ")[1].toDouble()

        Core.split(crop2,hsvC2)
        hue2 = Core.mean(hsvC2[0]).toString().replace("[","0, ").split(", ")[1].toDouble()*2
        sat2 = Core.mean(hsvC2[1]).toString().replace("[","0, ").split(", ")[1].toDouble()

        Core.split(crop3,hsvC3)
        hue3 = Core.mean(hsvC3[0]).toString().replace("[","0, ").split(", ")[1].toDouble()*2
        sat3 = Core.mean(hsvC3[1]).toString().replace("[","0, ").split(", ")[1].toDouble()

        Core.split(crop4,hsvC4)
        hue4 = Core.mean(hsvC4[0]).toString().replace("[","0, ").split(", ")[1].toDouble()*2
        sat4 = Core.mean(hsvC4[1]).toString().replace("[","0, ").split(", ")[1].toDouble()

        //Get Concentration
        if (substance==0) {
            conc1=(sat1-6.14823)/2.04579
            conc2=(sat2-6.14823)/2.04579
            conc3=(sat3-6.14823)/2.04579
            conc4=(sat4-6.14823)/2.04579

        }
        else if (substance==1) {
            conc1=(hue1-180.39899)/((-1)*17.14909)
            conc2=(hue2-180.39899)/((-1)*17.14909)
            conc3=(hue3-180.39899)/((-1)*17.14909)
            conc4=(hue4-180.39899)/((-1)*17.14909)
        }
        else   {
            conc1=(hue1-66.57604)/((-1)*1.5421)
            conc2=(hue2-66.57604)/((-1)*1.5421)
            conc3=(hue3-66.57604)/((-1)*1.5421)
            conc4=(hue4-66.57604)/((-1)*1.5421)
        }

        sendvaluetodisplay()

    }

    private fun sendvaluetodisplay() {

        val moveToResultActivity = Intent(this@ImportImage, ResultActivity::class.java).apply {
            putExtra("con2disp1",conc1)
            putExtra("con2disp2",conc2)
            putExtra("con2disp3",conc3)
            putExtra("con2disp4",conc4)
            putExtra("Uri",urisend.toString())

            if (substance == 1) {
                putExtra("val2disp1", sat1)
                putExtra("val2disp2", sat2)
                putExtra("val2disp3", sat3)
                putExtra("val2disp4", sat4)
            }
            else {
                putExtra("val2disp1",hue1)
                putExtra("val2disp2",hue2)
                putExtra("val2disp3",hue3)
                putExtra("val2disp4",hue4)
            }
        }

        startActivity(moveToResultActivity)

    }

}