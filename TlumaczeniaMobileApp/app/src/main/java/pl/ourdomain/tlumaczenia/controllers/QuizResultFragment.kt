package pl.ourdomain.tlumaczenia.controllers

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentQuizResultBinding

class QuizResultFragment : Fragment() {

    private lateinit var binding: FragmentQuizResultBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private val args: QuizResultFragmentArgs by navArgs()

    private var score: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_quiz_result, container, false
        )

        if (savedInstanceState != null){
            score = savedInstanceState.getInt("score")
        }

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
        binding.returnButton.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }

        // Setup result
        val resultTest = """${args.score}%"""
        binding.resultTest.text = resultTest

        when (args.score) {
            in 0..20 -> {
                binding.faceImage.setImageResource(R.drawable.ic_face_sad)
                binding.messageText.text = getString(R.string.text_quiz_result_bad)
                binding.messageText.setBackgroundResource(R.drawable.rounded_text_field_negative)
            }
            in 21..60 -> {
                binding.faceImage.setImageResource(R.drawable.ic_face_average)
                binding.messageText.text = getString(R.string.text_quiz_result_average)
                binding.messageText.setBackgroundResource(R.drawable.rounded_text_field_neutral)
            }
            else -> {
                binding.messageText.text = getString(R.string.text_quiz_result_good)
                binding.messageText.setBackgroundResource(R.drawable.rounded_text_field_positive)
            }
        }
    }

}
