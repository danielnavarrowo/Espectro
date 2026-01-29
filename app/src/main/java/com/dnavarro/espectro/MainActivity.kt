package com.dnavarro.espectro

import android.Manifest
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.dnavarro.espectro.services.LWPService
import com.dnavarro.espectro.ui.theme.EspectroTheme
import androidx.core.content.edit

class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied. Audio visualization won't work.", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request Permission
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)

        setContent {
            EspectroTheme {
                 MainActivityScreen(
                     onSetWallpaperClick = {
                         try {
                             val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                             intent.putExtra(
                                 WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                 ComponentName(this@MainActivity, LWPService::class.java)
                             )
                             startActivity(intent)
                         } catch (e: Exception) {
                             Toast.makeText(this@MainActivity, "Error opening wallpaper picker", Toast.LENGTH_SHORT).show()
                         }
                     }
                 )
            }
        }
    }
}

@Composable
fun MainActivityScreen(onSetWallpaperClick: () -> Unit) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val prefs = remember {
        context.getSharedPreferences(Constants.PRENS_NAME, Context.MODE_PRIVATE)
    }

    // Remember state
    var selectedTheme by remember {
        mutableStateOf(prefs.getString(Constants.PREF_THEME, Constants.THEME_ICE))
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Espectro Live Wallpaper", style = MaterialTheme.typography.headlineMedium)
            Text(text = "Classic Visualization", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp))

            Button(onClick = onSetWallpaperClick) {
                Text(text = "Set Wallpaper")
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Choose Wallpaper Theme",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Ice Option
                ThemeOption(
                    name = "Ice Blue",
                    resId = R.drawable.ice,
                    isSelected = selectedTheme == Constants.THEME_ICE,
                    onClick = {
                        selectedTheme = Constants.THEME_ICE
                        prefs.edit { putString(Constants.PREF_THEME, Constants.THEME_ICE) }
                    }
                )

                // Fire Option
                ThemeOption(
                    name = "Fire Orange",
                    resId = R.drawable.fire,
                    isSelected = selectedTheme == Constants.THEME_FIRE,
                    onClick = {
                        selectedTheme = Constants.THEME_FIRE
                        prefs.edit { putString(Constants.PREF_THEME, Constants.THEME_FIRE) }
                    }
                )
            }
        }
    }
}

@Composable
fun ThemeOption(
    name: String,
    resId: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(100.dp, 160.dp)
                .border(
                    width = if (isSelected) 4.dp else 1.dp,
                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(4.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        RadioButton(
            selected = isSelected,
            onClick = onClick
        )
    }
}
