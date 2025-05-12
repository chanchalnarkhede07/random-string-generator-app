package com.chanchal.generaterandomstring.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.chanchal.generaterandomstring.data.ContentProviderRepository
import com.chanchal.generaterandomstring.data.RandomStringDataItem
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: ContentProviderRepository
) : ViewModel() {

    // variable for list of generated strings
    private val _stringItemsList = MutableLiveData<List<RandomStringDataItem>>(emptyList())
    val stringItemsList: LiveData<List<RandomStringDataItem>> = _stringItemsList

    // variable to holds input length as a string
    private val _inputLength = MutableLiveData("")
    val inputLength: LiveData<String> = _inputLength

    // variable to store any error messages
    private val _errorMessage = MutableLiveData<String?>(null)
    val errorMessage: LiveData<String?> = _errorMessage

    // method which set length when user types a new length
    fun setLength(value: String) {
        _inputLength.value = value
        _errorMessage.value = null // Clear any old errors
    }

    // method which called when user clicks "Generate String"
    fun generateString() {
        val length = _inputLength.value?.toIntOrNull()

        if (length == null || length <= 0) {
            _errorMessage.value = "Please enter a valid length"
            return
        }

        viewModelScope.launch {
            val result = repository.fetchRandomString(length)

            // If successful, add the item to the list
            result.onSuccess { item ->
                _stringItemsList.value = _stringItemsList.value?.plus(item)
                _errorMessage.value = null
            }

            // If failed, show an error
            result.onFailure { error ->
                _errorMessage.value = "Failed to get string: ${error.message}"
            }
        }
    }

    // method which called when user clicks "Delete All"
    fun deleteAll() {
        _stringItemsList.value = emptyList()
    }

    // method which called when user deletes a specific item
    fun deleteItem(item: RandomStringDataItem) {
        _stringItemsList.value = _stringItemsList.value?.filter { it != item }
    }

    // Factory to help create ViewModel with repository
    companion object {
        fun provideFactory(repository: ContentProviderRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(repository) as T
                }
            }
        }
    }
}
