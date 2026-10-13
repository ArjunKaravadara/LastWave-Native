package com.lastwave.app.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lastwave.app.ui.theme.PlayfairDisplay
import androidx.compose.ui.text.font.FontWeight

/**
 * AKWave profile setup: shown once after login/guest, before MainShell.
 * The user picks a display name and an avatar gradient. Both are stored
 * in [UserProfilePreferences] and shown across the app.
 */
data class AvatarOption(
    val gradient: Brush,
    val label: String,
)

val AvatarOptions: List<AvatarOption> = listOf(
    AvatarOption(Brush.linearGradient(listOf(Color(0xFF6A5CFF), Color(0xFF9D7BFF))), "Violet"),
    AvatarOption(Brush.linearGradient(listOf(Color(0xFFFF6B6B), Color(0xFFFFA94D))), "Sunset"),
    AvatarOption(Brush.linearGradient(listOf(Color(0xFF20C997), Color(0xFF94D82D))), "Mint"),
    AvatarOption(Brush.linearGradient(listOf(Color(0xFF339AF0), Color(0xFF66D9E8))), "Ocean"),
    AvatarOption(Brush.linearGradient(listOf(Color(0xFFF783AC), Color(0xFFDA77F2))), "Bloom"),
    AvatarOption(Brush.linearGradient(listOf(Color(0xFFFFC078), Color(0xFFFF8787))), "Peach"),
    AvatarOption(Brush.linearGradient(listOf(Color(0xFF3BC9DB), Color(0xFF748FFC))), "Sky"),
    AvatarOption(Brush.linearGradient(listOf(Color(0xFFB197FC), Color(0xFF9775FA))), "Grape"),
)

@Composable
fun ProfileAvatar(
    avatarIndex: Int,
    displayName: String,
    size: androidx.compose.ui.unit.Dp = 72.dp,
    onClick: (() -> Unit)? = null,
) {
    val option = AvatarOptions[avatarIndex.coerceIn(AvatarOptions.indices)]
    val initial = displayName.trim().firstOrNull()?.uppercaseChar()?.toString() ?: "A"
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(option.gradient)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontFamily = PlayfairDisplay,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            ),
        )
    }
}

@Composable
fun ProfileSetupScreen(
    onComplete: (displayName: String, avatarIndex: Int) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableIntStateOf(0) }
    val canContinue = name.trim().length >= 2

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(48.dp))
            Text(
                text = "Create your profile",
                style = MaterialTheme.typography.displaySmall.copy(
                    fontFamily = PlayfairDisplay,
                    fontWeight = FontWeight.Bold,
                ),
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Pick a name and an avatar — this is how AKWave knows you.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(32.dp))

            // Live avatar preview
            ProfileAvatar(
                avatarIndex = selectedAvatar,
                displayName = name.ifBlank { "A" },
                size = 96.dp,
            )
            Spacer(Modifier.height(24.dp))

            // Avatar grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                itemsIndexed(AvatarOptions) { index, option ->
                    val selected = index == selectedAvatar
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(option.gradient)
                            .clickable { selectedAvatar = index }
                            .then(
                                if (selected) Modifier.border(
                                    3.dp,
                                    MaterialTheme.colorScheme.primary,
                                    CircleShape,
                                ) else Modifier
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        if (selected) {
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = "Selected",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(28.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { if (it.length <= 24) name = it },
                label = { Text("Your name") },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(Modifier.weight(1f))

            Button(
                onClick = { onComplete(name.trim(), selectedAvatar) },
                enabled = canContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text("Continue", style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}
