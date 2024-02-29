package com.example.myapplication.service

import com.example.myapplication.model.Balance
import com.example.myapplication.model.Commission
import com.example.myapplication.model.Currency
import com.example.myapplication.model.Exchange
import com.example.myapplication.model.RatesResponse

class ExchangeService {

    fun isSufficientFunds(
        fromBalance: Balance?,
        amount: Double,
        commission: Commission
    ): Boolean {
        return fromBalance != null && fromBalance.amount >= amount.plus(commission.fee)
    }

    fun isValidRatesResponse(
        response: RatesResponse?,
        fromCurrencyCode: String,
        toCurrencyCode: String
    ): Boolean {
        return response != null && response.rates.containsKey(fromCurrencyCode) && response.rates.containsKey(
            toCurrencyCode
        )
    }

    fun calculateExchangeRate(fromRate: Double, toRate: Double): Double {
        return toRate / fromRate
    }

    fun calculateExchangeResult(amount: Double, rate: Double): Double {
        return amount * rate
    }

    fun createExchangeObject(
        amount: Double,
        from: Currency,
        result: Double,
        to: Currency,
        rate: Double,
        commission: Commission
    ): Exchange {
        return Exchange(
            from = Balance(amount, from),
            to = Balance(result, to),
            rate = rate,
            commission = commission
        )
    }

    fun performExchange(
        userBalance: MutableList<Balance>,
        exchange: Exchange
    ) {
        val fromBalance = userBalance.find { it.currency == exchange.from.currency }
        if (fromBalance != null) {
            fromBalance.amount -= exchange.from.amount + exchange.commission.fee
        }
        val toBalance = userBalance.find { it.currency == exchange.to.currency }
        if (toBalance != null) {
            toBalance.amount += exchange.to.amount
        } else {
            userBalance.add(Balance(exchange.to.amount, exchange.to.currency))
        }
    }

    fun calculateCommission(freeExchanges: Int, amount: Double, currency: Currency): Commission {
        return if (freeExchanges > 0) {
            Commission(0.0, currency)
        } else {
            val fee = amount * 0.007
            Commission(fee, currency)
        }
    }

    fun executeExchange(
        userBalance: MutableList<Balance>,
        amount: Double,
        from: Currency,
        to: Currency,
        response: RatesResponse?,
        commission: Commission
    ): Exchange {
        val rate = calculateExchangeRate(
            response?.rates?.get(from.code)!!,
            response.rates[to.code]!!
        )
        val result = calculateExchangeResult(amount, rate)

        val exchange = createExchangeObject(amount, from, result, to, rate, commission)
        performExchange(userBalance, exchange)

        return exchange
    }
}









