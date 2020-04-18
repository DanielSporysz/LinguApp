package pl.ourdomain.tlumaczenia.controllers

import android.content.Context
import android.graphics.Typeface
import android.icu.lang.UProperty.INT_START
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import pl.ourdomain.tlumaczenia.R
import pl.ourdomain.tlumaczenia.databinding.FragmentAbcQuizBinding


class AbcQuizFragment : Fragment() {

    private lateinit var binding: FragmentAbcQuizBinding

    private lateinit var myContext: Context
    private var isAttached: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_abc_quiz, container, false
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
        binding.test69.text = "ABC questions list :)"
    }
}
