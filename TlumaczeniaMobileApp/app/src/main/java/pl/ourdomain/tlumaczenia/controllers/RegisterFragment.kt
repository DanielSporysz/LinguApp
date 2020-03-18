package pl.ourdomain.tlumaczenia.controllers


import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentRegisterBinding
import pl.ourdomain.tlumaczenia.dataclasses.RegisterInfo
import pl.ourdomain.tlumaczenia.API.registerUser
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.exceptions.TakenUsername
import java.lang.Exception


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    private val registerInfo: RegisterInfo =
        RegisterInfo()

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentRegisterBinding>(
            inflater,
            R.layout.fragment_register, container, false
        )

        binding.registerInfo = registerInfo

        binding.registerButton.setOnClickListener { view: View ->
            register(view)
            //
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

    private fun register(view: View) {
        disableRegisterButton()

        // Validate fields
        val (areFieldsValid, issueMessage) = validateFields()
        if (!areFieldsValid) {
            displayToast(issueMessage, Toast.LENGTH_SHORT)

            // delay enabling the button
            GlobalScope.launch {
                delay(2000)
                Handler(myContext.mainLooper).post {
                    enableRegisterButton()
                }
            }
            return
        }

        GlobalScope.launch {
            // Try registering
            var token: String? = null
            var isRegistered = false
            var errorMessage: String? = null
            try {
                token = registerUser(registerInfo.username, registerInfo.password)
                isRegistered = true
            } catch (e: TakenUsername) {
                errorMessage = getString(R.string.toast_taken_username)
            } catch (e: Exception) {
                errorMessage = getString(R.string.toast_internal_error)
            }

            Handler(myContext.mainLooper).post {
                if (isRegistered) {
                    SessionManager.authToken = token
                    SessionManager.username = registerInfo.username
                    SessionManager.password = registerInfo.password

                    //TODO remove this toast
                    displayToast(
                        "Token: " + SessionManager.authToken
                                + " " + SessionManager.username
                                + " " + SessionManager.password,
                        Toast.LENGTH_LONG
                    )

                    enableRegisterButton()
                    view.findNavController().navigate(R.id.action_registerFragment_to_menuMain)
                } else {
                    displayToast(errorMessage, Toast.LENGTH_LONG)

                    // delay enabling the button
                    GlobalScope.launch {
                        delay(2000)
                        Handler(myContext.mainLooper).post {
                            enableRegisterButton()
                        }
                    }
                }
            }
        }
    }

    private fun validateFields(): Pair<Boolean, String?> {
        if (registerInfo.username.isBlank() ||
            registerInfo.email.isBlank() ||
            registerInfo.reEmail.isBlank() ||
            registerInfo.password.isBlank() ||
            registerInfo.rePassword.isBlank()
        ) {
            return Pair(false, getString(R.string.toast_fill_all_fields))
        } else {
            return Pair(true, null)
        }
    }

    private fun disableRegisterButton() {
        binding.registerButton.isEnabled = false
        binding.registerButton.setBackgroundResource(R.drawable.rounded_disabled_button)
    }

    private fun enableRegisterButton() {
        binding.registerButton.isEnabled = true
        binding.registerButton.setBackgroundResource(R.drawable.rounded_button)
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
