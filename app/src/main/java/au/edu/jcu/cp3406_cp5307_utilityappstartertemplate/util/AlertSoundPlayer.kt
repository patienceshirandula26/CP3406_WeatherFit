package au.edu.jcu.cp3406_cp5307_utilityappstartertemplate.util

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

/**
 * Plays a short alert tone for severe weather (e.g. thunderstorms) when the
 * "Severe weather sound alert" setting is on. Uses ToneGenerator instead of
 * a bundled audio file, so no extra assets are needed.
 */
object AlertSoundPlayer {
    fun playSevereWeatherAlert() {
        val toneGenerator = ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)
        toneGenerator.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 600)
        Handler(Looper.getMainLooper()).postDelayed({ toneGenerator.release() }, 700)
    }
}