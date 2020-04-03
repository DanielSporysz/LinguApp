package pl.ourdomain.tlumaczenia.controllers


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.databinding.FragmentMenuMainBinding

class MainMenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuMainBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentMenuMainBinding>(
            inflater,
            R.layout.fragment_menu_main, container, false
        )

        initView()

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

    private fun initView(){
        binding.translateWordsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_menuMain_to_quickTranslation)
        }
        binding.savedWordsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_menuMain_to_savedWordsFragment)
        }
        binding.learnButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_menuMain_to_choseLanguage)
        }
    }

    private fun logout() {
        val sessionManager = SessionManager(myContext)
        sessionManager.logout()
    }
}
