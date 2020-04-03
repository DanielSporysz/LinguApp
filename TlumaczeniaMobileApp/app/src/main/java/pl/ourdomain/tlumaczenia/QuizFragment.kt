package pl.ourdomain.tlumaczenia

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
import pl.ourdomain.tlumaczenia.adapters.QuizAdapter
import pl.ourdomain.tlumaczenia.adapters.TranslationsAdapter
import pl.ourdomain.tlumaczenia.databinding.FragmentQuizBinding
import pl.ourdomain.tlumaczenia.dataclasses.Translation
import java.lang.Exception

class QuizFragment : Fragment() {

    private lateinit var binding: FragmentQuizBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var translations: List<Translation>? = null

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
        GlobalScope.launch {
            fetchQuiz()

            // User could leave this fragment by this time
            if (!isAttached) {
                return@launch
            }

            if (translations != null) {
                Handler(myContext.mainLooper).post {
                    initializeRecyclerView()
                }
            }
        }
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

    private fun initializeRecyclerView() {
        binding.recyclerViewQuiz.layoutManager = LinearLayoutManager(activity)
        binding.recyclerViewQuiz.adapter = QuizAdapter(translations!!)
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
