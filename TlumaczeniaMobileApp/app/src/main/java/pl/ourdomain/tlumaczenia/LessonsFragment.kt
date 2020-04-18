package pl.ourdomain.tlumaczenia

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pl.ourdomain.tlumaczenia.databinding.FragmentLessonsBinding

class LessonsFragment : Fragment() {

    private lateinit var binding: FragmentLessonsBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_lessons, container, false
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
        //TODO remove this
        binding.hello.text = System.getProperty("line.separator")?.let {
            "Czas Present Continuous składa się z:\\r\\n\\r\\n**podmiot + to be (am/is/are) + czasownik z końcówką -ing**\\r\\n\\r\\nCzasu Present Continuous używamy w następujących sytuacjach:\\r\\n1. Opisując czynności, które dzieją się w momencie mówienia o nich. Możemy dodatkowo użyć słów mówiących, że czynność jest wykonywana w tej chwili np. now lub at the moment.\\r\\n2. Wykorzystując  do opisu czynności, które nie muszą trwać dokładnie w chwili mówienia o nich, ale trwają dłuższy czas (włączając w to chwilę obecną).\\r\\n3. Wszelkie plany.\\r\\n4. Jeśli opisujemy czynność powtarzającą się lecz nie jest to \\\"stała\\\" cecha człowieka czy rzeczy."
                .replace("\\n", it)
                .replace("\\r", it)
        }
    }
}
