package pl.ourdomain.tlumaczenia.controllers

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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentQuickTranslationBinding
import java.lang.Exception

class QuickTranslationFragment : Fragment() {

    private lateinit var binding: FragmentQuickTranslationBinding
    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private lateinit var supportedLanguages: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuickTranslationBinding.inflate(inflater, container, false)

        binding.translateButton.setOnClickListener { view: View ->
            translate(view)
        }

        initLangSpinner()

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

    private fun validateFields(): Pair<Boolean, String?> {
        return if (binding.originalText.text.isBlank()
        ) {
            Pair(false, getString(R.string.toast_fill_all_fields))
        } else {
            Pair(true, null)
        }
    }

    private fun translate(view: View) {
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

        GlobalScope.launch {
            // Get translation and display it
            try {
                val original = binding.originalText.text.toString()
                val source_lang = binding.langSpinner.selectedItem.toString()

                val translated = API.translate(original)

                binding.translatedText.text = translated
            } catch (e: Exception) {
                e.printStackTrace()

                binding.translatedText.text = ""
                displayToast(getString(R.string.toast_translation_error), Toast.LENGTH_LONG)
            }

            // Enable translation button with delay to prevent flooding of requests
            delay(250)
            Handler(myContext.mainLooper).post {
                enableTranslateButton()
            }
        }
    }

    private fun initLangSpinner() {
        //TODO getting it from the server
        GlobalScope.launch {
            try {
                val data = API.fetchSupportedLanguages()
                print(data)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        supportedLanguages = mutableListOf("angielski", "polski", "hiszpański")
        supportedLanguages.remove("polski")

        val myAdapter = ArrayAdapter(
            myContext,
            android.R.layout.simple_spinner_item,
            supportedLanguages
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
