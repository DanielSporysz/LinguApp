package pl.ourdomain.tlumaczenia.controllers


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.databinding.FragmentLoginBinding
import pl.ourdomain.tlumaczenia.exceptions.InvalidCredentials


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

        binding.loginButton.setOnClickListener { 
            login()
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

    private fun validateFields(): Pair<Boolean, String?> {
        return if (binding.usernameField.text.toString().isBlank() ||
            binding.passwordField.text.toString().isBlank()
        ) {
            Pair(false, getString(R.string.toast_fill_all_fields))
        } else {
            Pair(true, null)
        }
    }

    private fun login() {
        // Disable button for the duration of handling the action
        disableLoginButton()

        // Validate fields
        val (areFieldsValid, errorMessage) = validateFields()
        if (!areFieldsValid) {
            displayToast(errorMessage, Toast.LENGTH_SHORT)

            // delay enabling the button
            GlobalScope.launch {
                delay(2000)
                Handler(myContext.mainLooper).post {
                    enableLoginButton()
                }
            }
            return
        }

        GlobalScope.launch {
            // Try getting an auth token
            var validCredentials = true
            var errorOccurred = false
            try {
                val sessionManager = SessionManager(myContext)
                sessionManager.useCredentials(
                    binding.usernameField.text.toString(),
                    binding.passwordField.text.toString()
                )
            } catch (e: InvalidCredentials) {
                validCredentials = false
            } catch (e: Exception) {
                Log.e("LOGIN", e.toString())
                validCredentials = false
                errorOccurred = true
            }

            // Feedback to user & navigation
            Handler(myContext.mainLooper).post {
                //Enable the login button
                enableLoginButton()

                if (validCredentials) {
                    //TODO remove this toast
                    displayToast(
                        "Token: " + SessionManager.authToken
                                + " " + SessionManager.username
                                + " " + SessionManager.password,
                        Toast.LENGTH_LONG
                    )

                    // Navigate to Menu Activity
                    val intent = Intent(myContext, Menu::class.java)
                    activity?.finish()
                    myContext.startActivity(intent)
                } else if (!errorOccurred) {
                    displayToast(
                        getString(R.string.toast_message_incorrect_credentials),
                        Toast.LENGTH_LONG
                    )
                } else {
                    displayToast(
                        getString(R.string.toast_internal_error),
                        Toast.LENGTH_LONG
                    )
                }
            }
        }
    }

    private fun disableLoginButton() {
        binding.loginButton.isEnabled = false
        binding.loginButton.setBackgroundResource(R.drawable.rounded_disabled_button)
    }

    private fun enableLoginButton() {
        binding.loginButton.isEnabled = true
        binding.loginButton.setBackgroundResource(R.drawable.rounded_button)
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
