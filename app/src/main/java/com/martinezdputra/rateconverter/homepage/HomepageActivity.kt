package com.martinezdputra.rateconverter.homepage

import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.martinezdputra.rateconverter.R
import com.martinezdputra.rateconverter.adapter.CurrencyAdapter
import com.martinezdputra.rateconverter.core.CoreActivity
import com.martinezdputra.rateconverter.databinding.HomepageActivityBinding
import com.martinezdputra.rateconverter.datamodel.Currency
import com.martinezdputra.rateconverter.di.component.DaggerAppComponent
import javax.inject.Inject


class HomepageActivity: CoreActivity<HomepageViewModel>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mBinding: HomepageActivityBinding

    private val mHandler = Handler()
    private val afterTextChangedTask = Runnable {
        Toast.makeText(this, "executed for ${viewModel.amount}", Toast.LENGTH_SHORT).show()
    }

    override fun createViewModel(): HomepageViewModel {
        return ViewModelProvider(this, viewModelFactory).get(HomepageViewModel::class.java)
    }

    override fun injectComponent() {
        DaggerAppComponent.create().inject(this)
    }

    override fun onInitView(): ViewDataBinding {
        mBinding = DataBindingUtil.setContentView(this, R.layout.homepage_activity)
        mBinding.viewModel = viewModel

        viewModel.fetchCurrencies()

        initViewComponents()
        return mBinding
    }

    private fun initViewComponents() {
        mBinding.editTextInput.doAfterTextChanged {
            mHandler.removeCallbacks(afterTextChangedTask)
            mHandler.postDelayed(afterTextChangedTask, 500)
        }
        viewModel.currencies.observe(this, Observer {
            mBinding.spinnerCurrency.adapter = CurrencyAdapter(this, it)
            mBinding.spinnerCurrency.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedItem: Currency? = parent?.getItemAtPosition(position) as? Currency
                    Toast.makeText(this@HomepageActivity, "selected item position ${selectedItem?.code}", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }
}