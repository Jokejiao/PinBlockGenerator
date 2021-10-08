package com.iso.pinblockgenerator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iso.pinblockgenerator.domain.PinBlockUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PosViewModel @Inject constructor(
    private val pinBlockUseCase: PinBlockUseCase
) : ViewModel() {

    private val _pinLiveData = MutableLiveData("")
    var pinLiveData: LiveData<String> = _pinLiveData

    private val _invalidPinPrompt = MutableLiveData<Boolean>()
    val invalidPinPrompt: LiveData<Boolean> = _invalidPinPrompt

    fun clickDigit(key: Char) {
        _invalidPinPrompt.value = false

        _pinLiveData.value?.let {
            if (it.length >= MAX_LEN) return@clickDigit
        }

        _pinLiveData.value += key
    }

    fun clearDigit() {
        _pinLiveData.value = _pinLiveData.value?.run {
            if (length > 0) substring(0, length - 1) else ""
        }
    }

    fun calculatePinBlock() {
        if (!validatePin()) {
            _invalidPinPrompt.value = true
            return
        }

        viewModelScope.launch {
            _pinLiveData.value?.let {
                pinBlockUseCase.invoke(it)
            }
        }
    }

    private fun validatePin() = _pinLiveData.value?.length?.let {
        it in MIN_LEN..MAX_LEN
    } ?: false

    companion object {
        private const val MIN_LEN = 4
        private const val MAX_LEN = 12
    }
}