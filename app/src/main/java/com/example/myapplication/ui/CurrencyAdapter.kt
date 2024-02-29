package com.example.myapplication.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.myapplication.model.Currency

class CurrencyAdapter(context: Context, resource: Int, private val currencies: List<Currency>) :
    ArrayAdapter<Currency>(context, resource, currencies) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val currencyCode = currencies[position].code
        (view as TextView).text = currencyCode
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val currencyCode = currencies[position].code
        (view as TextView).text = currencyCode
        return view
    }
}
