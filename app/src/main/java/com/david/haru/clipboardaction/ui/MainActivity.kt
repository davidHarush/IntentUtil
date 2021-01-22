package com.david.haru.clipboardaction.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.david.haru.clipboardaction.App
import com.david.haru.clipboardaction.R
import com.david.haru.myextensions.getBaseApp
import com.david.haru.myextensions.showToast


class MainActivity : AppCompatActivity(), ActionFragment.ActionFragmentListener {

    var selectedText = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (intent.action == Intent.ACTION_PROCESS_TEXT) {
            selectedText = if(!intent.hasExtra(Intent.EXTRA_PROCESS_TEXT)) {
                intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
            } else {
                getString(R.string.no_data_found)
            }
        }
        init(selectedText)
    }

    private fun init(selectedText: String = "") {
        if (selectedText.trim().isBlank()) {
            showFragment(EmptyFragment())
        } else {
            showFragment(ActionFragment.newInstance(selectedText))
            setTitle(selectedText)
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, fragment)
                .commit()
    }

    override fun setTitle(title: String) {
        //todo delete if not needed
        selectedText = title
        findViewById<TextView>(R.id.tvTitle).text = title
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
