package com.mib.feature_home.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.ParseException
import java.util.*

/**
 * Created by Stevian on 2020-02-17.
 */
class NumberTextWatcher(et: EditText) : TextWatcher {
    private val df: DecimalFormat
    private val dfnd: DecimalFormat
    private var hasFractionalPart: Boolean
    private val et: EditText
    override fun afterTextChanged(s: Editable) {
        et.removeTextChangedListener(this)
        try {
            val inilen: Int
            val endlen: Int
            inilen = et.text.length
            val v = s.toString()
                .replace(df.decimalFormatSymbols.groupingSeparator.toString(), "")
            val n = df.parse(v)
            val cp = et.selectionStart
            if (hasFractionalPart) {
                et.setText(df.format(n))
            } else {
                et.setText(dfnd.format(n))
            }
            endlen = et.text.length
            val sel = cp + (endlen - inilen)
            if (sel > 0 && sel <= et.text.length) {
                et.setSelection(sel)
            } else {
                // place cursor at the end?
                et.setSelection(et.text.length - 1)
            }
        } catch (nfe: NumberFormatException) {
            // do nothing?
        } catch (e: ParseException) {
            // do nothing?
        }
        et.addTextChangedListener(this)
    }

    override fun beforeTextChanged(
        s: CharSequence,
        start: Int,
        count: Int,
        after: Int
    ) {
    }

    override fun onTextChanged(
        s: CharSequence,
        start: Int,
        before: Int,
        count: Int
    ) {
        hasFractionalPart =
            if (s.toString().contains(df.decimalFormatSymbols.decimalSeparator.toString())) {
                true
            } else {
                false
            }
    }

    companion object {
        private const val TAG = "NumberTextWatcher"
    }

    init {
        val decimalFormatSymbols =
            DecimalFormatSymbols(Locale.getDefault())
        decimalFormatSymbols.groupingSeparator = '.'
        decimalFormatSymbols.decimalSeparator = ','
        df = DecimalFormat("#,###,###", decimalFormatSymbols)
        df.isDecimalSeparatorAlwaysShown = true
        dfnd = DecimalFormat("#,###", decimalFormatSymbols)
        this.et = et
        hasFractionalPart = false
        //        df = new DecimalFormat("#,###.##");
//        df.setDecimalSeparatorAlwaysShown(true);
//        dfnd = new DecimalFormat("#,###");
//        this.et = et;
//        hasFractionalPart = false;
    }
}