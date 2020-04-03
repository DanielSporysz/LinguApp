package pl.ourdomain.tlumaczenia.controllers


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentLearningMethodsBinding

class LearningMethodsFragment : Fragment() {

    private lateinit var binding: FragmentLearningMethodsBinding

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

    private fun initView() {
        binding.quizButton.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_learningMethods_to_quizFragment)
        }
    }
}
