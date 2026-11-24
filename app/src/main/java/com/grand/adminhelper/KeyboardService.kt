package com.grand.adminhelper

import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout

class KeyboardService : InputMethodService() {

    private var isCaps = false
    private fun getConn(): InputConnection? = currentInputConnection

    override fun onCreateInputView(): View {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(android.graphics.Color.parseColor("#FF1A1A1A"))
            setPadding(8, 8, 8, 8)
        }

        val rowAdmin = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        rowAdmin.addView(makeBtn("/ans ", "#FFCC00"))
        rowAdmin.addView(makeBtn("Приветствую", "#FF333333"))
        rowAdmin.addView(makeBtn("Отказано", "#FF333333"))
        rowAdmin.addView(makeBtn("Ожидайте", "#FF333333"))
        layout.addView(rowAdmin)

        val rowNum = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        for (c in "1234567890") rowNum.addView(makeKeyBtn(c.toString()))
        layout.addView(rowNum)

        val rows = listOf("йцукенгшщзхъ", "фывапролджэ", "ячсмитьбю")
        for (r in rows) {
            val row = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
            for (c in r) row.addView(makeKeyBtn(c.toString()))
            layout.addView(row)
        }

        val rowBottom = LinearLayout(this).apply { orientation = LinearLayout.HORIZONTAL }
        val btnCaps = makeBtn("⇧", "#FF444444")
        btnCaps.setOnClickListener { isCaps = !isCaps }
        val btnSpace = makeBtn("ПРОБЕЛ", "#FF444444")
        btnSpace.setOnClickListener { getConn()?.commitText(" ", 1) }
        val btnDel = makeBtn("⌫", "#FF444444")
        btnDel.setOnClickListener { getConn()?.deleteSurroundingText(1, 0) }
        val btnEnter = makeBtn("⏎", "#FF00AA00")
        btnEnter.setOnClickListener {
            getConn()?.commitText("\n", 1)
            sendDefaultEditorAction(true)
        }
        val btnSwitch = makeBtn("🌐", "#FF444444")
        btnSwitch.setOnClickListener { switchInputMethod() }

        rowBottom.addView(btnCaps)
        rowBottom.addView(btnSpace)
        rowBottom.addView(btnDel)
        rowBottom.addView(btnEnter)
        rowBottom.addView(btnSwitch)
        layout.addView(rowBottom)

        return layout
    }

    private fun makeBtn(text: String, colorHex: String): Button {
        return Button(this).apply {
            this.text = text
            setBackgroundColor(android.graphics.Color.parseColor(colorHex))
            setTextColor(android.graphics.Color.WHITE)
            textSize = 14f
            layoutParams = LinearLayout.LayoutParams(0, 130, 1f).apply {
                setMargins(3, 3, 3, 3)
            }
            setOnClickListener { getConn()?.commitText(text + " ", 1) }
        }
    }

    private fun makeKeyBtn(char: String): Button {
        return Button(this).apply {
            text = char
            setBackgroundColor(android.graphics.Color.parseColor("#FF333333"))
            setTextColor(android.graphics.Color.WHITE)
            textSize = 16f
            layoutParams = LinearLayout.LayoutParams(0, 130, 1f).apply {
                setMargins(2, 2, 2, 2)
            }
            setOnClickListener {
                val out = if (isCaps) char.uppercase() else char
                getConn()?.commitText(out, 1)
            }
        }
    }
}
