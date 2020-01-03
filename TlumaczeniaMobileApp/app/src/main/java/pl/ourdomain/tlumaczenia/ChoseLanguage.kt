package pl.ourdomain.tlumaczenia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pl.ourdomain.tlumaczenia.databinding.FragmentChoseLanguageBinding

class ChoseLanguage : Fragment() {
    private lateinit var binding: FragmentChoseLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentChoseLanguageBinding>(
            inflater,
            R.layout.fragment_chose_language, container, false
        )

        //TODO passing which language has been choose to next fragment
        binding.englishButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_choseLanguage_to_learningMethods)
        }
        binding.spainButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_choseLanguage_to_learningMethods)
        }
        binding.russianButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_choseLanguage_to_learningMethods)
        }

        return binding.root
    }
}
