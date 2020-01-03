package pl.ourdomain.tlumaczenia


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pl.ourdomain.tlumaczenia.databinding.FragmentLearningMethodsBinding

class LearningMethods : Fragment() {

    private lateinit var binding: FragmentLearningMethodsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentLearningMethodsBinding>(
            inflater,
            R.layout.fragment_learning_methods, container, false
        )

        return binding.root
    }


}
