package com.example.myapplication.ui

import android.content.Context
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.model.Balance
import com.example.myapplication.model.Currency
import com.example.myapplication.model.Exchange
import java.text.DecimalFormat

class UIHelper(private val context: Context) {

    fun updateBalance(balanceTextView: TextView, userBalance: MutableList<Balance>) {
        val balanceText = userBalance.joinToString("\n") { balance ->
            "${DecimalFormat("#.##").format(balance.amount)} ${balance.currency.code} "
        }
        balanceTextView.text = balanceText
    }

    fun showResult(exchange: Exchange) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Currency exchanged successful")

        val resultText = "You have changed  ${
            DecimalFormat("#.##")
                .format(exchange.from.amount)
        } ${exchange.from.currency.code} to ${
            DecimalFormat("#.##")
                .format(exchange.to.amount)
        } ${exchange.to.currency.code}. Fee -  " +
                "${
                    DecimalFormat("#.##")
                        .format(exchange.commission.fee)
                } ${exchange.commission.currency.code}."

        builder.setMessage(resultText)
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun validateInput(
        amountEditText: EditText,
        fromCurrency: Currency?,
        toCurrency: Currency?
    ): Boolean {
        return when {
            amountEditText.text.isEmpty() || fromCurrency == null || toCurrency == null -> {
                showToast("Please, choose currency which you want to exchange")
                false
            }

            amountEditText.text.toString().toDouble() < 1.0 -> {
                showToast("Minimal exchange - 1 ${fromCurrency.code}")
                false
            }

            else -> true
        }
    }
}