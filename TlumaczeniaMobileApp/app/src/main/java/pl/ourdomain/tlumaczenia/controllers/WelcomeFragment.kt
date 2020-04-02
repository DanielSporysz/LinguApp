package pl.ourdomain.tlumaczenia.controllers


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.databinding.FragmentWelcomeBinding


class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

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

    override fun onStart() {
        super.onStart()
        if (checkIfAlreadyLoggedIn()) {
            // Navigate to Menu Activity if logged in
            val intent = Intent(myContext, Menu::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            activity?.finish()
            myContext.startActivity(intent)
        }
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

    private fun checkIfAlreadyLoggedIn(): Boolean {
        val sessionManager = SessionManager(myContext)
        return sessionManager.isLoggedIn()
    }
}
