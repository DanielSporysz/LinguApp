package pl.ourdomain.tlumaczenia.controllers

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.NonCancellable.start
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.databinding.FragmentTranslatorBinding
import pl.ourdomain.tlumaczenia.dataclasses.Language

class TranslatorFragment : Fragment() {

    private lateinit var binding: FragmentTranslatorBinding
    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var supportedLanguages: List<Language>? = null

    private var invertedTranslation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTranslatorBinding.inflate(inflater, container, false)

        binding.translateButton.setOnClickListener {
            translate()
        }
        binding.swapArrows.setOnClickListener {
            swapTranslation(500L)
        }

        // get supported languages from server and init spinner
        disableTranslateButton()
        GlobalScope.launch {
            fetchSupportedLanguages()

            Handler(myContext.mainLooper).post {
                if (supportedLanguages != null) {
                    initLangSpinner()
                    enableTranslateButton()
                }
            }
        }

        // Restore fragment
        if (savedInstanceState != null) {
            Log.i("TRANSLATION", "Restoring fragment")
            invertedTranslation = savedInstanceState.getBoolean("invertedTranslation")
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("invertedTranslation", invertedTranslation)
    }

    override fun onResume() {
        super.onResume()

        Log.i("TRANSLATION", "Resuming..")
        // TODO a better way of UI updating after rotation
        GlobalScope.launch {
            delay(250)
            Handler(myContext.mainLooper).post {
                synchronizePositions()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        myContext = context
        isAttached = true
    }

    override fun onDetach() {
        super.onDetach()
        isAttached = false
    }

    private fun synchronizePositions() {
        // check if translation has been inverted
        if (invertedTranslation) {
            // negate swapTranslation function call
            invertedTranslation = !invertedTranslation
            // synchronize UI with the state
            swapTranslation(0)
        }
    }

    private fun swapTranslation(duration: Long) {
        invertedTranslation = !invertedTranslation

        // Calculate translation
        val spinnerLoc = binding.langSpinner.left
        val textLoc = binding.langText.left
        val deltaSpinner: Float
        val deltaText: Float
        if (invertedTranslation) {
            deltaSpinner = (textLoc - spinnerLoc).toFloat()
            deltaText = (spinnerLoc - textLoc).toFloat()
        } else {
            deltaSpinner = 0f
            deltaText = 0f
        }

        // Animate spinner
        animate(
            binding.langSpinner,
            "translationX",
            deltaSpinner, duration
        )

        // Animate text
        animate(
            binding.langText,
            "translationX",
            deltaText, duration
        )
    }

    private fun animate(view: View, property: String, distance: Float, receivedDuration: Long) {
        ObjectAnimator.ofFloat(
            view, property, distance
        )
            .apply {
                duration = receivedDuration
                start()
            }
    }

    private fun validateFields(): Pair<Boolean, String?> {
        return if (binding.srcText.text.isBlank()
        ) {
            Pair(false, getString(R.string.toast_fill_all_fields))
        } else {
            Pair(true, null)
        }
    }

    private fun translate() {
        // Disable translate button for the time of handling the request
        disableTranslateButton()

        // Validate fields
        val (areFieldsValid, errorMessage) = validateFields()
        if (!areFieldsValid) {
            displayToast(errorMessage, Toast.LENGTH_SHORT)

            // delay enabling the button
            GlobalScope.launch {
                delay(2000)

                // User could leave the fragment by this time
                if(!isAttached){
                    return@launch
                }

                Handler(myContext.mainLooper).post {
                    enableTranslateButton()
                }
            }
            return
        }

        val text = binding.srcText.text.toString()
        var srcLang =
            binding.langSpinner.selectedItem.toString().let { getShortLangName(it) }
        // WE ONLY TRANSLATE FROM/TO polish language
        var dstLang = "pl"

        // Swap if translation languages is inverted
        if (invertedTranslation) {
            srcLang = dstLang.also { dstLang = srcLang }
        }

        GlobalScope.launch {
            try {
                // Get translation
                val api = API(myContext)
                val translated = api.translate(text, srcLang, dstLang, SessionManager.authToken.toString())

                // Display translation
                binding.dstText.text = translated
            } catch (e: Exception) {
                Log.e("TRANSLATE", e.toString(), e)

                // User could leave the fragment by this time
                if(!isAttached){
                    return@launch
                }

                binding.dstText.text = ""
                Handler(myContext.mainLooper).post {
                    displayToast(getString(R.string.toast_translation_error), Toast.LENGTH_LONG)
                }
            }

            // Enable translation button with delay to prevent flooding of requests
            delay(250)

            // User could leave the fragment by this time
            if(!isAttached){
                return@launch
            }

            Handler(myContext.mainLooper).post {
                enableTranslateButton()
            }
        }
    }

    private fun getShortLangName(polishLangName: String): String {
        if (supportedLanguages == null) {
            throw Exception("Supported languages list is not initialized!")
        }

        // Find the language class
        for (lang in this.supportedLanguages!!) {
            if (lang.polishName == polishLangName) {
                return lang.shortName
            }
        }

        // If not found
        throw Exception("Short name was not found in the list!")
    }

    private fun fetchSupportedLanguages() {
        try {
            val api = API(myContext)
            supportedLanguages = api.fetchSupportedLanguages(SessionManager.authToken.toString())
        } catch (e: Exception) {
            Log.e("TRANSLATE", e.toString(), e)
        }
    }

    private fun initLangSpinner() {
        if (supportedLanguages == null) {
            return
        }

        // extract supported languages list from classes
        val spinnerLanguages = mutableListOf<String>()
        for (lang in supportedLanguages!!) {
            // WE ONLY TRANSLATE FROM/TO polish language
            if (lang.polishName != "polski") {
                spinnerLanguages.add(lang.polishName)
            }
        }

        // Setup spinner
        val myAdapter = ArrayAdapter(
            myContext,
            android.R.layout.simple_spinner_item,
            spinnerLanguages
        )
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.langSpinner.adapter = myAdapter
    }

    private fun disableTranslateButton() {
        binding.translateButton.isEnabled = false
        binding.translateButton.setBackgroundResource(R.drawable.rounded_disabled_button)
    }

    private fun enableTranslateButton() {
        binding.translateButton.isEnabled = true
        binding.translateButton.setBackgroundResource(R.drawable.rounded_button)
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
