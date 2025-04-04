package info.arthurribeiro.aloware.android.ui.screens.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.arthurribeiro.aloware.android.R
import info.arthurribeiro.aloware.android.ui.AlowareUiState
import info.arthurribeiro.aloware.android.ui.theme.Blue
import info.arthurribeiro.aloware.android.ui.theme.DarkBlue
import info.arthurribeiro.aloware.android.ui.theme.Green
import info.arthurribeiro.aloware.android.ui.theme.Red
import info.arthurribeiro.aloware.android.ui.theme.White

@Composable
fun InitialScreen(
    modifier: Modifier = Modifier,
    uiState: AlowareUiState,
    onCallButtonClick: (id: String) -> Unit,
    onAnswerButtonClick: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(DarkBlue)
                .padding(16.dp),
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text("Welcome to", color = Color.White, fontSize = 32.sp)
        Text("Aloware", color = Green, fontSize = 50.sp, fontWeight = FontWeight.Black)
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(
            text = text,
            onValueChange = {
                text = it
            },
        )
        Spacer(modifier = Modifier.height(24.dp))
        QuickCallSection(
            onCallButtonClick = onCallButtonClick,
        )
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = {
                if (uiState.isRinging) {
                    onAnswerButtonClick()
                } else {
                    onCallButtonClick(text)
                }
            },
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(50.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        if (uiState.isRinging) {
                            Green
                        } else {
                            Blue
                        },
                ),
        ) {
            Text(
                text = if (uiState.isRinging) "Answer" else "Start Call",
                fontSize = 18.sp,
                color = White
            )
        }
    }
}

@Composable
fun SearchBar(
    text: String,
    onValueChange: (String) -> Unit = {},
) {
    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
                .height(50.dp),
    )
}

@Composable
fun QuickCallSection(onCallButtonClick: (id: String) -> Unit) {
    Text("Quick Call", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        QuickCallButton(
            name = "Arthur\niOS",
            onClick = {
                onCallButtonClick("arthur-ios")
            },
        )
        QuickCallButton(
            name = "Adrielly\nAndroid",
            onClick = {
                onCallButtonClick("adrielly-android")
            },
        )
    }
}

@Composable
fun QuickCallButton(
    name: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(White.copy(alpha = 0.2f)),
        )

        Text(
            modifier = Modifier.padding(top = 6.dp),
            text = name,
            color = Color.White,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
        )
    }
}


@Preview
@Composable
fun PreviewInitialScreen() {
    InitialScreen(
        uiState = AlowareUiState(),
        onCallButtonClick = {},
        onAnswerButtonClick = {},
    )
}