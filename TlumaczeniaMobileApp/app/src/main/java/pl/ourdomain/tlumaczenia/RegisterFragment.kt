package pl.ourdomain.tlumaczenia


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pl.ourdomain.tlumaczenia.databinding.FragmentRegisterBinding
import pl.ourdomain.tlumaczenia.databinding.FragmentWelcomeBinding

/**
 * A simple [Fragment] subclass.
 */
class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val registerInfo: RegisterInfo = RegisterInfo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater,
            R.layout.fragment_register, container, false
        )

        binding.registerInfo = registerInfo

        return binding.root
    }

}
