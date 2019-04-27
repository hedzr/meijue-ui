package com.obsez.mobile.meijue.ui.util

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.text.InputFilter
import android.text.SpannableStringBuilder
import android.text.Spanned
import androidx.annotation.NonNull
import java.io.File
import java.lang.reflect.Array
import java.lang.reflect.InvocationTargetException
import java.text.DecimalFormat
import java.util.regex.Pattern

/**
 * Created by coco on 6/7/15.
 */
@Suppress("LocalVariableName", "MemberVisibilityCanBePrivate")
object FileUtil {
    
    
    fun getExtension(file: File?): String? {
        if (file == null) {
            return null
        }
        
        val dot = file.name.lastIndexOf(".")
        return if (dot >= 0) {
            file.name.substring(dot)
        } else {
            // No extension.
            ""
        }
    }
    
    fun getExtensionWithoutDot(file: File): String {
        val ext = getExtension(file)
        return if (ext!!.isEmpty()) {
            ext
        } else ext.substring(1)
    }
    
    @Suppress("ReplaceWithOperatorAssignment")
    fun getReadableFileSize(size: Long): String {
        val BYTES_IN_KILOBYTES = 1024
        val dec = DecimalFormat("###.#")
        val KILOBYTES = " KB"
        val MEGABYTES = " MB"
        val GIGABYTES = " GB"
        var fileSize = 0f
        var suffix = KILOBYTES
        
        if (size > BYTES_IN_KILOBYTES) {
            fileSize = (size / BYTES_IN_KILOBYTES).toFloat()
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES
                    suffix = GIGABYTES
                } else {
                    suffix = MEGABYTES
                }
            }
        }
        return dec.format(fileSize.toDouble()) + suffix
    }
    
    @NonNull
    fun getStoragePath(context: Context, isRemovable: Boolean): String? {
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        var storageVolumeClazz: Class<*>? = null
        
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
                val getPath = storageVolumeClazz!!.getMethod("getPath")
                val isRemovableMtd = storageVolumeClazz.getMethod("isRemovable")
                val getStorageVolumes = storageManager.javaClass.getMethod("getStorageVolumes")
                
                val result = getStorageVolumes.invoke(storageManager)
                
                for (sv in result as List<StorageVolume>) {
                    val b = isRemovableMtd.invoke(sv) as Boolean
                    if (b && isRemovable || !b && !isRemovable) {
                        //Timber.d("  ---Object--" + sv);
                        //Timber.d("  ---path--" + path + "--removable--" + b);
                        return getPath.invoke(sv) as String
                    }
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            
            return Environment.getExternalStorageDirectory().absolutePath
        }
        
        // legacy way to retrieve the paths
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume")
            val getVolumeList = storageManager.javaClass.getMethod("getVolumeList")
            val getPath = storageVolumeClazz!!.getMethod("getPath")
            val isRemovableMtd = storageVolumeClazz.getMethod("isRemovable")
            val result = getVolumeList.invoke(storageManager)
            val length = Array.getLength(result)
            //Timber.d("---length--" + length);
            for (i in 0 until length) {
                val storageVolumeElement = Array.get(result, i)
                //Timber.d("---Object--" + storageVolumeElement + "i==" + i);
                val path = getPath.invoke(storageVolumeElement) as String
                //Timber.d("---path_total--" + path);
                val removable = isRemovableMtd.invoke(storageVolumeElement) as Boolean
                if (isRemovable == removable) {
                    //Timber.d("---path--" + path);
                    return path
                }
            }
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
        
        return Environment.getExternalStorageDirectory().absolutePath
    }
    
    @JvmOverloads
    fun readSDCard(context: Context, isRemovable: Boolean?, freeOrTotal: Boolean? = false): Long {
        val df = DecimalFormat("0.00")
        if (getStoragePath(context, isRemovable!!) != null) {
            val sf = StatFs(getStoragePath(context, isRemovable))
            val blockSize: Long
            val blockCount: Long
            val availCount: Long
            if (Build.VERSION.SDK_INT > 18) {
                blockSize = sf.blockSizeLong //文件存储时每一个存储块的大小为4KB
                blockCount = sf.blockCountLong //存储区域的存储块的总个数
                availCount = sf.freeBlocksLong //存储区域中可用的存储块的个数（剩余的存储大小）
            } else {
                blockSize = sf.blockSize.toLong()
                blockCount = sf.blockCount.toLong()
                availCount = sf.freeBlocks.toLong()
            }
            //Log.d("sss", "总的存储空间大小:" + blockSize * blockCount / 1073741824 + "GB" + ",剩余空间:"
            //    + availCount * blockSize / 1073741824 + "GB"
            //    + "--存储块的总个数--" + blockCount + "--一个存储块的大小--" + blockSize / 1024 + "KB");
            //return df.format((freeOrTotal ? availCount : blockCount) * blockSize / 1073741824.0);
            return (if (freeOrTotal == true) availCount else blockCount).toLong() * blockSize
        }
        //return "-1";
        return -1
    }
    
    
    class NewFolderFilter @JvmOverloads constructor(private val maxLength: Int = 255, pattern: String = "^[^/<>|\\\\:&;#\n\r\t?*~\u0000-\u001f]*$") : InputFilter {
        private val pattern: Pattern = Pattern.compile(pattern)
        
        constructor(pattern: String) : this(255, pattern) {}
        
        override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
            val matcher = pattern.matcher(source)
            if (!matcher.matches()) {
                return if (source is SpannableStringBuilder) dest.subSequence(dstart, dend) else ""
            }
            
            var keep = maxLength - (dest.length - (dend - dstart))
            when {
                keep <= 0 -> return ""
                keep >= end - start -> return null // keep original
                else -> {
                    keep += start
                    if (Character.isHighSurrogate(source[keep - 1])) {
                        --keep
                        if (keep == start) {
                            return ""
                        }
                    }
                    return source.subSequence(start, keep).toString()
                }
            }
        }
    }
    /**
     * examples:
     * a simple allow only regex pattern: "^[a-z0-9]*$" (only lower case letters and numbers)
     * a simple anything but regex pattern: "^[^0-9;#&amp;]*$" (ban numbers and '&amp;', ';', '#' characters)
     */
}
