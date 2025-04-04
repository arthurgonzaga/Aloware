package info.arthurribeiro.aloware.android.ui.screens.call

import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import info.arthurribeiro.aloware.android.R
import info.arthurribeiro.aloware.android.ui.AlowareUiState
import info.arthurribeiro.aloware.android.ui.theme.DarkBlue
import info.arthurribeiro.aloware.android.ui.theme.Red
import info.arthurribeiro.aloware.android.ui.theme.White

@Composable
fun CallScreen(
    uiState: AlowareUiState,
    onHangUpClick: () -> Unit = {},
    onMuteToggleClick: () -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(White),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                uiState.callInvite?.from ?: "Unknown",
                color = DarkBlue,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                CircleButton(
                    DarkBlue,
                    icon = if(uiState.isMuted) {
                        ImageVector.vectorResource(id = R.drawable.ic_mic_white_off_24dp)
                    } else {
                        ImageVector.vectorResource(id = R.drawable.ic_mic_white_24dp)
                    },
                    onClick = onMuteToggleClick,
                )
                CircleButton(
                    Red,
                    ImageVector.vectorResource(id = R.drawable.ic_call_end_white_24dp),
                    onClick = onHangUpClick,
                )
            }
        }
    }
}

@Composable
fun CircleButton(
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit = {},
) {
    IconButton(
        modifier =
            Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color),
        onClick = onClick,
    ) {
        Icon(icon, contentDescription = null, tint = Color.White)
    }
}

@Preview
@Composable
fun PreviewCallScreen() {
    CallScreen(
        uiState = AlowareUiState(),
    )
}
