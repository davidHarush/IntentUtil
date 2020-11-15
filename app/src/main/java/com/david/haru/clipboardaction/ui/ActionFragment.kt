package com.david.haru.clipboardaction.ui

import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.david.haru.clipboardaction.R
import com.david.haru.intentUtil.CustomIntents
import com.david.haru.intentUtil.IntentUtil
import com.david.haru.myextensions.showToast


/**
 * Created by David Harush
 * On 10/11/2020.
 */
class ActionFragment : PreferenceFragmentCompat(), IntentUtil.CallBack {

    private lateinit var textData: String


    companion object {
        const val ARG_TEXT_DATA = "arg_text_data"
        fun newInstance(textData: String): ActionFragment {
            val args = Bundle()
            args.putString(ARG_TEXT_DATA, textData)
            val fragment = ActionFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)

        if (!this::textData.isInitialized) {
            textData =
                arguments?.getString(ARG_TEXT_DATA, getString(R.string.no_data_found)) ?: getString(
                    R.string.no_data_found
                )
        }
        setMessagesActions()
        setEmailAction()
        setGoogleSearch()


    }

    private fun setGoogleSearch() {
        findAction(R.string.google_search)?.setOnPreferenceClickListener {
            IntentUtil(CustomIntents.GoogleSearch(textData)).start(this)
            true
        }
    }

    private fun setMessagesActions() {
        if (isValidPhoneNumber(textData)) {
            findAction(R.string.send_sms)?.isEnabled = true
            findAction(R.string.send_whatsApp)?.isEnabled = true

            findAction(R.string.send_whatsApp)?.setOnPreferenceClickListener {
                showToast(textData)
                IntentUtil(CustomIntents.WhatsAppNumber(textData)).start(this)
                true
            }
            findAction(R.string.send_sms)?.setOnPreferenceClickListener {
                IntentUtil(CustomIntents.Sms(textData, msg = textData)).start(this)
                true
            }
        } else {
            findAction(R.string.send_sms)?.isEnabled = false
            findAction(R.string.send_whatsApp)?.isEnabled = false
        }
    }

    private fun setEmailAction() {
        if (isValidEmail(textData)) {
            findAction(R.string.send_email)?.isEnabled = true
            findAction(R.string.send_email)?.setOnPreferenceClickListener {
                IntentUtil(CustomIntents.Email(address = textData)).start(this)
                true
            }
        } else {
            findAction(R.string.send_email)?.isEnabled = false
        }

    }

    private fun findAction(@StringRes id: Int): Preference? = findPreference(getString(id))

    private fun isValidEmail(email: String) =
        email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPhoneNumber(number: String) =
        PhoneNumberUtils.isGlobalPhoneNumber(number)

    override fun onFail(e: Exception) {

        Log.e("onFail", "onFail", e)
    }
}


