package com.sharkBytesLab.camerastudio.KotlinCode

import android.content.Context
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import java.io.File

class CompressorCode(val context: Context, val actualImageFile: File, val myFile: String) {


//    val compressedImageFile = Compressor.compress(context, actualImageFile) {
//        default()
//        destination(myFile)
//    }
}