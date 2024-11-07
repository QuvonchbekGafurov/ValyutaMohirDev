package com.example.valyutamohirdev

import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
class CurrencyViewModel : ViewModel() {
    private val repository = CurrencyRepository()

    private val _currencyRates = MutableLiveData<List<CurrencyRate>>()
    val currencyRates: LiveData<List<CurrencyRate>> = _currencyRates

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchCurrencyRates() {
        viewModelScope.launch {
            _isLoading.value = true  // Yuklanish holatini yoqamiz
            try {
                val rates = repository.getCurrencyRates()
                if (rates != null && rates.isNotEmpty()) {
                    _currencyRates.value = rates!!
                    _error.value = null
                } else {
                    _error.value = "Ma'lumot mavjud emas"
                }
            } catch (e: Exception) {
                _error.value = "Xato yuz berdi: ${e.message}"
            } finally {
                _isLoading.value = false  // Yuklanish holatini o'chiramiz
            }
        }
    }
}
