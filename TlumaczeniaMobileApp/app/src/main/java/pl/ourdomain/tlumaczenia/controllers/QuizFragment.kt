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
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.adapters.QuizAdapter
import pl.ourdomain.tlumaczenia.databinding.FragmentQuizBinding
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import java.util.*

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var translations: List<Translation>? = null

    private lateinit var quizAdapter: QuizAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_quiz, container, false
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
        binding.finishButton.setOnClickListener { view: View ->
            finishQuiz(view)
        }

        // disable button until the quiz list appears
        disableFinishButton()

        GlobalScope.launch {
            fetchQuiz()

            // User could leave this fragment by this time
            if (!isAttached) {
                return@launch
            }

            if (translations != null) {
                Handler(myContext.mainLooper).post {
                    initializeRecyclerView()
                    enableFinishButton()
                }
            }
        }
    }

    private fun initializeRecyclerView() {
        binding.recyclerViewQuiz.layoutManager = LinearLayoutManager(activity)
        quizAdapter = QuizAdapter(translations!!)
        binding.recyclerViewQuiz.adapter = quizAdapter
    }

    private fun finishQuiz(view: View) {
        if (translations == null) {
            Log.e("QUIZ", "Illegal state! Translations list is null.")
            return
        }

        // Check answers
        var goodAnswers = 0
        for ((index, dstText) in quizAdapter.holderList.withIndex()) {
            val correctAnswer = translations?.get(index)?.translated
            if (dstText.toLowerCase(Locale.getDefault()) == correctAnswer?.toLowerCase(Locale.getDefault())) {
                goodAnswers++
            }
        }
        val score = goodAnswers * 100 / translations!!.size

        // Pass results
        val action =
            QuizFragmentDirections.actionQuizFragmentToQuizResultFragment(
                score
            )
        view.findNavController().navigate(action)
    }

    private fun fetchQuiz() {
        try {
            val api = API(myContext)
            //TODO REMOVE HARDCODED LANG
            translations = SessionManager.authToken?.let { api.fetchQuiz(it, "en") }
        } catch (e: Exception) {
            Log.e("QUIZ", e.toString(), e)

            if (isAttached) {
                Handler(myContext.mainLooper).post {
                    displayToast(getString(R.string.toast_internal_error), Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun disableFinishButton() {
        binding.finishButton.isEnabled = false
        binding.finishButton.setBackgroundResource(R.drawable.rounded_disabled_button)
    }

    private fun enableFinishButton() {
        binding.finishButton.isEnabled = true
        binding.finishButton.setBackgroundResource(R.drawable.rounded_button)
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
