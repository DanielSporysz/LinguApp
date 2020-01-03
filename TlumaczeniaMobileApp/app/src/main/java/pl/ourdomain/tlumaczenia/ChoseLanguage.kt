package pl.ourdomain.tlumaczenia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

        return binding.root
    }
}
