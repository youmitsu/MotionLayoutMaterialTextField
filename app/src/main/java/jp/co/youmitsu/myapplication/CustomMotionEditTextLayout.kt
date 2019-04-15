package jp.co.youmitsu.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ObservableField
import android.support.constraint.motion.MotionLayout
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import jp.co.youmitsu.myapplication.databinding.LayoutCustomMotionEdittextBinding

class CustomMotionEditTextLayout : FrameLayout {

    private val binding: LayoutCustomMotionEdittextBinding
    private val _title = ObservableField<String>("")
    private val _value = ObservableField<String>("")
    var title: String?
        get() = _title.get()
        set(value) {
            _title.set(value)
        }
    var value: String?
        get() = _value.get()
        set(value) {
            _value.set(value)
        }

    private val imm: InputMethodManager by lazy {
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.CustomMotionEditTextLayout, 0, 0)
        try {
            title = a.getString(R.styleable.CustomMotionEditTextLayout_title)
            value = a.getString(R.styleable.CustomMotionEditTextLayout_value)
        } finally {
            a.recycle()
        }
        val inflater = LayoutInflater.from(context)
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_custom_motion_edittext, this, true)
        binding.data = this
        initCallbacks()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initCallbacks() {
        binding.editText.setOnFocusChangeListener { _, hasFocus ->
            ((binding.editText.text as SpannableStringBuilder).toString()).apply {
                binding.nicknameValue.text = this
                value = this
            }
            if (!hasFocus) {
                binding.motionLayout.transitionToStart()
            }
        }
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {}

            override fun onTransitionCompleted(p0: MotionLayout?, sceneId: Int) {
                when (sceneId) {
                    R.id.end -> {
                        prepareEditTextToShow()
                    }
                }
            }
        })
    }

    private fun prepareEditTextToShow() {
        if (binding.editText.requestFocus()) {
            imm.showSoftInput(binding.editText, InputMethodManager.SHOW_IMPLICIT)
        }
        binding.editText.apply {
            setSelection(length())
        }
    }

    private fun hideKeyboard() {
        imm.hideSoftInputFromWindow(binding.motionLayout.windowToken, 0)
    }

}