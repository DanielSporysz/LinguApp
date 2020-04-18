package pl.ourdomain.tlumaczenia.controllers


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentLearningMethodsBinding

class LearningMethodsFragment : Fragment() {

    private lateinit var binding: FragmentLearningMethodsBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLearningMethodsBinding>(
            inflater,
            R.layout.fragment_learning_methods, container, false
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
        binding.quizButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_learningMethods_to_quizFragment)
        }
        binding.abcButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_learningMethods_to_abcQuizFragment)
        }
        binding.lessonsButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_learningMethods_to_lessonsFragment)
        }
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
