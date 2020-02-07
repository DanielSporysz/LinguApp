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
import pl.ourdomain.tlumaczenia.databinding.FragmentLoginBinding
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )

        binding.loginButton.setOnClickListener { view: View ->
            validateCredentials(view)
        }

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

    fun validateCredentials(view: View){
        val sessionManager = SessionManager(context)
        try {
            sessionManager.useCredentials(
                binding.usernameField.text.toString(),
                binding.passwordField.text.toString()
            )

            // if no exception was thrown, move to next window
            view.findNavController().navigate(R.id.action_login_to_menuMain)
        } catch (e: Exception){
            //TODO remove this print
            println("Invalid credentials"
                    + " " + binding.usernameField.text.toString()
                    + " " + binding.passwordField.text.toString())
            //TODO feedback to user about the error
        }

    }
}
