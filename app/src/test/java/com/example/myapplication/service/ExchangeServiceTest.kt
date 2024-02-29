package com.example.myapplication.service

import com.example.myapplication.model.Balance
import com.example.myapplication.model.Commission
import com.example.myapplication.model.Currency
import com.example.myapplication.model.Exchange
import com.example.myapplication.model.RatesResponse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExchangeServiceTest{

    @Test
    fun isSufficientFunds_ShouldReturnTrue_WhenThereAreSufficientFunds() {
        val exchangeService = ExchangeService()
        val fromBalance = Balance(100.0, Currency("USD", "United States Dollar", "$"))
        val amount = 50.0
        val commission = Commission(5.0, Currency("USD", "United States Dollar", "$"))

        val result = exchangeService.isSufficientFunds(fromBalance, amount, commission)

        assertTrue(result)
    }

    @Test
    fun `isValidRatesResponse should return true when response is valid`() {
        val exchangeService = ExchangeService()
        val response = RatesResponse("USD", "2022-01-01", mapOf("USD" to 1.0, "EUR" to 0.8))
        val fromCurrencyCode = "USD"
        val toCurrencyCode = "EUR"

        val result = exchangeService.isValidRatesResponse(response, fromCurrencyCode, toCurrencyCode)

        assertTrue(result)
    }

    @Test
    fun isValidRatesResponse_NegativeCase() {
        val response = RatesResponse("USD", "2022-01-01", mapOf("USD" to 1.0, "GBP" to 0.72))
        val result = ExchangeService().isValidRatesResponse(response, "USD", "EUR")
        assertFalse(result)
    }

    @Test
    fun calculateExchangeRate_ShouldCalculateExchangeRateCorrectly() {
        val result = ExchangeService().calculateExchangeRate(1.0, 0.85)
        assertEquals(0.85, result, 0.0001)
    }

    @Test
    fun calculateExchangeResult_ShouldCalculateExchangeResultCorrectly() {
        val result = ExchangeService().calculateExchangeResult(100.0, 0.85)
        assertEquals(85.0, result, 0.0001)
    }

    @Test
    fun createExchangeObject_ShouldCreateExchangeObjectWithCorrectValues() {
        val amount = 100.0
        val from = Currency("USD", "US Dollar", "$")
        val result = 85.0
        val to = Currency("EUR", "Euro", "€")
        val rate = 0.85
        val commission = Commission(1.0, Currency("USD", "US Dollar", "$"))

        val exchange = ExchangeService().createExchangeObject(amount, from, result, to, rate, commission)

        assertEquals(amount, exchange.from.amount, 0.0001)
        assertEquals(from, exchange.from.currency)
        assertEquals(result, exchange.to.amount, 0.0001)
        assertEquals(to, exchange.to.currency)
        assertEquals(rate, exchange.rate, 0.0001)
        assertEquals(commission, exchange.commission)
    }

    @Test
    fun performExchange_ShouldPerformExchangeCorrectly() {
        val fromBalance = Balance(100.0, Currency("USD", "US Dollar", "$"))
        val toBalance = Balance(50.0, Currency("EUR", "Euro", "€"))
        val userBalance = mutableListOf(fromBalance, toBalance)

        val exchange = Exchange(
            from = Balance(30.0, Currency("USD", "US Dollar", "$")),
            to = Balance(25.5, Currency("EUR", "Euro", "€")),
            rate = 0.85,
            commission = Commission(1.0, Currency("USD", "US Dollar", "$"))
        )

        ExchangeService().performExchange(userBalance, exchange)

        assertEquals(69.0, fromBalance.amount, 0.0001)
        assertEquals(75.5, toBalance.amount, 0.0001)
    }

    @Test
    fun calculateCommission_FreeExchanges_ShouldCalculateCommissionWithFreeExchanges() {
        val freeExchanges = 2
        val amount = 100.0
        val currency = Currency("USD", "US Dollar", "$")

        val commission = ExchangeService().calculateCommission(freeExchanges, amount, currency)

        assertEquals(0.0, commission.fee, 0.0001)
        assertEquals(currency, commission.currency)
    }

    @Test
    fun calculateCommission_WithFee_ShouldCalculateCommissionWithFee() {
        val freeExchanges = 0
        val amount = 100.0
        val currency = Currency("USD", "US Dollar", "$")

        val commission = ExchangeService().calculateCommission(freeExchanges, amount, currency)

        assertEquals(0.7, commission.fee, 0.0001)
        assertEquals(currency, commission.currency)
    }

    @Test
    fun executeExchange_ShouldExecuteExchangeCorrectly() {
        val userBalance = mutableListOf(Balance(100.0, Currency("USD", "US Dollar", "$")))
        val amount = 50.0
        val from = Currency("USD", "US Dollar", "$")
        val to = Currency("EUR", "Euro", "€")
        val response = RatesResponse("USD", "2022-01-01", mapOf("USD" to 1.0, "EUR" to 0.85))
        val commission = Commission(1.0, Currency("USD", "US Dollar", "$"))

        val exchange = ExchangeService().executeExchange(userBalance, amount, from, to, response, commission)

        assertEquals(50.0, exchange.from.amount, 0.0001)
        assertEquals(42.5, exchange.to.amount, 0.0001)
        assertEquals(0.85, exchange.rate, 0.0001)
        assertEquals(commission, exchange.commission)
        assertEquals(2, userBalance.size)
    }
}