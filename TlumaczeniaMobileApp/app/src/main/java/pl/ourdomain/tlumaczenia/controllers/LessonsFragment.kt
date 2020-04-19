package pl.ourdomain.tlumaczenia.controllers

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
import androidx.navigation.findNavController
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.databinding.FragmentLessonsBinding
import pl.ourdomain.tlumaczenia.dataclasses.Lesson

class LessonsFragment : Fragment() {

    private lateinit var binding: FragmentLessonsBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var lessons: List<Lesson>? = null

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
        binding.returnButton.setOnClickListener { view: View ->
            view.findNavController().popBackStack()
        }

//        //TODO remove this
//        binding.hello.text = System.getProperty("line.separator")?.let {
//            "Czas Present Continuous składa się z:\\r\\n\\r\\n**podmiot + to be (am/is/are) + czasownik z końcówką -ing**\\r\\n\\r\\nCzasu Present Continuous używamy w następujących sytuacjach:\\r\\n1. Opisując czynności, które dzieją się w momencie mówienia o nich. Możemy dodatkowo użyć słów mówiących, że czynność jest wykonywana w tej chwili np. now lub at the moment.\\r\\n2. Wykorzystując  do opisu czynności, które nie muszą trwać dokładnie w chwili mówienia o nich, ale trwają dłuższy czas (włączając w to chwilę obecną).\\r\\n3. Wszelkie plany.\\r\\n4. Jeśli opisujemy czynność powtarzającą się lecz nie jest to \\\"stała\\\" cecha człowieka czy rzeczy."
//                .replace("\\n", it)
//                .replace("\\r", it)
//        }

        GlobalScope.launch {
            fetchLessons()

            // User could leave this fragment by this time
            if (!isAttached) {
                return@launch
            }

            if (lessons != null) {
                Handler(myContext.mainLooper).post {
                    //TODO init recycler viewer
                    //initializeRecyclerView()
                    displayToast(lessons.toString(), Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun fetchLessons() {
        try {
            val api = API(myContext)
            //TODO REMOVE HARDCODED LANG
            lessons = SessionManager.authToken?.let { api.fetchLessons(it, "en") }
        } catch (e: Exception) {
            Log.e("QUIZ", e.toString(), e)

            if (isAttached) {
                Handler(myContext.mainLooper).post {
                    displayToast(getString(R.string.toast_internal_error), Toast.LENGTH_LONG)
                }
            }
        }
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
