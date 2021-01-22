package com.david.haru.clipboardaction.ui

import android.content.Context
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import androidx.annotation.StringRes
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.david.haru.clipboardaction.R
import com.david.haru.intentUtil.CustomIntents
import com.david.haru.intentUtil.IntentUtil
import com.david.haru.intentUtil.copyToClipboard
import com.david.haru.myextensions.getBaseAppContext
import com.david.haru.myextensions.showToast


/**
 * Created by David Harush
 * On 10/11/2020.
 */
class ActionFragment : PreferenceFragmentCompat(), IntentUtil.CallBack {

    interface ActionFragmentListener {
        fun setTitle(title: String)
    }

    private lateinit var textData: String
    private var listener: ActionFragmentListener? = null


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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ActionFragmentListener) {
            this.listener = context
        } else {
            throw RuntimeException("$context must implement ActionFragmentListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
        setCallAction()
        setUtmAction()
        setMoreShareAction()
        setBrowserAction()
        setCopyToClipboardAction()
        setAddContactAction()
    }

    private fun setAddContactAction() {
        val isPhoneNumber = isValidPhoneNumber(textData)
        val isEmailAddress = isValidEmail(textData)
        if (isPhoneNumber || isEmailAddress) {
            findAction(R.string.add_contact)?.isEnabled = true
            findAction(R.string.add_contact)?.setOnPreferenceClickListener {
                val customAddContactIntent = if(isPhoneNumber) {
                    CustomIntents.AddToContacts(phone = textData)
                } else {//if(isEmailAddress) {
                    CustomIntents.AddToContacts(email = textData)
                }
                IntentUtil(customAddContactIntent).start(this)
                true
            }
        } else {
            findAction(R.string.add_contact)?.isEnabled = false
        }
    }

    private fun setCopyToClipboardAction() {
        findAction(R.string.copy_to_clipboard)?.setOnPreferenceClickListener {
            copyTextToClipboard(textData)
            true
        }
    }

    private fun setBrowserAction() {
        if(isValidLink(textData)) {
            findAction(R.string.load_url)?.isEnabled = true
            findAction(R.string.load_url)?.setOnPreferenceClickListener {
                IntentUtil(CustomIntents.OpenUrlInBrowser(textData)).start(this)
                true
            }
        } else {
            findAction(R.string.load_url)?.isEnabled = false
        }
    }

    private fun setMoreShareAction() {
        findAction(R.string.more_share_options)?.setOnPreferenceClickListener {
            IntentUtil(CustomIntents.SimpleShare(textData)).start(this)
            true
        }
    }

    private fun setCallAction() {
        if (isValidPhoneNumber(textData)) {
            findAction(R.string.make_a_call)?.isEnabled = true

            findAction(R.string.make_a_call)?.setOnPreferenceClickListener {
                IntentUtil(CustomIntents.PhoneCall(textData, getBaseAppContext())).start(this)
                true
            }
        } else {
            findAction(R.string.make_a_call)?.isEnabled = false
        }
    }

    private fun setUtmAction() {
        if(isValidLink(textData) && textData.contains("?")) {
            findAction(R.string.remove_utm)?.isEnabled = true
            findAction(R.string.remove_utm)?.setOnPreferenceClickListener {
                val cleanUrl = if (textData.contains("?")) {
                    showToast("UTM removed")

                    val utmPrefixIndex = textData.indexOf("?")
                    textData.substring(0, utmPrefixIndex)
                } else {
                    showToast("No UTM found")

                    textData
                }
                textData = cleanUrl
                listener?.setTitle(textData)
                findAction(R.string.remove_utm)?.isEnabled = false

//                copyTextToClipboard(textData)     //todo bring back?
                true
            }
        } else {
            findAction(R.string.remove_utm)?.isEnabled = false
        }
    }

    private fun copyTextToClipboard(textToCopy: String) {
        showToast(getString(R.string.copied_to_clipboard))

        context?.copyToClipboard(textToCopy)
//   can also use the line below
//        IntentUtil.copyToClipboard(context, textToCopy)
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

    private fun isValidLink(url: String) =
            url.isNotEmpty() && url.startsWith("http")

    override fun onFail(e: Exception) {

        Log.e("onFail", "onFail", e)
    }
}


