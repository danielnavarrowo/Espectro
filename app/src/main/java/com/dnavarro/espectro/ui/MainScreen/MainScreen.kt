package com.dnavarro.espectro.ui.MainScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun MainScreen(
    contentPadding: PaddingValues
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer,

    ){
        innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            Text(text = "Espectro")
        }

    }


}