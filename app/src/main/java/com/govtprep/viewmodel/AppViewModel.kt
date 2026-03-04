package com.govtprep.viewmodel

import androidx.lifecycle.ViewModel
import com.govtprep.data.model.Test
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {
    private val _selectedTest = MutableStateFlow<Test?>(null)
    val selectedTest: StateFlow<Test?> = _selectedTest.asStateFlow()

    fun selectTest(test: Test) {
        _selectedTest.value = test
    }
}
