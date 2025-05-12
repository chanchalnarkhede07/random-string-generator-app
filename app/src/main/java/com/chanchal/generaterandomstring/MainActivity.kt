package com.chanchal.generaterandomstring

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chanchal.generaterandomstring.data.ContentProviderRepository
import com.chanchal.generaterandomstring.ui.MainUIScreen
import com.chanchal.generaterandomstring.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Created repository variable
        val repository = ContentProviderRepository(contentResolver)

        setContent {
            // Created the ViewModel and given it to the repository
            val viewModel: MainViewModel = viewModel(
                factory = MainViewModel.provideFactory(repository)
            )

            // Called the screen composable function and passed the viewModel
            MainUIScreen(viewModel)
        }
    }
}
