package pl.ourdomain.tlumaczenia.controllers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentWelcomeBinding

/**
 * A simple [Fragment] subclass.
 */
class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentWelcomeBinding>(
            inflater,
            R.layout.fragment_welcome, container, false
        )

        binding.buttonRegister.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }

        binding.buttonLogin.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_welcomeFragment_to_login)
        }

        return binding.root
    }


}
