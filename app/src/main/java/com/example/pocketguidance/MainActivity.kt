package com.example.pocketguidance

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.example.pocketguidance.data.local.database.AppDatabase
import com.example.pocketguidance.data.repository.UserRepository
import com.example.pocketguidance.navigation.AppNavGraph
import com.example.pocketguidance.ui.theme.PocketGuidanceTheme
import com.example.pocketguidance.viewmodel.AuthModelFactory
import com.example.pocketguidance.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db         = AppDatabase.getDatabase(this)
        val repo       = UserRepository(db.userDao())
        val authViewModel = ViewModelProvider(
            this,
            AuthModelFactory(repo)
        )[AuthViewModel::class.java]

        setContent {
            PocketGuidanceTheme {
                // ✅ AppNavGraph now only needs authViewModel —
                //    it creates its own NavController internally
                AppNavGraph(authViewModel = authViewModel)
            }
        }
    }
}
