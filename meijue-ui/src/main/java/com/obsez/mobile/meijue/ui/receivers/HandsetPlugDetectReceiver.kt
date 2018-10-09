package com.obsez.mobile.meijue.ui.receivers

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothHeadset
import android.bluetooth.BluetoothProfile
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import android.widget.Toast
import timber.log.Timber
import java.io.FileNotFoundException
import java.io.FileReader


/**
 * 耳机插入和拔出时会发出广播ACTION_HEADSET_PLUG，所以只要注册一个广播接受者就可以监听耳机的状态了
 *
 * 为了检测蓝牙耳机的状态，必须：
 * <uses-permission android:name="android.permission.BLUETOOTH" />
 *
 * 为了操作音频设置，可能需要：
 * <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
 *
 * 要使用 BoardcastReceiver，需要声明：
 * <receiver android:name="com.obsez.mobile.meijue.ui.receivers.HandsetPlugDetectReceiver" android:enabled="true" android:exported="true" />
 *
 */
class HandsetPlugDetectReceiver : BroadcastReceiver() {
    
    private var mCallback: ((connected: Boolean) -> Unit)? = null
    
    companion object {
        private val instance by lazy { HandsetPlugDetectReceiver() }
        
        val isRegistered: Boolean = instance.mCallback != null
        
        fun register(a: Activity, callback: ((connected: Boolean) -> Unit)? = null) {
            if (isRegistered) {
                throw IllegalArgumentException("multiple register calls")
            }
            
            instance.mCallback = callback
            
            val intentFilter = IntentFilter()
            intentFilter.addAction(Intent.ACTION_HEADSET_PLUG)
            intentFilter.addAction(BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED)
            a.registerReceiver(instance, intentFilter)
        }
        
        fun unregister(a: Activity) {
            a.unregisterReceiver(instance)
            instance.mCallback = null
        }
        
        private const val HEADSET_STATE_PATH = "/sys/class/switch/h2w/state"
        
        val isHandsetPresent: Boolean
            get() {
                val buffer = CharArray(1024)
                var newState = 0
                try {
                    val file = FileReader(HEADSET_STATE_PATH)
                    val len = file.read(buffer, 0, 1024)
                    newState = Integer.valueOf(String(buffer, 0, len).trim { it <= ' ' })
                } catch (e: FileNotFoundException) {
                    Timber.e("This kernel does not have wired headset support")
                } catch (e: Exception) {
                    Timber.e(e, "eee")
                }
                // 0：无插入，1：耳机和话筒均插入，2：仅插入话筒。
                return newState != 0
            }
        
        
        /**
         * need declaration:
         * <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
         */
        fun checkHandsetPresent(context: Context): Boolean {
            val audoManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            return audoManager.isWiredHeadsetOn
        }
        
        /**
         * need declaration:
         * <uses-permission android:name="android.permission.MODIFY_AUDIO_SETTINGS" />
         *
         * = checkHandsetPresent(context)
         */
        fun getHeadsetStatus(context: Context): Boolean {
            val audoManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            
            //      IntentFilter iFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            //      Intent iStatus = registerReceiver(null, iFilter);
            //      boolean isConnected = iStatus.getIntExtra("state", 0) == 1;
            //
            //      if(isConnected){
            //         Toast.makeText(MainActivity.this,"耳机ok",Toast.LENGTH_SHORT).show();
            //      }
            
            return audoManager.isWiredHeadsetOn
        }
        
        
        /**
         * need
         * <uses-permission android:name="android.permission.BLUETOOTH" />
         *
         * @return {#BluetoothProfile.STATE_CONNECTED}
         */
        fun getBlootoothHeadsetStatus(context: Context): Int {
            val ba = BluetoothAdapter.getDefaultAdapter()
            
            // int isBlueCon;//蓝牙适配器是否存在，即是否发生了错误
            if (ba == null) {
                // isBlueCon = -1;     //error
                return -1
            } else if (ba.isEnabled()) {
                val a2dp = ba.getProfileConnectionState(BluetoothProfile.A2DP)              //可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
                val headset = ba.getProfileConnectionState(BluetoothProfile.HEADSET)        //蓝牙头戴式耳机，支持语音输入输出
                val health = ba.getProfileConnectionState(BluetoothProfile.HEALTH)          //蓝牙穿戴式设备
                
                //查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态
                var flag = -1
                if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                    flag = a2dp
                } else if (headset == BluetoothProfile.STATE_CONNECTED) {
                    flag = headset
                } else if (health == BluetoothProfile.STATE_CONNECTED) {
                    flag = health
                }
                //说明连接上了三种设备的一种
                if (flag != -1) {
                    // isBlueCon = 1;            //connected
                    return BluetoothProfile.STATE_CONNECTED
                }
            }
            return -2
        }
        
    }
    
    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //TO/DO("HandsetPlugDetectReceiver.onReceive() is not implemented")
        
        val action = intent.action
        if (Intent.ACTION_HEADSET_PLUG.equals(action)) {
            if (intent.hasExtra("state")) {
                val state = intent.getIntExtra("state", 0)
                // 0：无插入，1：耳机和话筒均插入，2：仅插入话筒。
                if (state == 1) {
                    if (mCallback != null)
                        mCallback?.invoke(true)
                    else
                        Toast.makeText(context, "插入耳机", Toast.LENGTH_SHORT).show()
                } else if (state == 0) {
                    if (mCallback != null)
                        mCallback?.invoke(false)
                    else
                        Toast.makeText(context, "拔出耳机", Toast.LENGTH_SHORT).show()
                }
            }
        } else if (BluetoothHeadset.ACTION_CONNECTION_STATE_CHANGED.equals(action)) {
            val adapter = BluetoothAdapter.getDefaultAdapter();
            if (BluetoothProfile.STATE_DISCONNECTED == adapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                //isHeadset--; //Bluetooth headset is now disconnected
                if (mCallback != null)
                    mCallback?.invoke(false)
                else
                    Toast.makeText(context, "拔出耳机", Toast.LENGTH_SHORT).show()
            } else {
                //isHeadset++;
                if (mCallback != null)
                    mCallback?.invoke(true)
                else
                    Toast.makeText(context, "插入耳机", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
}
