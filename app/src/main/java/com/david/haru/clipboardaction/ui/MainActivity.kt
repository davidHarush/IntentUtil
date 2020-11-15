package com.david.haru.clipboardaction.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.david.haru.clipboardaction.App
import com.david.haru.clipboardaction.R
import com.david.haru.myextensions.getBaseApp
import com.david.haru.myextensions.showToast


class MainActivity : AppCompatActivity() {

    var selectedText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (intent.action == Intent.ACTION_PROCESS_TEXT) {
            selectedText = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT) ?: getString(R.string.no_data_found)
        }
        init(selectedText)
    }

    private fun init(selectedText: String = "") {
        if (selectedText.trim().isBlank()) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, EmptyFragment())
                .commit()
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, ActionFragment.newInstance(selectedText))
                .commit()
            findViewById<TextView>(R.id.tvTitle).text = selectedText
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 55) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$selectedText"))
                startActivity(callIntent)
            } else {
                showToast("Permission DENIED")
            }
        }
    }

}