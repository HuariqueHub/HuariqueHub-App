package com.example.huariquehub_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.huariquehub_mobile.ui.navigation.AppNavigation
import com.example.huariquehub_mobile.ui.theme.HuariqueHubMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HuariqueHubMobileTheme {
                AppNavigation()
            }
        }
    }
}