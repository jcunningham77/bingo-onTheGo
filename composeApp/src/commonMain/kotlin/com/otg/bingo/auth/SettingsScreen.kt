package com.otg.bingo.auth

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.otg.bingo.navigation.BrandingTopBar
import com.otg.bingo.navigation.SystemBackHandler
import com.otg.bingo.util.loggi

@Composable
fun SettingsScreen(onClose: () -> Unit) {

    SystemBackHandler(onBack = onClose)

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            BrandingTopBar {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
    ) { paddingValues ->
        loggi("Settings screen padding: ${paddingValues.calculateStartPadding(LayoutDirection.Rtl)}")
        Box(Modifier.fillMaxSize().padding(paddingValues)) {
            Box(Modifier.fillMaxSize().padding(16.dp)){
                TextButton(
                    onClick = {}
                ) {Text("Sign out")}
            }

        }
    }
}

