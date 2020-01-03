package pl.ourdomain.tlumaczenia.controllers

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentQuickTranslationBinding

class QuickTranslationFragment : Fragment() {

    private lateinit var binding: FragmentQuickTranslationBinding
    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuickTranslationBinding.inflate(inflater, container, false)

        initLangSpinners()

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

    fun initLangSpinners() {
        val originalLangSpinner = binding.originalLangSpinner
        val targetLangSpinner = binding.targetLangSpinner

        ArrayAdapter.createFromResource(
            myContext,
            R.array.supported_languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            originalLangSpinner.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            myContext,
            R.array.supported_languages,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            targetLangSpinner.adapter = adapter
        }
    }
}
