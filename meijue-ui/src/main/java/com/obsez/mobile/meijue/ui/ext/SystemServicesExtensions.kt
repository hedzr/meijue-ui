@file:Suppress("NOTHING_TO_INLINE")

package com.obsez.mobile.meijue.ui.ext


import android.accounts.AccountManager
import android.app.*
import android.app.admin.DevicePolicyManager
import android.app.job.JobScheduler
import android.app.usage.NetworkStatsManager
import android.app.usage.UsageStatsManager
import android.appwidget.AppWidgetManager
import android.bluetooth.BluetoothManager
import android.content.ClipboardManager
import android.content.Context
import android.content.RestrictionsManager
import android.content.pm.LauncherApps
import android.hardware.ConsumerIrManager
import android.hardware.SensorManager
import android.hardware.camera2.CameraManager
import android.hardware.display.DisplayManager
import android.hardware.fingerprint.FingerprintManager
import android.hardware.input.InputManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.media.AudioManager
import android.media.MediaRouter
import android.media.midi.MidiManager
import android.media.projection.MediaProjectionManager
import android.media.session.MediaSessionManager
import android.media.tv.TvInputManager
import android.net.ConnectivityManager
import android.net.nsd.NsdManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.nfc.NfcManager
import android.os.*
import android.os.storage.StorageManager
import android.print.PrintManager
import android.service.wallpaper.WallpaperService
import android.telecom.TelecomManager
import android.telephony.CarrierConfigManager
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.view.accessibility.CaptioningManager
import android.view.inputmethod.InputMethodManager
import android.view.textservice.TextServicesManager
import androidx.annotation.RequiresApi

inline fun Context.getActivityManager() = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
inline fun Context.getAlarmManager() = getSystemService(Context.ALARM_SERVICE) as AlarmManager
inline fun Context.getAudioManager() = getSystemService(Context.AUDIO_SERVICE) as AudioManager
inline fun Context.getClipboardManager() = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
inline fun Context.getConnectivityManager() = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
inline fun Context.getKeyguardManager() = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
inline fun Context.getLayoutInflater() = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
inline fun Context.getLocationManager() = getSystemService(Context.LOCATION_SERVICE) as LocationManager
inline fun Context.getNotificationManager() = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
inline fun Context.getPowerManager() = getSystemService(Context.POWER_SERVICE) as PowerManager
inline fun Context.getSearchManager() = getSystemService(Context.SEARCH_SERVICE) as SearchManager
inline fun Context.getSensorManager() = getSystemService(Context.SENSOR_SERVICE) as SensorManager
inline fun Context.getTelephonyManager() = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
inline fun Context.getVibrator() = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
inline fun Context.getWallpaperService() = getSystemService(Context.WALLPAPER_SERVICE) as WallpaperService
inline fun Context.getWifiManager() = getSystemService(Context.WIFI_SERVICE) as WifiManager
inline fun Context.getWindowManager() = getSystemService(Context.WINDOW_SERVICE) as WindowManager
inline fun Context.getInputMethodManager() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
inline fun Context.getAccessibilityManager() = getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
inline fun Context.getAccountManager() = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
inline fun Context.getDevicePolicyManager() = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
inline fun Context.getDropBoxManager() = getSystemService(Context.DROPBOX_SERVICE) as DropBoxManager
inline fun Context.getUiModeManager() = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
inline fun Context.getDownloadManager() = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
inline fun Context.getStorageManager() = getSystemService(Context.STORAGE_SERVICE) as StorageManager
inline fun Context.getNfcManager() = getSystemService(Context.NFC_SERVICE) as NfcManager
inline fun Context.getUsbManager() = getSystemService(Context.USB_SERVICE) as UsbManager
inline fun Context.getTextServicesManager() = getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE) as TextServicesManager
inline fun Context.getWifiP2pManager() = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
inline fun Context.getInputManager() = getSystemService(Context.INPUT_SERVICE) as InputManager
inline fun Context.getMediaRouter() = getSystemService(Context.MEDIA_ROUTER_SERVICE) as MediaRouter
inline fun Context.getNsdManager() = getSystemService(Context.NSD_SERVICE) as NsdManager
inline fun Context.getDisplayManager() = getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
inline fun Context.getUserManager() = getSystemService(Context.USER_SERVICE) as UserManager
inline fun Context.getBluetoothManager() = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
inline fun Context.getAppOpsManager() = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
inline fun Context.getCaptioningManager() = getSystemService(Context.CAPTIONING_SERVICE) as CaptioningManager
inline fun Context.getConsumerIrManager() = getSystemService(Context.CONSUMER_IR_SERVICE) as ConsumerIrManager
inline fun Context.getPrintManager() = getSystemService(Context.PRINT_SERVICE) as PrintManager
inline fun Context.getAppWidgetManager() = getSystemService(Context.APPWIDGET_SERVICE) as AppWidgetManager
inline fun Context.getBatteryManager() = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
inline fun Context.getCameraManager() = getSystemService(Context.CAMERA_SERVICE) as CameraManager
inline fun Context.getJobScheduler() = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
inline fun Context.getLauncherApps() = getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
inline fun Context.getMediaProjectionManager() = getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
inline fun Context.getMediaSessionManager() = getSystemService(Context.MEDIA_SESSION_SERVICE) as MediaSessionManager
inline fun Context.getRestrictionsManager() = getSystemService(Context.RESTRICTIONS_SERVICE) as RestrictionsManager
inline fun Context.getTelecomManager() = getSystemService(Context.TELECOM_SERVICE) as TelecomManager
inline fun Context.getTvInputManager() = getSystemService(Context.TV_INPUT_SERVICE) as TvInputManager
@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
inline fun Context.getSubscriptionManager() = getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager

@RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
inline fun Context.getUsageStatsManager() = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager

@RequiresApi(Build.VERSION_CODES.M)
inline fun Context.getCarrierConfigManager() = getSystemService(Context.CARRIER_CONFIG_SERVICE) as CarrierConfigManager

@RequiresApi(Build.VERSION_CODES.M)
inline fun Context.getFingerprintManager() = getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

@RequiresApi(Build.VERSION_CODES.M)
inline fun Context.getMidiManager() = getSystemService(Context.MIDI_SERVICE) as MidiManager

@RequiresApi(Build.VERSION_CODES.M)
inline fun Context.getNetworkStatsManager() = getSystemService(Context.NETWORK_STATS_SERVICE) as NetworkStatsManager
