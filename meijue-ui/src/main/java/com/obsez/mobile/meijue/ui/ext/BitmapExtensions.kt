package com.obsez.mobile.meijue.ui.ext


import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable


/**
 * 设法找出外边框的主要颜色，这是为了便于融合到背景
 *
 */
val Bitmap.dominantFrameColor: Int
    get() {
        val width = this.width
        val height = this.height
        if (width < 10 || height < 10) {
            return dominantColor2
        }
        
        val size = width * height
        val pixels = IntArray(size)
        //Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);
        this.getPixels(pixels, 0, width, 0, 0, width, height)
        var color: Int
        var r = 0
        var g = 0
        var b = 0
        var a: Int
        var count = 0
        for (x in 0 until width) {
            for (y in arrayOf(0, 1, 2, 3, 4, height - 5, height - 4, height - 3, height - 2, height - 1)) {
                color = pixels[y * width + x]
                a = Color.alpha(color)
                if (a > 0) {
                    r += Color.red(color)
                    g += Color.green(color)
                    b += Color.blue(color)
                    count++
                }
            }
        }
        for (y in 0 until height) {
            for (x in arrayOf(0, 1, 2, 3, 4, width - 5, width - 4, width - 3, width - 2, width - 1)) {
                color = pixels[y * width + x]
                a = Color.alpha(color)
                if (a > 0) {
                    r += Color.red(color)
                    g += Color.green(color)
                    b += Color.blue(color)
                    count++
                }
            }
        }
        r /= count
        g /= count
        b /= count
        //r = r shl 16 and 0x00FF0000
        //g = g shl 8 and 0x0000FF00
        //b = b and 0x000000FF
        color = Color.rgb(r, g, b) //-0x1000000 or r or g or b
        return color
    }

/**
 * 设法找到图片的主要颜色
 *
 * 1. https://android.jlelse.eu/creating-the-flexible-space-with-image-pattern-on-android-b5f8908b9921
 * 2. https://stackoverflow.com/questions/8471236/finding-the-dominant-color-of-an-image-in-an-android-drawable
 *
 */
val Bitmap.dominantColor: Int
    get() {
        val newBitmap = Bitmap.createScaledBitmap(this, 1, 1, true)
        val color = newBitmap.getPixel(0, 0)
        newBitmap.recycle()
        return color
    }

val Bitmap.dominantColor2: Int
    get() {
        //if (this == null) {
        //    return Color.TRANSPARENT
        //}
        val width = this.width
        val height = this.height
        val size = width * height
        val pixels = IntArray(size)
        //Bitmap bitmap2 = bitmap.copy(Bitmap.Config.ARGB_4444, false);
        this.getPixels(pixels, 0, width, 0, 0, width, height)
        var color: Int
        var r = 0
        var g = 0
        var b = 0
        var a: Int
        var count = 0
        for (i in pixels.indices) {
            color = pixels[i]
            a = Color.alpha(color)
            if (a > 0) {
                r += Color.red(color)
                g += Color.green(color)
                b += Color.blue(color)
                count++
            }
        }
        r /= count
        g /= count
        b /= count
        r = r shl 16 and 0x00FF0000
        g = g shl 8 and 0x0000FF00
        b = b and 0x000000FF
        color = -0x1000000 or r or g or b
        return color
    }


/**
 * Convert this Drawable to Bitmap representation. Should take care of every Drawable type
 */
fun Drawable.toBitmap(): Bitmap {
    if (this is BitmapDrawable) {
        return bitmap
    }
    
    val bitmap = if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }
    
    Canvas(bitmap).apply {
        setBounds(0, 0, width, height)
        draw(this)
    }
    return bitmap
}

