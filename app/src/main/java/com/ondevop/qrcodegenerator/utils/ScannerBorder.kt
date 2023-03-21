package com.ondevop.qrcodegenerator.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.ondevop.qrcodegenerator.R

class ScannerBorder (context : Context, set : AttributeSet ) : View(context , set){

    private val painter = Paint().apply {
        color = ContextCompat.getColor(context,R.color.md_theme_light_primary)
        style = Paint.Style.STROKE
        strokeWidth =13f
    }


    private val rectF = RectF()
    private val clearRect = RectF()
    private val clearMode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)

    init {
        // Initialize the clearRect object once in the constructor
        clearRect.set(
            width / 2f - 200f,
            height / 2f - 200f,
            width / 2f + 200f,
            height / 2f + 200f
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Use the preallocated RectF object
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        painter.style = Paint.Style.STROKE
        canvas.drawRect(clearRect, painter)



        painter.style = Paint.Style.FILL
        painter.xfermode = clearMode
        canvas.drawRect(clearRect, painter)
    }
}