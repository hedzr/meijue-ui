package com.obsez.mobile.meijue.ui.ext


import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.Bitmap
import kotlin.random.Random


/**
 * 设法找出外边框的主要颜色，这是为了便于融合到背景
 *
 * return the dominant color of an image, depends its frame colors.
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
 * return the dominant color of an image.
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

/**
 * return the dominant color, we calculate each points to get it.
 */
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


/**
 * highlight / focus / shadow 效果
 * glow 效果：INNER, OUTER, SOLID, NORMAL
 *
 *
 * NOTE: setMaskFilter 不支持硬件加速，makeGlow() 需要关闭硬件加速才能生效。
 */
@Suppress("LocalVariableName")
fun Bitmap.applyGlow(length: Int = 16, clearColor: Int = 0xffffff, blurStyle: BlurMaskFilter.Blur = BlurMaskFilter.Blur.OUTER): Bitmap? {
    // create new bitmap, which will be painted and becomes result image
    val bmOut = Bitmap.createBitmap(this.width + length * 2, this.height + length * 2, Bitmap.Config.ARGB_8888)
    // setup canvas for painting
    val canvas = Canvas(bmOut)
    // setup default color
    canvas.drawColor(clearColor, PorterDuff.Mode.CLEAR)
    // create a blur paint for capturing alpha
    val ptBlur = Paint()
    ptBlur.maskFilter = BlurMaskFilter(length.toFloat(), blurStyle)
    val offsetXY = IntArray(2)
    // capture alpha into a bitmap
    val bmAlpha = this.extractAlpha(ptBlur, offsetXY)
    // create a color paint
    val ptAlphaColor = Paint()
    ptAlphaColor.color = -0x1
    // paint color for captured alpha region (bitmap)
    canvas.drawBitmap(bmAlpha, offsetXY[0].toFloat(), offsetXY[1].toFloat(), ptAlphaColor)
    // free memory
    bmAlpha.recycle()
    
    // paint the image source
    canvas.drawBitmap(this, 0f, 0f, null)
    
    // return out final image
    return bmOut
}

//fun Bitmap.makeGlowBad(context: Context): Bitmap? {
//    //var Image = BitmapFactory.decodeResource(mContext.getResources(), mThumbIds[position])
//    val image = this.copy(Bitmap.Config.ARGB_8888, true)
//    val paint = Paint()
//    paint.isDither = true
//    paint.isFilterBitmap = true
//    val glow = BitmapFactory.decodeResource(context.resources, R.drawable.glow_image)
//    val bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
//    val canvas = Canvas(bitmap)
//
//    canvas.drawBitmap(glow, Rect(0, 0, glow.width, glow.height), Rect(0, 0, image.width, image.height), paint)
//    canvas.drawBitmap(image, Rect(0, 0, image.width, image.height), Rect(0 + 5, 0 + 5, image.width - 5, image.height - 5), paint)
//
//    return bitmap
//}

/**
 * 反射效果：水面上的倒影
 *
 */
@Suppress("LocalVariableName")
fun Bitmap.applyRefelection(): Bitmap {
    // The gap we want between the reflection and the original image
    val reflectionGap = 0
    
    // Get your bitmap from drawable folder
    val originalImage: Bitmap = this
    
    val width = originalImage.width
    val height = originalImage.height
    
    // This will not scale but will flip on the Y axis
    val matrix = Matrix()
    matrix.preScale(1f, -1f)
    
    /* Create a Bitmap with the flip matix applied to it.
       We only want the bottom half of the image*/
    
    val reflectionImage: Bitmap = Bitmap.createBitmap(originalImage, 0,
        height / 2, width, height / 2, matrix, false)
    
    // Create a new bitmap with same width but taller to fit reflection
    val bitmapWithReflection = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_8888)
    // Create a new Canvas with the bitmap that's big enough for
    // the image plus gap plus reflection
    val canvas = Canvas(bitmapWithReflection)
    // Draw in the original image
    canvas.drawBitmap(originalImage, 0f, 0f, null)
    //Draw the reflection Image
    canvas.drawBitmap(reflectionImage, 0f, (height + reflectionGap).toFloat(), null)
    
    // Create a shader that is a linear gradient that covers the reflection
    val paint = Paint()
    val shader = LinearGradient(0f, originalImage.height.toFloat(), 0f,
        (bitmapWithReflection.height + reflectionGap).toFloat(),
        -0x66000001, 0x00ffffff, Shader.TileMode.CLAMP)
    // Set the paint to use this shader (linear gradient)
    paint.shader = shader
    // Set the Transfer mode to be porter duff and destination in
    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
    // Draw a rectangle using the paint with our linear gradient
    canvas.drawRect(0f, height.toFloat(), width.toFloat(),
        (bitmapWithReflection.height + reflectionGap).toFloat(), paint)
    if (originalImage.isRecycled) {
        originalImage.recycle()
        //originalImage = null
    }
    if (reflectionImage.isRecycled) {
        reflectionImage.recycle()
        //reflectionImage = null
    }
    return bitmapWithReflection
}

@Suppress("LocalVariableName")
fun Bitmap.applyGrayScale(): Bitmap {
    // constant factors
    val GS_RED = 0.299
    val GS_GREEN = 0.587
    val GS_BLUE = 0.114
    
    // create output bitmap
    val bmOut = Bitmap.createBitmap(this.width, this.height, this.config)
    // pixel information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    
    // get image size
    val width = this.width
    val height = this.height
    
    // scan through every single pixel
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get one pixel color
            pixel = this.getPixel(x, y)
            // retrieve color of all channels
            A = Color.alpha(pixel)
            R = Color.red(pixel)
            G = Color.green(pixel)
            B = Color.blue(pixel)
            // take conversion up to one single value
            B = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
            G = B
            R = G
            // set new pixel color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    
    // return final image
    return bmOut
}

@Suppress("LocalVariableName")
fun Bitmap.applyGammaCorrection(red: Double = 0.75, green: Double = 0.75, blue: Double = 0.75): Bitmap {
    // create output image
    val bmOut = Bitmap.createBitmap(this.width, this.height, this.config)
    // get image size
    val width = this.width
    val height = this.height
    // color information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    // constant value curve
    val MAX_SIZE = 256
    val MAX_VALUE_DBL = 255.0
    val MAX_VALUE_INT = 255
    val REVERSE = 1.0
    
    // gamma arrays
    val gammaR = IntArray(MAX_SIZE)
    val gammaG = IntArray(MAX_SIZE)
    val gammaB = IntArray(MAX_SIZE)
    
    // setting values for every gamma channels
    for (i in 0 until MAX_SIZE) {
        gammaR[i] = Math.min(MAX_VALUE_INT,
            (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / red) + 0.5).toInt())
        gammaG[i] = Math.min(MAX_VALUE_INT,
            (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / green) + 0.5).toInt())
        gammaB[i] = Math.min(MAX_VALUE_INT,
            (MAX_VALUE_DBL * Math.pow(i / MAX_VALUE_DBL, REVERSE / blue) + 0.5).toInt())
    }
    
    // apply gamma table
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            pixel = this.getPixel(x, y)
            A = Color.alpha(pixel)
            // look up gamma
            R = gammaR[Color.red(pixel)]
            G = gammaG[Color.green(pixel)]
            B = gammaB[Color.blue(pixel)]
            // set new color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    
    // return final image
    return bmOut
}

/**
 * Sepia Toning:
 * 复古风格的棕褐色色调
 *
 * Look up the concepts of Sepia on Wikipedia:
 * http://en.wikipedia.org/wiki/Photographic_print_toning
 * https://en.wikipedia.org/wiki/Sepia_(color)
 */
@Suppress("LocalVariableName")
fun Bitmap.applySepiaToning(depth: Int = 50, red: Double = 2.2, green: Double = 0.0, blue: Double = 2.2): Bitmap {
    // source image size
    val width = this.width
    val height = this.height
    // create output bitmap
    val bmOut = Bitmap.createBitmap(width, height, this.config)
    // constant grayscale
    val GS_RED = 0.3
    val GS_GREEN = 0.59
    val GS_BLUE = 0.11
    // color information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    
    // scan through all pixels of image
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            pixel = this.getPixel(x, y)
            // get color on each channel
            A = Color.alpha(pixel)
            R = Color.red(pixel)
            G = Color.green(pixel)
            B = Color.blue(pixel)
            // apply grayscale sample
            R = (GS_RED * R + GS_GREEN * G + GS_BLUE * B).toInt()
            G = R
            B = G
            
            // apply intensity level for sepia-toning on each channel
            R += (depth * red).toInt()
            if (R > 255) {
                R = 255
            }
            
            G += (depth * green).toInt()
            if (G > 255) {
                G = 255
            }
            
            B += (depth * blue).toInt()
            if (B > 255) {
                B = 255
            }
            
            // set new pixel color to output image
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    
    // return final image
    return bmOut
}

/**
 * Reducing color depth involves converting to standard values.
 * For example: if I want to offset 32, then each image color channel will apply the range: 0, 31, 63 .
 *
 * You might want to refer about list of monochrome on Wikipedia,color depth on Wikipedia,
 * with the help of this,reduceColorDepth() method you can set the color depth of image on click,
 * on action_down etc.
 *
 * http://en.wikipedia.org/wiki/List_of_monochrome_and_RGB_palettes#30-bit_RGB
 * https://en.wikipedia.org/wiki/Color_depth
 */
@Suppress("LocalVariableName")
fun Bitmap.reduceColorDepth(bitOffset: Int = 16): Bitmap {
    // get image original size
    val width = this.width
    val height = this.height
    // create output bitmap
    val bmOut = Bitmap.createBitmap(width, height, this.config)
    // color information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    
    // scan through all pixels
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            pixel = this.getPixel(x, y)
            A = Color.alpha(pixel)
            R = Color.red(pixel)
            G = Color.green(pixel)
            B = Color.blue(pixel)
            
            // round-off color offset
            R = R + bitOffset / 2 - (R + bitOffset / 2) % bitOffset - 1
            if (R < 0) {
                R = 0
            }
            G = G + bitOffset / 2 - (G + bitOffset / 2) % bitOffset - 1
            if (G < 0) {
                G = 0
            }
            B = B + bitOffset / 2 - (B + bitOffset / 2) % bitOffset - 1
            if (B < 0) {
                B = 0
            }
            
            // set pixel color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    
    // return final image
    return bmOut
}

@Suppress("LocalVariableName")
fun Bitmap.takeContrast(value: Double = 100.0): Bitmap {
    // src image size
    val width = this.width
    val height = this.height
    // create output bitmap with original size
    val bmOut = Bitmap.createBitmap(width, height, this.config)
    // color information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    // get contrast value
    val contrast = Math.pow((100 + value) / 100, 2.0)
    
    // scan through all pixels
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            pixel = this.getPixel(x, y)
            A = Color.alpha(pixel)
            // apply filter contrast for every channel R, G, B
            R = Color.red(pixel)
            R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
            if (R < 0) {
                R = 0
            } else if (R > 255) {
                R = 255
            }
            
            G = Color.red(pixel)
            G = (((G / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
            if (G < 0) {
                G = 0
            } else if (G > 255) {
                G = 255
            }
            
            B = Color.red(pixel)
            B = (((B / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
            if (B < 0) {
                B = 0
            } else if (B > 255) {
                B = 255
            }
            
            // set new pixel color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    
    // return final image
    return bmOut
}

@Suppress("LocalVariableName")
fun Bitmap.takeColorContrast(value: Double = 100.0): Bitmap {
    // src image size
    val width = this.width
    val height = this.height
    // create output bitmap with original size
    val bmOut = Bitmap.createBitmap(width, height, this.config)
    // color information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    // get contrast value
    val contrast = Math.pow((100 + value) / 100, 2.0)
    
    // scan through all pixels
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            pixel = this.getPixel(x, y)
            A = Color.alpha(pixel)
            // apply filter contrast for every channel R, G, B
            R = Color.red(pixel)
            R = (((R / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
            if (R < 0) {
                R = 0
            } else if (R > 255) {
                R = 255
            }
            
            G = Color.green(pixel)
            G = (((G / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
            if (G < 0) {
                G = 0
            } else if (G > 255) {
                G = 255
            }
            
            B = Color.blue(pixel)
            B = (((B / 255.0 - 0.5) * contrast + 0.5) * 255.0).toInt()
            if (B < 0) {
                B = 0
            } else if (B > 255) {
                B = 255
            }
            
            // set new pixel color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    
    // return final image
    return bmOut
}

/**
 * set brightness of an image
 */
@Suppress("LocalVariableName")
fun Bitmap.setBrightness(value: Int = -80): Bitmap {
    // original image size
    val width = this.width
    val height = this.height
    // create output bitmap
    val bmOut = Bitmap.createBitmap(width, height, this.config)
    // color information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    
    // scan through all pixels
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            pixel = this.getPixel(x, y)
            A = Color.alpha(pixel)
            R = Color.red(pixel)
            G = Color.green(pixel)
            B = Color.blue(pixel)
            
            // increase/decrease each channel
            R += value
            if (R > 255) {
                R = 255
            } else if (R < 0) {
                R = 0
            }
            
            G += value
            if (G > 255) {
                G = 255
            } else if (G < 0) {
                G = 0
            }
            
            B += value
            if (B > 255) {
                B = 255
            } else if (B < 0) {
                B = 0
            }
            
            // apply new pixel color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    
    // return final image
    return bmOut
}

/**
 * Color boost technique is basically based on color filtering,
 * which is to increase the intensity of a single color channel.
 */
@Suppress("LocalVariableName")
fun Bitmap.applyBoostColor(type: Int = 1, percent: Float = 90f): Bitmap {
    // original image size
    val width = this.width
    val height = this.height
    // create output bitmap
    val bmOut = Bitmap.createBitmap(width, height, this.config)
    // color information
    var A: Int
    var R: Int
    var G: Int
    var B: Int
    var pixel: Int
    // scan through all pixels
    for (x in 0 until width) {
        for (y in 0 until height) {
            // get pixel color
            pixel = this.getPixel(x, y)
            A = Color.alpha(pixel)
            R = Color.red(pixel)
            G = Color.green(pixel)
            B = Color.blue(pixel)
            if (type == 1) {
                R = (R * (1 + percent)).toInt()
                if (R > 255) R = 255
            } else if (type == 2) {
                G = (G * (1 + percent)).toInt()
                if (G > 255) G = 255
            } else if (type == 3) {
                B = (B * (1 + percent)).toInt()
                if (B > 255) B = 255
            }
            // apply new pixel color to output bitmap
            bmOut.setPixel(x, y, Color.argb(A, R, G, B))
        }
    }
    // return final image
    return bmOut
}


fun Bitmap.computeConvolution3x3(matrix: ConvolutionMatrix): Bitmap {
    return ConvolutionMatrix.computeConvolution3x3(this, matrix)
}

/**
 * Constructor with argument of size.
 *
 * It’s not easy to explain about Convolution, so I just pick up several article links which would be helpful for you to understand convolution better.and you can use it for references.
 *
 * GIMP Documentation on Convolution Matrix <— really detailed on explanation.
 * VcsKicks.Com – Convolution Application on Box Blur effect
 * Convolution Study by Ahn Song Ho
 * CodeProject – Image Processing for Dummies (Part II – Convolution Filters)
 * RoboRealms – Short article on Convolution
 * Convolution on Wikipedia
 *
 * http://docs.gimp.org/en/plug-in-convmatrix.html
 * http://www.vcskicks.com/box-blur.php
 * http://www.songho.ca/dsp/convolution/convolution.html
 * http://www.codeproject.com/KB/GDI-plus/csharpfilters.aspx
 * http://www.roborealm.com/help/Convolution.php
 * http://en.wikipedia.org/wiki/Convolution
 *
 */
class ConvolutionMatrix(size: Int) {
    
    internal var Matrix: Array<DoubleArray> = Array(size) { DoubleArray(size) }
    internal var Factor = 1.0f
    internal var Offset = 1.0f
    
    fun setAll(value: Double) {
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) {
                Matrix[x][y] = value
            }
        }
    }
    
    fun applyConfig(config: Array<DoubleArray>) {
        for (x in 0 until SIZE) {
            for (y in 0 until SIZE) {
                Matrix[x][y] = config[x][y]
            }
        }
    }
    
    companion object {
        const val SIZE = 3
        
        fun computeConvolution3x3(src: Bitmap, matrix: ConvolutionMatrix): Bitmap {
            val width = src.width
            val height = src.height
            val result = Bitmap.createBitmap(width, height, src.config)
            
            var A: Int
            var R: Int
            var G: Int
            var B: Int
            var sumR: Int
            var sumG: Int
            var sumB: Int
            val pixels = Array(SIZE) { IntArray(SIZE) }
            
            for (y in 0 until height - 2) {
                for (x in 0 until width - 2) {
                    
                    // get pixel matrix
                    for (i in 0 until SIZE) {
                        for (j in 0 until SIZE) {
                            pixels[i][j] = src.getPixel(x + i, y + j)
                        }
                    }
                    
                    // get alpha of center pixel
                    A = Color.alpha(pixels[1][1])
                    
                    // init color sum
                    sumB = 0
                    sumG = sumB
                    sumR = sumG
                    
                    // get sum of RGB on matrix
                    for (i in 0 until SIZE) {
                        for (j in 0 until SIZE) {
                            sumR += (Color.red(pixels[i][j]) * matrix.Matrix[i][j]).toInt()
                            sumG += (Color.green(pixels[i][j]) * matrix.Matrix[i][j]).toInt()
                            sumB += (Color.blue(pixels[i][j]) * matrix.Matrix[i][j]).toInt()
                        }
                    }
                    
                    // get final Red
                    R = (sumR / matrix.Factor + matrix.Offset).toInt()
                    if (R < 0) {
                        R = 0
                    } else if (R > 255) {
                        R = 255
                    }
                    
                    // get final Green
                    G = (sumG / matrix.Factor + matrix.Offset).toInt()
                    if (G < 0) {
                        G = 0
                    } else if (G > 255) {
                        G = 255
                    }
                    
                    // get final Blue
                    B = (sumB / matrix.Factor + matrix.Offset).toInt()
                    if (B < 0) {
                        B = 0
                    } else if (B > 255) {
                        B = 255
                    }
                    
                    // apply new pixel
                    result.setPixel(x + 1, y + 1, Color.argb(A, R, G, B))
                }
            }
            
            // final image
            return result
        }
    }
}

/**
 * 高斯模糊效果
 */
fun Bitmap.applyGaussianBlur(): Bitmap {
    //set gaussian blur configuration
    val gaussianBlurConfig = arrayOf(
        doubleArrayOf(1.0, 2.0, 1.0),
        doubleArrayOf(2.0, 4.0, 2.0),
        doubleArrayOf(1.0, 2.0, 1.0))
    // create instance of Convolution matrix
    val convolutionMatrix = ConvolutionMatrix(3)
    // Apply Configuration
    convolutionMatrix.applyConfig(gaussianBlurConfig)
    convolutionMatrix.Factor = 16f
    convolutionMatrix.Offset = 0f
    //return out put bitmap
    return ConvolutionMatrix.computeConvolution3x3(this, convolutionMatrix)
}

/**
 * 锐化效果
 */
fun Bitmap.applySharpen(weight: Double): Bitmap {
    // set sharpness configuration
    val sharpConfig = arrayOf(
        doubleArrayOf(0.0, -2.0, 0.0),
        doubleArrayOf(-2.0, weight, -2.0),
        doubleArrayOf(0.0, -2.0, 0.0))
    //create convolution matrix instance
    val convolutionMatrix = ConvolutionMatrix(3)
    //apply configuration
    convolutionMatrix.applyConfig(sharpConfig)
    //set weight according to factor
    convolutionMatrix.Factor = (weight - 8).toFloat()
    return ConvolutionMatrix.computeConvolution3x3(this, convolutionMatrix)
}

/**
 * 均值移除效果 - 用平均移除法来达到轮廓效果
 *
 * 图像预处理：均值减法（Mean Subtraction），归一化（Normalization），PCA和白化（Whitening）
 */
fun Bitmap.applyMeanRemoval(): Bitmap {
    // set Mean Removal configuration
    val meanRemovalConfig = arrayOf(
        doubleArrayOf(-1.0, -1.0, -1.0),
        doubleArrayOf(-1.0, 9.0, -1.0),
        doubleArrayOf(-1.0, -1.0, -1.0))
    //create convolution matrix instance
    val convolutionMatrix = ConvolutionMatrix(3)
    //apply configuration
    convolutionMatrix.applyConfig(meanRemovalConfig)
    // set weight of factor and offset
    convolutionMatrix.Factor = 1f
    convolutionMatrix.Offset = 0f
    return ConvolutionMatrix.computeConvolution3x3(this, convolutionMatrix)
}

/**
 * 平滑效果
 */
fun Bitmap.applySmoothEffect(value: Double): Bitmap {
    //create convolution matrix instance
    val convolutionMatrix = ConvolutionMatrix(3)
    convolutionMatrix.setAll(1.0)
    convolutionMatrix.Matrix[1][1] = value
    // set weight of factor and offset
    convolutionMatrix.Factor = (value + 8).toFloat()
    convolutionMatrix.Offset = 1f
    return ConvolutionMatrix.computeConvolution3x3(this, convolutionMatrix)
}

/**
 * 浮雕效果
 */
fun Bitmap.applyEmboss(): Bitmap {
    // set Emboss configuration
    val embossConfig = arrayOf(
        doubleArrayOf(-1.0, 0.0, -1.0),
        doubleArrayOf(0.0, 4.0, 0.0),
        doubleArrayOf(-1.0, 0.0, -1.0))
    //create convolution matrix instance
    val convolutionMatrix = ConvolutionMatrix(3)
    //apply configuration
    convolutionMatrix.applyConfig(embossConfig)
    // set weight of factor and offset
    convolutionMatrix.Factor = 1f
    convolutionMatrix.Offset = 127f
    return ConvolutionMatrix.computeConvolution3x3(this, convolutionMatrix)
}

/**
 * 雕刻效果
 */
fun Bitmap.applyEngrave(): Bitmap {
    //create convolution matrix instance
    val convolutionMatrix = ConvolutionMatrix(3)
    convolutionMatrix.setAll(0.0)
    convolutionMatrix.Matrix[0][0] = -2.0
    convolutionMatrix.Matrix[1][1] = 2.0
    // set weight of factor and offset
    convolutionMatrix.Factor = 1f
    convolutionMatrix.Offset = 95f
    return ConvolutionMatrix.computeConvolution3x3(this, convolutionMatrix)
}

/**
 *
 * http://www.codeproject.com/Articles/19077/Hue-Saturation-Lightness-Filter
 *
 */
fun Bitmap.applyHueFilter(level: Int = 3): Bitmap {
    // get original image size
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    val hsv = FloatArray(3)
    // get pixel array from source image
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    
    var index = 0
    // iteration through pixels
    for (y in 0 until height) {
        for (x in 0 until width) {
            // get current index in 2D-matrix
            index = y * width + x
            // convert to HSV
            Color.colorToHSV(pixels[index], hsv)
            // increase Saturation level
            hsv[0] *= level.toFloat()
            hsv[0] = Math.max(0.0, Math.min(hsv[0].toDouble(), 360.0)).toFloat()
            // take color back
            pixels[index] = pixels[index] or Color.HSVToColor(hsv)
        }
    }
    
    // output bitmap
    val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
    return bmOut
}

/**
 *
 * http://en.wikipedia.org/wiki/HSL_and_HSV#Saturation
 */
fun Bitmap.applySaturationFilter(level: Int): Bitmap {
    // get original image size
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    val hsv = FloatArray(3)
    // get pixel array from source image
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    
    var index = 0
    // iteration through all pixels
    for (y in 0 until height) {
        for (x in 0 until width) {
            // get current index in 2D-matrix
            index = y * width + x
            // convert to HSV
            Color.colorToHSV(pixels[index], hsv)
            // increase Saturation level
            hsv[1] *= level.toFloat()
            hsv[1] = Math.max(0.0, Math.min(hsv[1].toDouble(), 1.0)).toFloat()
            // take color back
            pixels[index] = Color.HSVToColor(hsv)
        }
    }
    
    // output bitmap
    val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
    return bmOut
}

/**
 * 彩色墨水效果
 *
 * 处理后带有水彩画风格，色块变得更平板。
 * 有时候也称作卡通渲染（Cel shading or toon shading），是一种去真实感的渲染方法，带有手绘效果。
 */
fun Bitmap.applyShadingFilter(shadingColor: Int): Bitmap {
    // get original image size
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    // get pixel array from source image
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    
    var index = 0
    // iteration through all pixels
    for (y in 0 until height) {
        for (x in 0 until width) {
            // get current index in 2D-matrix
            index = y * width + x
            // AND
            pixels[index] = pixels[index] and shadingColor
        }
    }
    
    // output bitmap
    val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
    return bmOut
}

/**
 * 噪声效果
 */
fun Bitmap.applyFlea(): Bitmap {
    // get source image size
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    // get pixel array from source
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    // create a random object
    //val random = Random.nextInt()
    
    var index = 0
    // iteration through pixels
    for (y in 0 until height) {
        for (x in 0 until width) {
            // get current index in 2D-matrix
            index = y * width + x
            // get random color
            val randColor = Color.rgb(Random.nextInt(255),
                Random.nextInt(255), Random.nextInt(255))
            // OR
            pixels[index] = pixels[index] or randColor
        }
    }
    // output bitmap
    val bmOut = Bitmap.createBitmap(width, height, this.config)
    bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
    return bmOut
}


internal const val PI = 3.14159
internal const val FULL_CIRCLE_DEGREE = 360.0
internal const val HALF_CIRCLE_DEGREE = 180.0
internal const val RANGE = 256.0

/**
 *
 * Working with images in Google Android:
 * https://www.developer.com/ws/android/programming/Working-with-Images-in-Googles-Android-3748281.htm
 *
 * 从使用到源码，细说 Android 中的 tint 着色器: https://yifeng.studio/2017/03/30/android-tint/
 *
 */
@Suppress("LocalVariableName")
fun Bitmap.applyTint(degree: Int = 80): Bitmap {
    
    // get source image size
    val width = this.width
    val height = this.height
    
    val pix: IntArray? = IntArray(width * height)
    // get pixel array from source
    this.getPixels(pix, 0, width, 0, 0, width, height)
    
    var RY: Int
    var GY: Int
    var BY: Int
    var RYY: Int
    var GYY: Int
    var BYY: Int
    var R: Int
    var G: Int
    var B: Int
    var Y: Int
    val angle = PI * degree.toDouble() / HALF_CIRCLE_DEGREE
    
    val S = (RANGE * Math.sin(angle)).toInt()
    val C = (RANGE * Math.cos(angle)) .toInt()
    
    for (y in 0 until height)
        for (x in 0 until width) {
            val index = y * width + x
            val r = pix!![index] shr 16 and 0xff
            val g = pix[index] shr 8 and 0xff
            val b = pix[index] and 0xff
            RY = (70 * r - 59 * g - 11 * b) / 100
            GY = (-30 * r + 41 * g - 11 * b) / 100
            BY = (-30 * r - 59 * g + 89 * b) / 100
            Y = (30 * r + 59 * g + 11 * b) / 100
            RYY = (S * BY + C * RY) / 256
            BYY = (C * BY - S * RY) / 256
            GYY = (-51 * RYY - 19 * BYY) / 100
            R = Y + RYY
            R = if (R < 0) 0 else if (R > 255) 255 else R
            G = Y + GYY
            G = if (G < 0) 0 else if (G > 255) 255 else G
            B = Y + BYY
            B = if (B < 0) 0 else if (B > 255) 255 else B
            pix[index] = -0x1000000 or (R shl 16) or (G shl 8) or B
        }
    // output bitmap
    val outBitmap = Bitmap.createBitmap(width, height, this.config)
    outBitmap.setPixels(pix, 0, width, 0, 0, width, height)
    
    //pix = null
    
    return outBitmap
}

@Suppress("LocalVariableName")
fun Bitmap.applyDarkFilter(): Bitmap {
    // get image source size
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    // get pixel array from source
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    // create random object
    //val random = Random()
    
    var R: Int
    var G: Int
    var B: Int
    var index = 0
    var thresHold = 0
    // iteration through pixels
    for (y in 0 until height) {
        for (x in 0 until width) {
            // get current index in 2D-matrix
            index = y * width + x
            // get RGB colors
            R = Color.red(pixels[index])
            G = Color.green(pixels[index])
            B = Color.blue(pixels[index])
            // generate threshold
            thresHold = Random.nextInt(255)
            if (R < thresHold && G < thresHold && B < thresHold) {
                pixels[index] = Color.rgb(0, 0, 0)
            }
        }
    }
    // create output bitmap
    val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
    return bmOut
}

@Suppress("LocalVariableName")
fun Bitmap.applySnowFalling(): Bitmap {
    // get source image size
    val width = this.width
    val height = this.height
    val pixels = IntArray(width * height)
    // get pixel array from source
    this.getPixels(pixels, 0, width, 0, 0, width, height)
    // create random object
    //val random = Random()
    
    var R: Int
    var G: Int
    var B: Int
    var index = 0
    var thresHold = 50
    // iteration through pixels
    for (y in 0 until height) {
        for (x in 0 until width) {
            // get current index in 2D-matrix
            index = y * width + x
            // get RGB colors
            R = Color.red(pixels[index])
            G = Color.green(pixels[index])
            B = Color.blue(pixels[index])
            // generate threshold
            thresHold = Random.nextInt(255)
            if (R > thresHold && G > thresHold && B > thresHold) {
                pixels[index] = Color.rgb(255, 255, 255)
            }
        }
    }
    // create output bitmap
    val bmOut = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    bmOut.setPixels(pixels, 0, width, 0, 0, width, height)
    return bmOut
}


// image effects - see also http://shaikhhamadali.blogspot.com/p/home.html - Convolution Matrix, ...

//


fun Bitmap.newBitmap(): Bitmap {
    return if (width <= 0 || height <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }
}

fun Bitmap.newBitmapFromThis(): Bitmap {
    return if (width <= 0 || height <= 0) {
        Bitmap.createBitmap(1, 1, this.config)
    } else {
        Bitmap.createBitmap(width, height, this.config)
    }
}

fun Bitmap.newBitmap(width: Int, height: Int): Bitmap {
    return if (width <= 0 || height <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    }
}

fun Bitmap.newOverlay(bgColor: Int = 0x66ffffff): Bitmap {
    val bitmap = newBitmap()
    
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    //paint.isDither = true
    //paint.isFilterBitmap = true
    
    Canvas(bitmap).apply {
        drawColor(bgColor, PorterDuff.Mode.CLEAR)
    }
    
    return bitmap
}

/**
 * 半透明圆角矩形
 */
fun Bitmap.drawNewOverlay(color: Int = 0x66ffffff, circleRect: RectF? = null, radius: Float = 5f): Bitmap {
    val bitmap = newBitmap()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    Canvas(bitmap).apply {
        //Draw Overlay
        paint.color = color // 0 //getResources().getColor(android.R.color.black));
        
        //paint.style = Paint.Style.FILL
        //drawPaint(paint)
        
        //Draw semi-transparent shape
        //paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        drawRoundRect(circleRect ?: RectF(0f, 0f, bitmap.width.toFloat(),
            bitmap.height.toFloat()), radius, radius, paint)
    }
    
    return bitmap
}

/**
 * 白色底色，透明圆角矩形
 */
fun Bitmap.drawNewHoleOverlay(circleRect: RectF? = null, radius: Float = 5f): Bitmap {
    val bitmap = newBitmap()
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    
    Canvas(bitmap).apply {
        //Draw Overlay
        paint.color = 0xffffff // 0 //getResources().getColor(android.R.color.black));
        paint.style = Paint.Style.FILL
        drawPaint(paint)
        
        //Draw transparent shape
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        drawRoundRect(circleRect ?: RectF(0f, 0f, bitmap.width.toFloat(),
            bitmap.height.toFloat()), radius, radius, paint)
    }
    
    return bitmap
}

fun Drawable.newBitmap(): Bitmap {
    return if (intrinsicWidth <= 0 || intrinsicHeight <= 0) {
        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
    } else {
        Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
    }
}

fun Bitmap.newOverlay(context: Context, overlayDrawableResourceId: Int): Bitmap? {
    try {
        val width = this.width
        val height = this.height
        val r = context.resources
        return this.decodeSampledBitmapFromResource(r, overlayDrawableResourceId, width, height)
    } catch (ex: Exception) {
    }
    return null
}

fun Drawable.newOverlay(context: Context, overlayDrawableResourceId: Int): Bitmap? {
    try {
        val width = this.intrinsicWidth
        val height = this.intrinsicHeight
        val r = context.resources
        val bm = newBitmap()
        return bm.decodeSampledBitmapFromResource(r, overlayDrawableResourceId, width, height)
    } catch (ex: Exception) {
    }
    return null
}


fun Bitmap.applyOverlay(context: Context, overlayBitmap: Bitmap): Drawable? {
    var drawable: Drawable? = null
    try {
        this.let { bm ->
            val width = bm.width
            val height = bm.height
            val r = context.resources
            
            val layers = arrayOfNulls<Drawable>(2)
            
            layers[0] = BitmapDrawable(r, bm)
            layers[1] = BitmapDrawable(r, overlayBitmap)
            
            //val layerDrawable = LayerDrawable(layers)
            //bitmap = layerDrawable.drawableToBitmap()
            drawable = LayerDrawable(layers)
        }
    } catch (ex: Exception) {
    }
    
    return drawable
}

fun Drawable.applyOverlay(context: Context, overlayBitmap: Bitmap): Drawable? {
    var drawable: Drawable? = null
    try {
        val width = this.intrinsicWidth
        val height = this.intrinsicHeight
        val r = context.resources
        
        val layers = arrayOfNulls<Drawable>(2)
        
        layers[0] = this
        layers[1] = BitmapDrawable(r, overlayBitmap)
        
        //val layerDrawable = LayerDrawable(layers)
        //bitmap = layerDrawable.drawableToBitmap()
        drawable = LayerDrawable(layers)
        
    } catch (ex: Exception) {
    }
    
    return drawable
}

//fun Drawable.applyOverlay(context: Context, overlayDrawableResourceId: Int): Drawable? {
//    var drawable: Drawable? = null
//    try {
//        toBitmap().let { bm ->
//            val width = bm.width
//            val height = bm.height
//            val r = context.resources
//
//            val layers = arrayOfNulls<Drawable>(2)
//
//            layers[0] = this
//            layers[1] = BitmapDrawable(r, bm.decodeSampledBitmapFromResource(r, overlayDrawableResourceId, width, height))
//            //val layerDrawable = LayerDrawable(layers)
//            //bitmap = layerDrawable.drawableToBitmap()
//            drawable = LayerDrawable(layers)
//        }
//    } catch (ex: Exception) {
//    }
//
//    return drawable
//}
//
//
//fun Bitmap.applyOverlay(context: Context, overlayDrawableResourceId: Int): Drawable? {
//    //var bitmap: Bitmap? = null
//    try {
//        val width = this.width
//        val height = this.height
//        val r = context.resources
//
//        val imageAsDrawable = BitmapDrawable(r, this)
//        val layers = arrayOfNulls<Drawable>(2)
//
//        layers[0] = imageAsDrawable
//        layers[1] = BitmapDrawable(r, decodeSampledBitmapFromResource(r, overlayDrawableResourceId, width, height))
//
//        //val layerDrawable = LayerDrawable(layers)
//        //bitmap = layerDrawable.toBitmap()
//        return LayerDrawable(layers)
//    } catch (ex: Exception) {
//    }
//
//    return null
//}

fun Bitmap.decodeSampledBitmapFromResource(res: Resources, resId: Int,
                                           reqWidth: Int, reqHeight: Int): Bitmap {
    
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options()
    options.inJustDecodeBounds = true
    BitmapFactory.decodeResource(res, resId, options)
    
    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)
    
    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeResource(res, resId, options)
}

fun Bitmap.calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    // Raw height and width of image
    val height = options.outHeight
    val width = options.outWidth
    var inSampleSize = 1
    
    if (height > reqHeight || width > reqWidth) {
        
        val halfHeight = height / 2
        val halfWidth = width / 2
        
        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }
    
    return inSampleSize
}

//fun Bitmap.drawableToBitmap(drawable: Drawable): Bitmap? {
//    var bitmap: Bitmap? = null
//
//    if (drawable is BitmapDrawable) {
//        if (drawable.bitmap != null) {
//            return drawable.bitmap
//        }
//    }
//
//    bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
//        Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888) // Single color bitmap will be created of 1x1 pixel
//    } else {
//        Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//    }
//
//    val canvas = Canvas(bitmap!!)
//    drawable.setBounds(0, 0, canvas.width, canvas.height)
//    drawable.draw(canvas)
//    return bitmap
//}



