package pl.ourdomain.tlumaczenia


import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import pl.ourdomain.tlumaczenia.databinding.FragmentQuickTranslationBinding


/**
 * A simple [Fragment] subclass.
 */
class QuickTranslation : Fragment() {

    private lateinit var binding: FragmentQuickTranslationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentQuickTranslationBinding>(
            inflater,
            R.layout.fragment_quick_translation, container, false
        )

        //TODO initializing spinner
        /*if (isAdded) {
            val originalLangSpinner = binding.originalLangSpinner
            ArrayAdapter.createFromResource(
                context,
                R.array.supported_languages,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                originalLangSpinner.adapter = adapter
            }

        }*/

        return binding.root
    }


}
