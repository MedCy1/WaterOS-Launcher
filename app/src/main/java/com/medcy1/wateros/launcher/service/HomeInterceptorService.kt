package com.medcy1.wateros.launcher.service

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.accessibility.AccessibilityEvent
import com.medcy1.wateros.launcher.MainActivity

class HomeInterceptorService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // No-op for window state inspection
    }

    override fun onInterrupt() {
        Log.w(TAG, "Accessibility service interrupted")
    }

    override fun onKeyEvent(event: KeyEvent?): Boolean {
        if (event == null) return false

        // Intercept remote physical Home key (code 3)
        if (event.keyCode == KeyEvent.KEYCODE_HOME && event.action == KeyEvent.ACTION_UP) {
            Log.i(TAG, "Intercepted hardware Home key press, routing to WaterOS Launcher")
            routeToWaterOSHome()
            return true
        }

        return super.onKeyEvent(event)
    }

    private fun routeToWaterOSHome() {
        val homeIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
        startActivity(homeIntent)
    }

    companion object {
        private const val TAG = "WaterOS_HomeService"
    }
}
