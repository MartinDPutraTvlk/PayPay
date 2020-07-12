package com.martinezdputra.rateconverter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import com.martinezdputra.rateconverter.R
import com.martinezdputra.rateconverter.databinding.LayoutRateConversionItemBinding
import com.martinezdputra.rateconverter.datamodel.Rate

class RateAdapter(context: Context, currencies: List<Rate>): ArrayAdapter<Rate>(context, 0, currencies) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempView = convertView
        if(tempView == null) {
            val binding = DataBindingUtil.inflate<LayoutRateConversionItemBinding>(LayoutInflater.from(context), R.layout.layout_rate_conversion_item, parent, false)
            tempView = binding.root
        }
        DataBindingUtil.findBinding<LayoutRateConversionItemBinding>(tempView)?.also { binding ->
            getItem(position)?.also {
                binding.title = it.targetCurrency?.code
                binding.title2 = it.targetCurrency?.displayString
                binding.conversionRate = String.format("%.4f", it.rate)
                binding.amountAfterConversion = String.format("%.4f", it.amountAfterConversion)
            }
        }
        return tempView
    }
}