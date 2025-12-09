package com.douglasessousa.royalehub.ui.settings

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.douglasessousa.royalehub.R
import com.douglasessousa.royalehub.ui.theme.LossRed
import com.douglasessousa.royalehub.ui.theme.TextGray
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    onSave: () -> Unit
) {
    val nickname by viewModel.nickname.collectAsState()
    val id by viewModel.id.collectAsState()
    val avatarUri by viewModel.avatarUri.collectAsState()

    var showImageSourceDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var tempImageUri by remember { mutableStateOf<Uri?>(null) }

    fun createImageUri(context: Context): Uri {
        val file = File.createTempFile("JPEG_${System.currentTimeMillis()}_", ".jpg", context.externalCacheDir)
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    val pickMediaLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            viewModel.onAvatarChange(uri)
        }
    }

    val takePictureLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            tempImageUri?.let { viewModel.onAvatarChange(it) }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val newUri = createImageUri(context)
            tempImageUri = newUri
            takePictureLauncher.launch(newUri)
        }
    }

    if (showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { showImageSourceDialog = false },
            title = { Text("Escolher fonte da imagem") },
            text = { Text("Selecione uma foto para o seu perfil.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        when (PackageManager.PERMISSION_GRANTED) {
                            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                                val newUri = createImageUri(context)
                                tempImageUri = newUri
                                takePictureLauncher.launch(newUri)
                            }
                            else -> cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                ) {
                    Text("Câmera")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showImageSourceDialog = false
                        pickMediaLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                ) {
                    Text("Galeria")
                }
            }
        )
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text("Configurações", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Perfil do Jogador",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.surface
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .clickable { showImageSourceDialog = true },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = avatarUri,
                    contentDescription = "Avatar do Usuário",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
                    error = painterResource(id = R.drawable.ic_launcher_foreground)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nickname,
                onValueChange = { viewModel.onNicknameChange(it) },
                label = { Text("Nickname") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = id,
                onValueChange = { viewModel.onIdChange(it) },
                label = { Text("ID do Jogador") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveUser()
                    onSave()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Salvar Perfil")
            }

            Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Aparência", style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (isDarkTheme) "Modo Escuro" else "Modo Claro",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextGray
                        )
                    }

                    // Switch p/ a troca do tema
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onThemeChange(it) },
                        thumbContent = {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                modifier = Modifier.size(SwitchDefaults.IconSize)
                            )
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Dados da Aplicação", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Gerencie seus dados locais.", style = MaterialTheme.typography.bodySmall, color = TextGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.clearData() },
                        colors = ButtonDefaults.buttonColors(containerColor = LossRed),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limpar Todos os Dados")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sobre", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Versão 1.0.0", style = MaterialTheme.typography.bodySmall, color = TextGray)
                    Text("Desenvolvido para organizar seus decks e estratégias.", style = MaterialTheme.typography.bodySmall, color = TextGray)
                }
            }
        }
    }
}