package com.douglasessousa.royalehub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.douglasessousa.royalehub.navigation.NavGraph
import com.douglasessousa.royalehub.ui.theme.RoyalehubTheme
import com.douglasessousa.royalehub.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoyalehubTheme {
                NavGraph(appViewModel = appViewModel)
            }
        }
    }
}
