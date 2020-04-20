package pl.ourdomain.tlumaczenia.controllers

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.adapters.ABCDQuizAdapter
import pl.ourdomain.tlumaczenia.databinding.FragmentAbcQuizBinding
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.adapters.LessonAdapter


class AbcQuizFragment : Fragment() {

    private lateinit var binding: FragmentAbcQuizBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var translations: List<Translation>? = null

    private lateinit var abcdQuizAdapter: ABCDQuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_abc_quiz, container, false
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

    private fun initView() {
        binding.returnButton.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }

        GlobalScope.launch {
            fetchTranslations()

            if (isAttached && translations != null) {
                Handler(myContext.mainLooper).post {
                    initializeRecyclerView()
                }
            }
        }
    }

    private fun fetchTranslations() {
        try {
            val api = API(myContext)
            //TODO REMOVE HARDCODED LANG
            translations = SessionManager.authToken?.let { api.fetchABCDQuiz(it, "en") }
        } catch (e: Exception) {
            Log.e("ABCDQuiz", e.toString(), e)

            if (isAttached) {
                Handler(myContext.mainLooper).post {
                    displayToast(getString(R.string.toast_internal_error), Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.abcdQuiz.layoutManager = LinearLayoutManager(activity)
        abcdQuizAdapter = ABCDQuizAdapter(translations!!)
        binding.abcdQuiz.adapter = abcdQuizAdapter
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
