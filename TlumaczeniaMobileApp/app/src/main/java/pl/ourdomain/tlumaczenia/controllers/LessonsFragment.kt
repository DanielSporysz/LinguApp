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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import pl.ourdomain.tlumaczenia.API
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.SessionManager
import pl.ourdomain.tlumaczenia.adapters.LessonAdapter
import pl.ourdomain.tlumaczenia.databinding.FragmentLessonsBinding
import pl.ourdomain.tlumaczenia.dataclasses.Lesson

class LessonsFragment : Fragment() {

    private lateinit var binding: FragmentLessonsBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    private var lessons: List<Lesson>? = null

    private lateinit var lessonsAdapter: LessonAdapter

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

        GlobalScope.launch {
            fetchLessons()

            if (isAttached && lessons != null) {
                Handler(myContext.mainLooper).post {
                    initializeRecyclerView()
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

    private fun initializeRecyclerView() {
        binding.lessonsView.layoutManager = LinearLayoutManager(activity)
        lessonsAdapter = LessonAdapter(lessons!!)
        binding.lessonsView.adapter = lessonsAdapter
    }

    private fun displayToast(msg: String?, duration: Int) {
        val toast = Toast.makeText(myContext, msg, duration)
        toast.show()
    }
}
