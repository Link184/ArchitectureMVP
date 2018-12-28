package com.link184.architecture.mvp

import android.app.Activity
import android.content.Intent

/**
 * Designed to avoid ugly onActivityResult handling. No more if else if else.
 */
sealed class ActivityResultState(val requestCode: Int, val resultCode: Int, val data: Intent?) {
    class Ok(requestCode: Int, data: Intent?) : ActivityResultState(requestCode, Activity.RESULT_OK, data)
    class Canceled(requestCode: Int, data: Intent?) : ActivityResultState(requestCode, Activity.RESULT_CANCELED, data)
    class FirstUser(requestCode: Int, data: Intent?) : ActivityResultState(requestCode, Activity.RESULT_FIRST_USER, data)
    class Unknown(requestCode: Int, resultCode: Int, data: Intent?) : ActivityResultState(requestCode, resultCode, data)

    override fun toString(): String {
        return "ActivityResultState(requestCode=$requestCode, resultCode=$resultCode, data=$data)"
    }
}

internal fun resolveActivityState(requestCode: Int, resultCode: Int, data: Intent?): ActivityResultState =
        when (resultCode) {
            Activity.RESULT_OK -> ActivityResultState.Ok(requestCode, data)
            Activity.RESULT_CANCELED -> ActivityResultState.Canceled(requestCode, data)
            Activity.RESULT_FIRST_USER -> ActivityResultState.FirstUser(requestCode, data)
            else -> ActivityResultState.Unknown(requestCode, resultCode, data)
        }
