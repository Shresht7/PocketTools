package com.shresht7.pockettools

import com.shresht7.pockettools.ui.screens.tipCalculator.calculateTipAmount
import org.junit.Test
import org.junit.Assert.*
import java.text.NumberFormat

class TipCalculatorTest {
    @Test
    fun calculateTip_20PercentNoRoundup() {
        val amount = 10.00f
        val tipPercent = 20.00f / 100f
        val expectedTip = NumberFormat.getCurrencyInstance().format(2.00)
        val actualTip = calculateTipAmount(amount, tipPercent, false)
        assertEquals(expectedTip, actualTip)
    }
}