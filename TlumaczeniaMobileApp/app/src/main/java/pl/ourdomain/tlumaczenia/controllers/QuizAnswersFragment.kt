package pl.ourdomain.tlumaczenia.controllers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.ourdomain.tlumaczenia.QuizState
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.adapters.QuizAdapter
import pl.ourdomain.tlumaczenia.adapters.QuizResultAdapter
import pl.ourdomain.tlumaczenia.databinding.FragmentQuizAnswersBinding

class QuizAnswersFragment : Fragment() {

    private lateinit var binding: FragmentQuizAnswersBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_quiz_answers, container, false
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

        if (QuizState.translations != null && QuizState.answers != null && QuizState.isCorrect != null) {
            binding.answersView.layoutManager = LinearLayoutManager(activity)
            val adapter =
                QuizResultAdapter(
                    QuizState.translations!!, QuizState.answers!!,
                    QuizState.isCorrect!!
                )
            binding.answersView.adapter = adapter
        }
    }
}
