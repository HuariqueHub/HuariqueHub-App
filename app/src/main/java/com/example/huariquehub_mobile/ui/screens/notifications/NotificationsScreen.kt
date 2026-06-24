package com.example.huariquehub_mobile.ui.screens.notifications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huariquehub_mobile.ui.theme.*

/**
 * Lista de notificaciones del usuario (US12). Al tocar una se marca como leída.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    viewModel: NotificationsViewModel = viewModel()
) {
    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones", color = SurfaceColor) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = SurfaceColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OrangePrimary)
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).padding(top = 8.dp)) {
            when {
                viewModel.isLoading && viewModel.notifications.isEmpty() ->
                    CircularProgressIndicator(color = OrangePrimary, modifier = Modifier.align(Alignment.Center))

                viewModel.notifications.isEmpty() ->
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🔔", fontSize = 44.sp)
                        Spacer(Modifier.height(8.dp))
                        Text("No tienes notificaciones", color = TextSecondary)
                    }

                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(viewModel.notifications) { n ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { if (!n.isRead) viewModel.markAsRead(n.id) },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (n.isRead) SurfaceColor else OrangeLight
                            )
                        ) {
                            Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Notifications, null, tint = OrangePrimary)
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(n.title, fontWeight = FontWeight.SemiBold, color = BrownDark, fontSize = 15.sp)
                                    Text(n.body, color = TextSecondary, fontSize = 13.sp)
                                    if (n.date.isNotBlank())
                                        Text(n.date, color = TextSecondary, fontSize = 11.sp)
                                }
                                if (!n.isRead) {
                                    Surface(
                                        color = OrangePrimary,
                                        shape = androidx.compose.foundation.shape.CircleShape
                                    ) {
                                        Box(Modifier.size(10.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
