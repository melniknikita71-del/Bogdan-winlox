package com.example.bogdanlockerdemo

import android.app.Activity
import android.os.Bundle
import android.os.CountDownTimer
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import java.util.Locale

class MainActivity : Activity() {
    private val red = Color.rgb(190, 0, 0)
    private val black = Color.rgb(5, 5, 5)
    private var timer: CountDownTimer? = null
    private var remaining = 48L * 60L * 60L * 1000L
    private lateinit var root: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        showBootScreen()
    }

    private fun base(bg: Int): LinearLayout {
        root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(24, 28, 24, 24)
            setBackgroundColor(bg)
        }
        setContentView(root)
        return root
    }

    private fun text(value: String, size: Float, color: Int, mono: Boolean = false): TextView =
        TextView(this).apply {
            text = value
            textSize = size
            setTextColor(color)
            gravity = Gravity.CENTER
            if (mono) typeface = Typeface.MONOSPACE
        }

    private fun addLine(parent: LinearLayout, view: View, weight: Float = 0f) {
        parent.addView(view, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            if (weight > 0f) 0 else LinearLayout.LayoutParams.WRAP_CONTENT,
            weight
        ).apply { topMargin = 8; bottomMargin = 8 })
    }

    private fun safeExitButton(parent: LinearLayout) {
        val b = Button(this).apply {
            text = "ВЫЙТИ ИЗ ДЕМО"
            setOnClickListener { finish() }
        }
        addLine(parent, b)
    }

    private fun showBootScreen() {
        val p = base(black)
        addLine(p, text(
            "Booting Android demo...\\n" +
            "Boot error: 0x03527737\\nBoot error: 0x02666712\\n" +
            "Boot error: 0x02897593\\nBoot error: 0x01447812\\n" +
            "Memory section at address 0x0424* is locked!\\n" +
            "Service UXCryptor started.\\n\\n* DEMO SIMULATION ONLY *",
            17f, Color.rgb(235, 25, 35), true
        ))
        addLine(p, text("◉", 76f, Color.WHITE))
        addLine(p, text("ВАС ЗАМЕТИЛИ", 32f, Color.WHITE))
        addLine(p, text("Это визуальная хоррор-демонстрация", 13f, Color.LTGRAY))
        safeExitButton(p)
        p.postDelayed({ if (!isFinishing) showReaperScreen() }, 5000)
    }

    private fun showReaperScreen() {
        val p = base(red)
        addLine(p, text("☠", 82f, Color.WHITE))
        addLine(p, text(
            "             .-.\n" +
            "            (o o)\n" +
            "            | O \\\\\n" +
            "             \\\\   \\\\\n" +
            "          .---'---.\n" +
            "         /  ЖНЕЦ  \\\\\n" +
            "        /_________\\\\",
            22f, Color.WHITE, true
        ))
        addLine(p, text("БЛОКИРОВКА УСТРОЙСТВА", 22f, Color.WHITE))
        safeExitButton(p)
        p.postDelayed({ if (!isFinishing) showWarningScreen() }, 4500)
    }

    private fun showWarningScreen() {
        val p = base(black)
        addLine(p, text("В ВАШЕМ УСТРОЙСТВЕ ЖУКИ", 22f, Color.RED))
        addLine(p, text(
            "ПОЭТОМУ МЫ НЕМЕДЛЕННО ЗАБЛОКИРОВАЛИ ЕГО.\\n" +
            "ПРИ ПЕРЕЗАГРУЗКЕ ОН БУДЕТ ЗАБЛОКИРОВАН НАВСЕГДА.\\n\\n" +
            "ЗА КОДОМ К @Loser_1715",
            17f, Color.WHITE
        ))
        addLine(p, text("ОСТАЛОСЬ ВРЕМЕНИ (ДЕКОРАТИВНО)", 13f, Color.LTGRAY))
        val clock = text("48:00:00", 36f, Color.RED, true)
        addLine(p, clock)
        timer?.cancel()
        timer = object : CountDownTimer(remaining, 1000) {
            override fun onTick(ms: Long) {
                remaining = ms
                val s = ms / 1000
                clock.text = String.format(Locale.US, "%02d:%02d:%02d", s / 3600, (s % 3600) / 60, s % 60)
            }
            override fun onFinish() { clock.text = "00:00:00" }
        }.start()

        val input = EditText(this).apply {
            hint = "Введите демонстрационный пароль"
            setSingleLine(true)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setBackgroundColor(Color.DKGRAY)
        }
        addLine(p, input)
        val unlock = Button(this).apply {
            text = "ПРОВЕРИТЬ КОД"
            setOnClickListener {
                if (input.text.toString() == "7777") showCompleteScreen()
                else Toast.makeText(this@MainActivity, "Неверный код (демо)", Toast.LENGTH_SHORT).show()
            }
        }
        addLine(p, unlock)
        safeExitButton(p)
        addLine(p, text("DEMO • НИЧЕГО НЕ БЛОКИРУЕТ • ДАННЫЕ НЕ ЗАТРАГИВАЮТСЯ", 10f, Color.LTGRAY))
    }

    private fun showCompleteScreen() {
        timer?.cancel()
        val p = base(black)
        addLine(p, text("ДЕМО ЗАВЕРШЕНО", 30f, Color.GREEN))
        addLine(p, text("Код принят. Устройство никогда не блокировалось.", 17f, Color.WHITE))
        safeExitButton(p)
    }

    override fun onDestroy() {
        timer?.cancel()
        super.onDestroy()
    }
}
