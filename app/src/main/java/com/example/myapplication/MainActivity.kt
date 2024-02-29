package com.example.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.config.appModule
import com.example.myapplication.model.Balance
import com.example.myapplication.model.Commission
import com.example.myapplication.model.Currency
import com.example.myapplication.service.ApiService
import com.example.myapplication.service.ExchangeService
import com.example.myapplication.ui.CurrencyAdapter
import com.example.myapplication.ui.UIHelper
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {

    private val currencies = listOf(
        Currency("EUR", "Euro", "€"),
        Currency("AED", "United Arab Emirates Dirham", "د.إ"),
        Currency("AFN", "Afghan Afghani", "؋"),
        Currency("ALL", "Albanian Lek", "L"),
        Currency("AMD", "Armenian Dram", "֏"),
        Currency("ANG", "Netherlands Antillean Guilder", "ƒ"),
        Currency("AOA", "Angolan Kwanza", "Kz"),
        Currency("ARS", "Argentine Peso", "$"),
        Currency("AUD", "Australian Dollar", "$"),
        Currency("AWG", "Aruban Florin", "ƒ"),
        Currency("AZN", "Azerbaijani Manat", "₼"),
        Currency("BAM", "Bosnia and Herzegovina Convertible Mark", "KM"),
        Currency("BBD", "Barbadian Dollar", "$"),
        Currency("BDT", "Bangladeshi Taka", "৳"),
        Currency("BGN", "Bulgarian Lev", "лв"),
        Currency("BIF", "Burundian Franc", "Fr"),
        Currency("BMD", "Bermudian Dollar", "$"),
        Currency("BND", "Brunei Dollar", "$"),
        Currency("BOB", "Bolivian Boliviano", "Bs."),
        Currency("BRL", "Brazilian Real", "R$"),
        Currency("BSD", "Bahamian Dollar", "$"),
        Currency("BTN", "Bhutanese Ngultrum", "Nu."),
        Currency("BWP", "Botswana Pula", "P"),
        Currency("BYN", "Belarusian Ruble", "Br"),
        Currency("BYR", "Belarusian Ruble (old)", "Br"),
        Currency("BZD", "Belize Dollar", "$"),
        Currency("CAD", "Canadian Dollar", "$"),
        Currency("CDF", "Congolese Franc", "Fr"),
        Currency("CHF", "Swiss Franc", "Fr"),
        Currency("CLP", "Chilean Peso", "$"),
        Currency("CNY", "Chinese Yuan", "¥"),
        Currency("COP", "Colombian Peso", "$"),
        Currency("CRC", "Costa Rican Colón", "₡"),
        Currency("CUC", "Cuban Convertible Peso", "$"),
        Currency("CUP", "Cuban Peso", "$"),
        Currency("CVE", "Cape Verdean Escudo", "Esc"),
        Currency("CZK", "Czech Koruna", "Kč"),
        Currency("DJF", "Djiboutian Franc", "Fr"),
        Currency("DKK", "Danish Krone", "kr"),
        Currency("DOP", "Dominican Peso", "$"),
        Currency("DZD", "Algerian Dinar", "د.ج"),
        Currency("EGP", "Egyptian Pound", "£"),
        Currency("ERN", "Eritrean Nakfa", "Nfk"),
        Currency("ETB", "Ethiopian Birr", "Br"),
        Currency("FJD", "Fijian Dollar", "$"),
        Currency("FKP", "Falkland Islands Pound", "£"),
        Currency("GBP", "British Pound", "£"),
        Currency("GEL", "Georgian Lari", "₾"),
        Currency("GGP", "Guernsey Pound", "£"),
        Currency("GHS", "Ghanaian Cedi", "₵"),
        Currency("GIP", "Gibraltar Pound", "£"),
        Currency("GMD", "Gambian Dalasi", "D"),
        Currency("GNF", "Guinean Franc", "Fr"),
        Currency("GTQ", "Guatemalan Quetzal", "Q"),
        Currency("GYD", "Guyanese Dollar", "$"),
        Currency("HKD", "Hong Kong Dollar", "$"),
        Currency("HNL", "Honduran Lempira", "L"),
        Currency("HRK", "Croatian Kuna", "kn"),
        Currency("HTG", "Haitian Gourde", "G"),
        Currency("HUF", "Hungarian Forint", "Ft"),
        Currency("IDR", "Indonesian Rupiah", "Rp"),
        Currency("ILS", "Israeli New Shekel", "₪"),
        Currency("IMP", "Manx Pound", "£"),
        Currency("INR", "Indian Rupee", "₹"),
        Currency("IQD", "Iraqi Dinar", "ع.د"),
        Currency("IRR", "Iranian Rial", "﷼"),
        Currency("ISK", "Icelandic Króna", "kr"),
        Currency("JEP", "Jersey Pound", "£"),
        Currency("JMD", "Jamaican Dollar", "$"),
        Currency("JOD", "Jordanian Dinar", "د.ا"),
        Currency("JPY", "Japanese Yen", "¥"),
        Currency("KES", "Kenyan Shilling", "Sh"),
        Currency("KGS", "Kyrgyzstani Som", "с"),
        Currency("KHR", "Cambodian Riel", "៛"),
        Currency("KMF", "Comorian Franc", "Fr"),
        Currency("KPW", "North Korean Won", "₩"),
        Currency("KRW", "South Korean Won", "₩"),
        Currency("KWD", "Kuwaiti Dinar", "د.ك"),
        Currency("KYD", "Cayman Islands Dollar", "$"),
        Currency("KZT", "Kazakhstani Tenge", "₸"),
        Currency("LAK", "Lao Kip", "₭"),
        Currency("LBP", "Lebanese Pound", "ل.ل"),
        Currency("LKR", "Sri Lankan Rupee", "Rs"),
        Currency("LRD", "Liberian Dollar", "$"),
        Currency("LSL", "Lesotho Loti", "L"),
        Currency("LTL", "Lithuanian Litas", "Lt"),
        Currency("LVL", "Latvian Lats", "Ls"),
        Currency("LYD", "Libyan Dinar", "ل.د"),
        Currency("MAD", "Moroccan Dirham", "د.م."),
        Currency("MDL", "Moldovan Leu", "L"),
        Currency("MGA", "Malagasy Ariary", "Ar"),
        Currency("MKD", "Macedonian Denar", "ден"),
        Currency("MMK", "Burmese Kyat", "Ks"),
        Currency("MNT", "Mongolian Tögrög", "₮"),
        Currency("MOP", "Macanese Pataca", "P"),
        Currency("MRO", "Mauritanian Ouguiya", "UM"),
        Currency("MUR", "Mauritian Rupee", "₨"),
        Currency("MVR", "Maldivian Rufiyaa", ".ރ"),
        Currency("MWK", "Malawian Kwacha", "MK"),
        Currency("MXN", "Mexican Peso", "$"),
        Currency("MYR", "Malaysian Ringgit", "RM"),
        Currency("MZN", "Mozambican Metical", "MT"),
        Currency("NAD", "Namibian Dollar", "$"),
        Currency("NGN", "Nigerian Naira", "₦"),
        Currency("NIO", "Nicaraguan Córdoba", "C$"),
        Currency("NOK", "Norwegian Krone", "kr"),
        Currency("NPR", "Nepalese Rupee", "₨"),
        Currency("NZD", "New Zealand Dollar", "$"),
        Currency("OMR", "Omani Rial", "ر.ع."),
        Currency("PAB", "Panamanian Balboa", "B/."),
        Currency("PEN", "Peruvian Sol", "S/"),
        Currency("PGK", "Papua New Guinean Kina", "K"),
        Currency("PHP", "Philippine Peso", "₱"),
        Currency("PKR", "Pakistani Rupee", "₨"),
        Currency("PLN", "Polish Złoty", "zł"),
        Currency("PYG", "Paraguayan Guaraní", "₲"),
        Currency("QAR", "Qatari Riyal", "ر.ق"),
        Currency("RON", "Romanian Leu", "lei"),
        Currency("RSD", "Serbian Dinar", "дин"),
        Currency("RWF", "Rwandan Franc", "Fr"),
        Currency("SAR", "Saudi Riyal", "ر.س"),
        Currency("SBD", "Solomon Islands Dollar", "$"),
        Currency("SCR", "Seychellois Rupee", "₨"),
        Currency("SDG", "Sudanese Pound", "ج.س"),
        Currency("SEK", "Swedish Krona", "kr"),
        Currency("SGD", "Singapore Dollar", "$"),
        Currency("SHP", "Saint Helena Pound", "£"),
        Currency("SLL", "Sierra Leonean Leone", "Le"),
        Currency("SOS", "Somali Shilling", "Sh"),
        Currency("SRD", "Surinamese Dollar", "$"),
        Currency("STD", "São Tomé and Príncipe Dobra", "Db"),
        Currency("SVC", "Salvadoran Colón", "₡"),
        Currency("SYP", "Syrian Pound", "£"),
        Currency("SZL", "Swazi Lilangeni", "L"),
        Currency("THB", "Thai Baht", "฿"),
        Currency("TJS", "Tajikistani Somoni", "SM"),
        Currency("TMT", "Turkmenistani Manat", "m"),
        Currency("TND", "Tunisian Dinar", "د.ت"),
        Currency("TOP", "Tongan Paʻanga", "T$"),
        Currency("TRY", "Turkish Lira", "₺"),
        Currency("TTD", "Trinidad and Tobago Dollar", "$"),
        Currency("TWD", "New Taiwan Dollar", "$"),
        Currency("TZS", "Tanzanian Shilling", "Sh"),
        Currency("UAH", "Ukrainian Hryvnia", "₴"),
        Currency("UGX", "Ugandan Shilling", "Sh"),
        Currency("USD", "United States Dollar", "$"),
        Currency("UYU", "Uruguayan Peso", "$"),
        Currency("UZS", "Uzbekistani Som", "so'm"),
        Currency("VEF", "Venezuelan Bolívar", "Bs"),
        Currency("VND", "Vietnamese đồng", "₫"),
        Currency("VUV", "Vanuatu Vatu", "Vt"),
        Currency("WST", "Samoan Tala", "T"),
        Currency("XAF", "Central African CFA franc", "Fr"),
        Currency("XCD", "East Caribbean Dollar", "$"),
        Currency("XDR", "Special Drawing Rights", ""),
        Currency("XOF", "West African CFA franc", "Fr"),
        Currency("XPF", "CFP Franc", "Fr"),
        Currency("YER", "Yemeni Rial", "﷼"),
        Currency("ZAR", "South African Rand", "R"),
        Currency("ZMK", "Zambian Kwacha (pre-2013)", "ZK"),
        Currency("ZMW", "Zambian Kwacha", "ZK"),
        Currency("ZWL", "Zimbabwean Dollar", "$")
    )

    private val userBalance = mutableListOf(Balance(1000.0, currencies[0]))

    private var freeExchanges = 5

    private var fromCurrency: Currency? = null

    private var toCurrency: Currency? = null

    private lateinit var fromSpinner: Spinner
    private lateinit var toSpinner: Spinner
    private lateinit var amountEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var balanceTextView: TextView
    private lateinit var resultTextView: TextView

    private val apiUrl = "https://developers.paysera.com/tasks/api/currency-exchange-rates"

    private val uiHelper: UIHelper by inject()
    private val exchangeService: ExchangeService by inject()
    private val apiService: ApiService by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

        initializeViews()
        setupSpinners()
        setupSubmitButton()
    }

    private fun initializeViews() {
        fromSpinner = findViewById(R.id.from_spinner)
        toSpinner = findViewById(R.id.to_spinner)
        amountEditText = findViewById(R.id.amount_edit_text)
        amountEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    resultTextView.text = ""
                } else {
                    val amount = s.toString().toDoubleOrNull()
                    if (amount != null && fromCurrency != null && toCurrency != null) {
                        updateExpectedAmount()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing
            }
        })


        submitButton = findViewById(R.id.submit_button)
        balanceTextView = findViewById(R.id.balance_text_view)
        resultTextView = findViewById(R.id.result_text_view)

        uiHelper.updateBalance(balanceTextView, userBalance)
    }

    private fun setupSpinners() {
        val adapter = CurrencyAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        fromSpinner.adapter = adapter
        toSpinner.adapter = adapter

        fromSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                fromCurrency = currencies[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing
            }
        }

        toSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                toCurrency = currencies[position]
                updateExpectedAmount()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //do nothing
            }
        }
    }

    private fun setupSubmitButton() {
        submitButton.setOnClickListener {
            handleExchangeButtonClick()
        }
    }

    private fun handleExchangeButtonClick() {
        if (uiHelper.validateInput(amountEditText, fromCurrency, toCurrency)) {
            try {
                val amount = amountEditText.text.toString().toDouble()
                tryExchange(amount, fromCurrency!!, toCurrency!!)
            } catch (e: NumberFormatException) {
                uiHelper.showToast("Number format exception")
            }
        }
    }

    private fun tryExchange(amount: Double, from: Currency, to: Currency) {
        val fromBalance = userBalance.find { it.currency == from }
        val commission = exchangeService.calculateCommission(freeExchanges, amount, from)

        if (exchangeService.isSufficientFunds(fromBalance, amount, commission)) {
            handleExchange(amount, from, to, commission)
        } else {
            uiHelper.showToast("Insufficient funds for exchange")
        }
    }

    private fun updateExpectedAmount() {
        val amount = amountEditText.text.toString().toDoubleOrNull()
        if (amount != null && fromCurrency != null && toCurrency != null) {
            lifecycleScope.launch {
                val response = apiService.getRatesResponse(apiUrl)
                if (exchangeService.isValidRatesResponse(
                        response,
                        fromCurrency!!.code,
                        toCurrency!!.code
                    )
                ) {
                    val rate = exchangeService.calculateExchangeRate(
                        response?.rates?.get(fromCurrency!!.code)!!,
                        response.rates[toCurrency!!.code]!!
                    )
                    val result = exchangeService.calculateExchangeResult(amount, rate)
                    resultTextView.text = "+${DecimalFormat("#.##").format(result)}"
                }
            }
        }
    }

    private fun handleExchange(
        amount: Double,
        from: Currency,
        to: Currency,
        commission: Commission
    ) {
        lifecycleScope.launch {
            val response = apiService.getRatesResponse(apiUrl)

            if (exchangeService.isValidRatesResponse(response, from.code, to.code)) {
                val exchange = exchangeService.executeExchange(
                    userBalance,
                    amount,
                    from,
                    to,
                    response,
                    commission
                )

                uiHelper.showResult(exchange)
                uiHelper.updateBalance(balanceTextView, userBalance)

                freeExchanges--
            } else {
                uiHelper.showToast("Unable to get exchange rates")
            }
        }
    }
}