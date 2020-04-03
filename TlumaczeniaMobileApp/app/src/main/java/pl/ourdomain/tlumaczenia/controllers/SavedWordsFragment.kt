package pl.ourdomain.tlumaczenia.controllers

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentSavedWordsBinding
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import java.lang.Exception

class SavedWordsFragment : Fragment() {

    private lateinit var binding: FragmentSavedWordsBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var words: List<Translation>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentSavedWordsBinding>(
            inflater, R.layout.fragment_saved_words, container, false
        )

        GlobalScope.launch {
            fetchSavedWords()

            Handler(myContext.mainLooper).post {
                if (words != null) {
                    binding.list.text = words.toString()
                }
            }
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

    private fun fetchSavedWords() {
        try {
            val api = API(myContext)
            words = api.fetchSavedWords()
        } catch (e: Exception) {
            Log.e("SAVED_WORDS", e.toString(), e)

            Handler(myContext.mainLooper).post {
                displayToast(getString(R.string.toast_internal_error), Toast.LENGTH_LONG)
            }
        }
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
