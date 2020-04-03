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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.adapters.TranslationsAdapter
import pl.ourdomain.tlumaczenia.databinding.FragmentSavedTranslationsBinding
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import java.lang.Exception

class SavedTranslationsFragment : Fragment() {

    private lateinit var binding: FragmentSavedTranslationsBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var words: List<Translation>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_saved_translations, container, false
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
        // Fetch saved translations
        GlobalScope.launch {
            fetchSavedTranslations()

            // User could leave the fragment by this time
            if(!isAttached){
                return@launch
            }

            Handler(myContext.mainLooper).post {
                initializeRecyclerView()
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.translations.layoutManager = LinearLayoutManager(activity)
        if (words!=null){
            binding.translations.adapter = TranslationsAdapter(words!!)
        }
    }

    private fun fetchSavedTranslations() {
        try {
            val api = API(myContext)
            words = api.fetchSavedWords(SessionManager.authToken.toString())
        } catch (e: Exception) {
            Log.e("SAVED_WORDS", e.toString(), e)

            // User could leave the fragment by this time
            if(!isAttached){
                return
            }

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
