package pl.ourdomain.tlumaczenia

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import pl.ourdomain.tlumaczenia.databinding.FragmentQuizResultBinding

class QuizResultFragment : Fragment() {

    private lateinit var binding: FragmentQuizResultBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    val args: QuizResultFragmentArgs by navArgs()

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
        binding.resultTest.text = args.score.toString()
    }

}
