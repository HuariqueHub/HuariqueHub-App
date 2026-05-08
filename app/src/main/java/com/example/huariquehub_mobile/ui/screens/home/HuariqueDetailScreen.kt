package com.example.huariquehub_mobile.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huariquehub_mobile.data.model.*
import com.example.huariquehub_mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HuariqueDetailScreen(
    huariqueId: Int,
    onBack: () -> Unit
) {
    val huarique = sampleHuariques.find { it.id == huariqueId } ?: sampleHuariques.first()
    val reviews = remember { getReviewsForHuarique(huariqueId) }
    var isFav by remember { mutableStateOf(huarique.isFavorite) }
    var showReviewDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(huarique.name, fontWeight = FontWeight.Bold, color = SurfaceColor, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = SurfaceColor)
                    }
                },
                actions = {
                    IconButton(onClick = { isFav = !isFav }) {
                        Icon(
                            if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            null,
                            tint = if (isFav) ErrorRed else SurfaceColor
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Share, null, tint = SurfaceColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OrangePrimary)
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showReviewDialog = true },
                containerColor = OrangePrimary,
                contentColor = SurfaceColor,
                icon = { Icon(Icons.Default.Edit, null) },
                text = { Text("Escribir reseña") }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmWhite),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Imagen hero
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .background(
                            Brush.horizontalGradient(colors = listOf(OrangeLight, YellowGreen.copy(alpha = 0.6f)))
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🍽️", fontSize = 80.sp)
                    // Badge rating
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = BrownDark.copy(alpha = 0.85f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Star, null, tint = StarYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("%.1f".format(huarique.rating), color = SurfaceColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(" (${huarique.reviewCount})", color = SurfaceColor.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                    }
                }
            }

            // Información principal
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Nombre y categoría
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = huarique.name,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BrownDark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = OrangeLight
                                ) {
                                    Text(
                                        huarique.category,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        fontSize = 12.sp,
                                        color = BrownMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Info de contacto
                        InfoRow(Icons.Default.LocationOn, "${huarique.address}, ${huarique.district}")
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(Icons.Default.Phone, huarique.phone)
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoRow(Icons.Default.Schedule, huarique.hours)

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Descripción
                        Text("Acerca de", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = BrownDark)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(huarique.description, fontSize = 14.sp, color = TextSecondary, lineHeight = 22.sp)

                        Spacer(modifier = Modifier.height(16.dp))

                        // Botones de acción
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = OrangePrimary),
                                border = androidx.compose.foundation.BorderStroke(1.dp, OrangePrimary)
                            ) {
                                Icon(Icons.Default.Phone, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Llamar", fontSize = 13.sp)
                            }
                            Button(
                                onClick = {},
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                            ) {
                                Icon(Icons.Default.Map, null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Ver mapa", fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Reseñas header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Reseñas (${reviews.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = BrownDark
                    )
                    // Rating promedio
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) { index ->
                            Icon(
                                Icons.Default.Star, null,
                                tint = if (index < huarique.rating.toInt()) StarYellow else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }

            if (reviews.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("💬", fontSize = 36.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Aún no hay reseñas", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text("¡Sé el primero en opinar!", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(reviews) { review ->
                    ReviewCard(review = review)
                }
            }
        }
    }

    // Dialog para nueva reseña
    if (showReviewDialog) {
        NewReviewDialog(
            onDismiss = { showReviewDialog = false },
            onSubmit = { _, _ -> showReviewDialog = false }
        )
    }
}

@Composable
private fun InfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = OrangePrimary, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, fontSize = 14.sp, color = TextSecondary)
    }
}

@Composable
fun ReviewCard(review: com.example.huariquehub_mobile.data.model.Review) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(OrangeLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            review.userName.first().toString(),
                            fontWeight = FontWeight.Bold,
                            color = OrangePrimary,
                            fontSize = 16.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(review.userName, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = BrownDark)
                        Text(review.date, fontSize = 11.sp, color = TextSecondary)
                    }
                }
                // Estrellas
                Row {
                    repeat(5) { index ->
                        Icon(
                            Icons.Default.Star, null,
                            tint = if (index < review.rating.toInt()) StarYellow else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(review.comment, fontSize = 14.sp, color = TextPrimary, lineHeight = 21.sp)
        }
    }
}

@Composable
private fun NewReviewDialog(
    onDismiss: () -> Unit,
    onSubmit: (Float, String) -> Unit
) {
    var rating by remember { mutableStateOf(5f) }
    var comment by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva reseña", fontWeight = FontWeight.Bold, color = BrownDark) },
        text = {
            Column {
                Text("Tu calificación:", fontSize = 14.sp, color = TextSecondary)
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    repeat(5) { index ->
                        IconButton(
                            onClick = { rating = (index + 1).toFloat() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Star, null,
                                tint = if (index < rating.toInt()) StarYellow else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = comment,
                    onValueChange = { comment = it },
                    label = { Text("Comparte tu experiencia") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = OrangePrimary,
                        focusedLabelColor = OrangePrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (comment.isNotBlank()) onSubmit(rating, comment) },
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                shape = RoundedCornerShape(10.dp)
            ) { Text("Publicar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = TextSecondary) }
        },
        containerColor = SurfaceColor
    )
}
