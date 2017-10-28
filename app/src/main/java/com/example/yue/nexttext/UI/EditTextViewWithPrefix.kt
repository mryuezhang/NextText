package com.example.yue.nexttext.UI

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet

/**
 * Created by yue on 2017-10-28.
 */
class EditTextViewWithPrefix : AppCompatEditText {

    private var mOriginalLeftPadding = -1f
    constructor(context: Context): super(context)
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, int: Int): super(context, attributeSet, int)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        getPrefix()
    }

    private fun getPrefix(){
        if( mOriginalLeftPadding == -1f){
            val prefix = tag as String
            val widths = FloatArray(prefix.length)
            paint.getTextWidths(prefix, widths)
            val textWidth = widths.sum()
            mOriginalLeftPadding = compoundPaddingLeft.toFloat()
            setPadding((textWidth + mOriginalLeftPadding).toInt(),
                    paddingRight, paddingTop,
                    paddingBottom)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val prefix = tag as String
        canvas.drawText(prefix, mOriginalLeftPadding,
                getLineBounds(0, null).toFloat(), paint)
    }
}