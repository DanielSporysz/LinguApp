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
import pl.ourdomain.tlumaczenia.databinding.FragmentQuickTranslationBinding
import pl.ourdomain.tlumaczenia.dataclasses.Language

class QuickTranslationFragment : Fragment() {

    private lateinit var binding: FragmentQuickTranslationBinding
    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var supportedLanguages: List<Language>? = null

    private var invertedTranslation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuickTranslationBinding.inflate(inflater, container, false)

        binding.translateButton.setOnClickListener {
            translate()
        }
        binding.spinnersScene.swapArrows.setOnClickListener {
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

        return binding.root
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

    override fun onResume() {
        super.onResume()

        // When the screen has been rotated
        synchronizePositions()
    }

    private fun validateFields(): Pair<Boolean, String?> {
        return if (binding.srcText.text.isBlank()
        ) {
            Pair(false, getString(R.string.toast_fill_all_fields))
        } else {
            Pair(true, null)
        }
    }

    private fun synchronizePositions(){
        if(invertedTranslation){
            swapTranslation(0)
        }
    }

    private fun swapTranslation(duration: Long) {
        invertedTranslation = !invertedTranslation

        // Calculate translation
        val spinnerLoc = binding.spinnersScene.langSpinner.left
        val textLoc = binding.spinnersScene.langText.left
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
            binding.spinnersScene.langSpinner,
            "translationX",
            deltaSpinner, duration
        )

        // Animate text
        animate(
            binding.spinnersScene.langText,
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
                Handler(myContext.mainLooper).post {
                    enableTranslateButton()
                }
            }
            return
        }

        val text = binding.srcText.text.toString()
        var srcLang =
            binding.spinnersScene.langSpinner.selectedItem.toString().let { getShortLangName(it) }
        // WE ONLY TRANSLATE FROM/TO polish language
        var dstLang = "pl"

        // Swap if translation languages is inverted
        if (invertedTranslation) {
            srcLang = dstLang.also { dstLang = srcLang }
        }

        GlobalScope.launch {
            try {
                // Get translation
                val translated = API.translate(text, srcLang, dstLang)

                // Display translation
                binding.dstText.text = translated
            } catch (e: Exception) {
                Log.e("TRANSLATE", e.toString(), e)

                binding.dstText.text = ""
                Handler(myContext.mainLooper).post {
                    displayToast(getString(R.string.toast_translation_error), Toast.LENGTH_LONG)
                }
            }

            // Enable translation button with delay to prevent flooding of requests
            delay(250)
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
            supportedLanguages = API.fetchSupportedLanguages()
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

        binding.spinnersScene.langSpinner.adapter = myAdapter
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
