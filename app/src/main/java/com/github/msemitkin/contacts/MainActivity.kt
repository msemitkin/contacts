package com.github.msemitkin.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.msemitkin.contacts.ui.theme.ContactsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navHostController = rememberNavController()
            ContactsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            ApplicationHeader(
                                onAuthorClick = { navHostController.navigate("author") },
                            )
                        }
                    ) {
                        NavHost(
                            navController = navHostController,
                            startDestination = "homepage",
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            composable("homepage") {
                                if (ContextCompat.checkSelfPermission(
                                        applicationContext,
                                        Manifest.permission.READ_CONTACTS
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    ActivityCompat.requestPermissions(
                                        this@MainActivity,
                                        arrayOf(Manifest.permission.READ_CONTACTS), 100
                                    )
                                }
                                ContactsAutoSuggestions(
                                    getContactsByQuery = ContactsService(applicationContext.contentResolver)
                                )
                            }
                            composable("author") {
                                AuthorScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ApplicationHeader(
    onAuthorClick: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        actions = {
            var expanded by remember { mutableStateOf(false) }
            IconButton(onClick = {
                expanded = true
            }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(onClick = onAuthorClick) {
                    Text(text = "About author")
                }
            }
        })
}