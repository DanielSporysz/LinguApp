package pl.ourdomain.tlumaczenia.controllers


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.databinding.FragmentLoginBinding


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

    private fun validateCredentials(view: View) {
        binding.loginButton.isEnabled = false
        binding.loginButton.setBackgroundResource(R.drawable.rounded_disabled_button)

        GlobalScope.launch {
            //TODO remove delay
            delay(1000)

            // Try getting an auth token
            val sessionManager = SessionManager(context)
            var isCorrect = true
            try {
                sessionManager.useCredentialsAndFetchAuthToken(
                    binding.usernameField.text.toString(),
                    binding.passwordField.text.toString()
                )
            } catch (e: Exception) {
                isCorrect = false
            }

            // Feedback to user & navigation
            Handler(myContext.mainLooper).post {
                //Enable the login button
                binding.loginButton.isEnabled = true
                binding.loginButton.setBackgroundResource(R.drawable.rounded_button)

                if (isCorrect) {
                    displayToast("Token: " + SessionManager.authToken)
                    view.findNavController().navigate(R.id.action_login_to_menuMain)
                } else {
                    displayToast("Incorrect credentials")
                }
            }
        }
    }

    private fun displayToast(msg: String?) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
