package com.david.haru.intentUtil

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.provider.Settings
import android.webkit.URLUtil
import androidx.annotation.RequiresApi

/**
 * Created by David Harush
 * On 12/11/2020.
 */


abstract class SimpleIntent {
    open val intent: Intent = Intent(emptyAction)

    companion object {
        var emptyAction: String? = null
    }

    fun isEmptyIntent() = (
            intent.action == emptyAction
            )
}

class CustomIntents {

    //=========================
    class Custom(private val action: String, private val dataUri: Uri = Uri.EMPTY) :
        SimpleIntent() {
        override val intent: Intent
            get() = Intent(action).apply {
                if (dataUri != Uri.EMPTY) {
                    data = dataUri
                }
            }
    }

    //=========================
    class PackageName(
        private val packageName: String = "", private val context: Context
    ) : SimpleIntent() {

        override val intent: Intent
            get() = context.packageManager.getLeanbackLaunchIntentForPackage(packageName) ?: Intent(
                emptyAction
            )
    }

    //=========================
    class Email(
        private val address: String = "",
        private val subject: String = "",
        private val body: String = ""
    ) : SimpleIntent() {

        override val intent: Intent
            get() = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, body)
            }
    }

    //=========================
    class WhatsAppNumber(
        private val number: String = "" ) : SimpleIntent() {
        override val intent: Intent
            get() = Intent(Intent.ACTION_SENDTO).apply {
                setPackage("com.whatsapp")
                data = Uri.parse("smsto:$number")
            }
    }

    //=========================
    class Sms(private val number: String = "", private val msg: String = "") : SimpleIntent() {
        override val intent: Intent
            get() = Intent().apply {
                action = Intent.ACTION_VIEW
                type = "vnd.android-dir/mms-sms"
                putExtra("address", number)
                putExtra("sms_body", msg)
            }
    }

    //=========================
    class GoogleSearch(private val query: String = "") : SimpleIntent() {
        override val intent: Intent
            get() = Intent().apply {
                action = Intent.ACTION_WEB_SEARCH
                putExtra(SearchManager.QUERY, query)
            }
    }

    //=========================
    class SimpleShare(private val msg: String = "") : SimpleIntent() {
        override val intent: Intent
            get() = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, msg)
            }
    }


    //=========================
    class AddToContacts(private val phone: String = "", private val email: String = "") : SimpleIntent() {
        override val intent: Intent
            get() = Intent().apply {
                if(email.isNotEmpty()) {
                    putExtra(ContactsContract.Intents.Insert.EMAIL, email)
                    putExtra(ContactsContract.Intents.Insert.EMAIL_TYPE,
                        ContactsContract.CommonDataKinds.Email.TYPE_WORK
                    )
                }
                if(phone.isNotEmpty()) {
                    putExtra(ContactsContract.Intents.Insert.PHONE, phone)
                    putExtra(ContactsContract.Intents.Insert.PHONE_TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                }

                action = ContactsContract.Intents.Insert.ACTION
                type = ContactsContract.RawContacts.CONTENT_TYPE

//                putExtra(Intent.EXTRA_TEXT, phone) //todo remove?
            }
    }
    //=========================
    class OpenUrlInBrowser(private val url: String = "") : SimpleIntent() {
        override val intent: Intent
            get() =
                if (URLUtil.isValidUrl(url)) {
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(url)
                    }
                } else {
                    Intent(emptyAction)
                }
    }

    //=========================
    class PhoneCall(private val number: String = "", private val context: Context) :
        SimpleIntent() {
        val dial = "tel:$number"
        override val intent: Intent
            get() = if (context.checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse(dial)
                }
            } else {
                Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse(dial)
                }
            }
    }

    //=========================
    @RequiresApi(Build.VERSION_CODES.Q)
    class SettingPanelConnectivity : SimpleIntent() {
        override val intent: Intent
            get() = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
    }

    //=========================
    @RequiresApi(Build.VERSION_CODES.Q)
    class SettingPanelWifi : SimpleIntent() {
        override val intent: Intent
            get() = Intent(Settings.Panel.ACTION_WIFI)
    }

    //=========================
    @RequiresApi(Build.VERSION_CODES.Q)
    class SettingPanelVolume : SimpleIntent() {
        override val intent: Intent
            get() = Intent(Settings.Panel.ACTION_VOLUME)
    }

    //=========================
    @RequiresApi(Build.VERSION_CODES.Q)
    class SettingPanelNfc : SimpleIntent() {
        override val intent: Intent
            get() = Intent(Settings.Panel.ACTION_NFC)
    }
    //=========================
}
