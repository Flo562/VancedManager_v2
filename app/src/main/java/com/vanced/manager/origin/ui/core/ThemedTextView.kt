package com.vanced.manager.origin.ui.core

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.vanced.manager.origin.utils.accentColor
import com.vanced.manager.origin.utils.lifecycleOwner

class ThemedTextView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attributeSet, defStyleAttr) {
    init {
        context.lifecycleOwner?.let { owner ->
            accentColor.observe(owner) { color ->
                setTextColor(color.toInt())
            }
        }
    }
}