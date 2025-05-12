package com.chanchal.generaterandomstring.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chanchal.generaterandomstring.R
import com.chanchal.generaterandomstring.viewmodel.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainUIScreen(viewModel: MainViewModel) {
    // Observe state from ViewModel
    val items by viewModel.stringItemsList.observeAsState(emptyList())
    val length by viewModel.inputLength.observeAsState("")
    val error by viewModel.errorMessage.observeAsState()

    // Main screen content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        //Title Text
        Text(
            text = stringResource(R.string.app_bar_label),
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Input for string length
        OutlinedTextField(
            value = length,
            onValueChange = { viewModel.setLength(it) },
            label = { Text(text = stringResource(R.string.string_length_label)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Buttons for generate and delete all functionlaity
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { viewModel.generateString() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.Black
                ),
            ) {
                Text(text = stringResource(R.string.generate_button))
            }
            Button(
                onClick = { viewModel.deleteAll() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE91E63),
                    contentColor = Color.Black
                ),
            ) {
                Text(text = stringResource(R.string.delete_all_string))
            }
        }

        // Show error message if error is occured
        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = it, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // List showing generated random strings
        LazyColumn {
            items(items) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text("Value: ${item.value}")
                        Text("Length: ${item.length}")
                        Text("Created: ${item.created}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.deleteItem(item) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE91E63),
                                contentColor = Color.Black
                            ),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(text = stringResource(R.string.delete))
                        }
                    }
                }
            }
        }
    }

}
