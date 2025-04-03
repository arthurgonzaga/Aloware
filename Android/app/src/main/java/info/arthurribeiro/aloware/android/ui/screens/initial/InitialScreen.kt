package info.arthurribeiro.aloware.android.ui.screens.initial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import info.arthurribeiro.aloware.android.R
import info.arthurribeiro.aloware.android.ui.AlowareUiState

@Composable
fun InitialScreen(
    modifier: Modifier = Modifier,
    uiState: AlowareUiState,
    onCallButtonClick: (id: String) -> Unit,
) {
    var currentText by remember {
        mutableStateOf("")
    }

    Scaffold { paddingValues ->
        Column(modifier.padding(paddingValues)) {
            TextField(
                currentText,
                onValueChange = {
                    currentText = it
                },
            )

            Button(
                onClick = {
                    onCallButtonClick(currentText)
                },
            ) {
                Text(
                    text = stringResource(R.string.call),
                )
            }
        }
    }
}
