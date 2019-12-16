package pl.ourdomain.tlumaczenia


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pl.ourdomain.tlumaczenia.databinding.FragmentMenuMainBinding
import pl.ourdomain.tlumaczenia.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass.
 */
class MenuMain : Fragment() {

    private lateinit var binding: FragmentMenuMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentMenuMainBinding>(
            inflater,
            R.layout.fragment_menu_main, container, false
        )

        return binding.root
    }


}
