package com.dnavarro.espectro.services
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import com.dnavarro.espectro.renderer.GLES20Renderer
import android.Manifest
import com.dnavarro.espectro.Constants
import com.dnavarro.espectro.R

class LWPService : OpenGLES2WallpaperService() {
    override fun onCreateEngine(): Engine {
        return EspectroEngine()
    }
    inner class EspectroEngine : GLEngine(), SharedPreferences.OnSharedPreferenceChangeListener {
        private var renderer: GLES20Renderer? = null
        private lateinit var prefs: SharedPreferences
        private var currentTheme: String = Constants.THEME_ICE

        override fun onCreate(surfaceHolder: android.view.SurfaceHolder?) {
            super.onCreate(surfaceHolder)

            // Use applicationContext to share prefs correctly with Activity
            prefs = applicationContext.getSharedPreferences(Constants.PRENS_NAME, Context.MODE_PRIVATE)
            prefs.registerOnSharedPreferenceChangeListener(this)

            renderer = GLES20Renderer(this@LWPService)

            // Set initial texture
            currentTheme = prefs.getString(Constants.PREF_THEME, Constants.THEME_ICE) ?: Constants.THEME_ICE
            val resId = if (currentTheme == Constants.THEME_FIRE) R.drawable.fire else R.drawable.ice
            renderer!!.mCurrentTextureResId = resId

            setRenderer(renderer!!)
            // Initial check for audio
            checkAudioPermission()
        }

        override fun onDestroy() {
            super.onDestroy()
            prefs.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
            if (key == Constants.PREF_THEME) {
                checkAndUpdateTheme()
            }
        }

        override fun onVisibilityChanged(visible: Boolean) {
            super.onVisibilityChanged(visible)
            renderer?.setVisible(visible)
            if (visible) {
                 checkAudioPermission()
                 checkAndUpdateTheme()
            }
        }

        private fun checkAndUpdateTheme() {
            val newTheme = prefs.getString(Constants.PREF_THEME, Constants.THEME_ICE) ?: Constants.THEME_ICE
            if (newTheme != currentTheme) {
                currentTheme = newTheme
                val resId = if (newTheme == Constants.THEME_FIRE) R.drawable.fire else R.drawable.ice
                queueEvent {
                    renderer?.updateTexture(resId)
                }
            }
        }

        private fun checkAudioPermission() {
             var hasPermission = false
             if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                 hasPermission = true
             }
             renderer?.setAudioEnabled(hasPermission)
        }
    }
}
