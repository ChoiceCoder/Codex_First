package com.govtprep

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.govtprep.ui.navigation.GovtPrepNavHost
import com.govtprep.ui.theme.GovtPrepTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GovtPrepTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    GovtPrepNavHost()
                }
            }
        }
    }
}
