package com.david.haru.intentUtil

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.fragment.app.Fragment

/**
 * Created by David Harush
 * On 10/11/2020.
 */

class IntentUtil(private val simpleIntent: SimpleIntent, private val callBack: CallBack? = null) {

    interface CallBack {
        fun onFail(e: Exception)
    }



    fun start(fragment: Fragment) {
        if (simpleIntent.isEmptyIntent()){
            callBack?.onFail(IllegalArgumentException("Empty Intent"))
            return
        }
        try {
            fragment.startActivity(simpleIntent.intent)
        } catch (e: Exception) {
            callBack?.onFail(e)
        }
    }

    fun start(activity: Activity) {
        if (simpleIntent.isEmptyIntent()){
            callBack?.onFail(IllegalArgumentException("Empty Intent"))
            return
        }
        try {
            activity.startActivity(
                simpleIntent.intent
            )
        } catch (e: Exception) {
            callBack?.onFail(e)
        }
    }

    fun startForResult(fragment: Fragment, requestCode: Int = 0) {
        if (simpleIntent.isEmptyIntent()){
            callBack?.onFail(IllegalArgumentException("Empty Intent"))
            return
        }
        try {
            fragment.startActivityForResult(
                simpleIntent.intent
                , requestCode
            )
        } catch (e: Exception) {
            callBack?.onFail(e)
        }
    }

    fun startForResult(activity: Activity, requestCode: Int = 0) {
        if (simpleIntent.isEmptyIntent()){
            callBack?.onFail(IllegalArgumentException("Empty Intent"))
            return
        }
        try {
            activity.startActivityForResult(
                simpleIntent.intent
                , requestCode
            )
        } catch (e: Exception) {
            callBack?.onFail(e)
        }
    }

    companion object {
        fun copyToClipboard(ctx: Context?, text: CharSequence) {
            ctx?.copyToClipboard(text)
        }
    }
}


fun Context.copyToClipboard(text: CharSequence){
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label",text)
    clipboard.setPrimaryClip(clip)
}