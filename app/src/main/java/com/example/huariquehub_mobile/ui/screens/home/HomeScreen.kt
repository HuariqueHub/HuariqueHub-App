package com.example.huariquehub_mobile.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huariquehub_mobile.data.model.*
import com.example.huariquehub_mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onHuariqueClick: (Int) -> Unit,
    onProfileClick: () -> Unit,
    userRole: UserRole = UserRole.CONSUMER
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todas") }

    val filteredHuariques = remember(searchQuery, selectedCategory) {
        sampleHuariques.filter { huarique ->
            val matchesSearch = searchQuery.isEmpty() ||
                huarique.name.contains(searchQuery, ignoreCase = true) ||
                huarique.district.contains(searchQuery, ignoreCase = true) ||
                huarique.category.contains(searchQuery, ignoreCase = true)
            val matchesCategory = selectedCategory == "Todas" || huarique.category == selectedCategory
            matchesSearch && matchesCategory
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "PuntoSabor 🍽️",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = SurfaceColor
                        )
                        Text(
                            text = "Lima, Perú",
                            fontSize = 12.sp,
                            color = SurfaceColor.copy(alpha = 0.8f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onProfileClick) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(SurfaceColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Person, contentDescription = "Perfil", tint = SurfaceColor)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = OrangePrimary)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmWhite),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Banner hero
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(colors = listOf(OrangePrimary, OrangePrimary.copy(alpha = 0.0f)))
                        )
                        .padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Column {
                        Text(
                            text = "Descubre los mejores",
                            fontSize = 16.sp,
                            color = SurfaceColor.copy(alpha = 0.9f)
                        )
                        Text(
                            text = "Huariques cerca de ti",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = SurfaceColor
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // Barra de búsqueda
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar huariques, distritos...", color = TextSecondary) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = OrangePrimary) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Close, null, tint = TextSecondary)
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = OrangePrimary,
                                unfocusedBorderColor = Color.Transparent,
                                focusedContainerColor = SurfaceColor,
                                unfocusedContainerColor = SurfaceColor
                            )
                        )
                    }
                }
            }

            // Banner propietario
            if (userRole == UserRole.OWNER) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = BrownDark)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("🏪", fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Eres propietario", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = SurfaceColor)
                                Text("Gestiona tus locales y promos", fontSize = 12.sp, color = SurfaceColor.copy(alpha = 0.75f))
                            }
                            TextButton(onClick = onProfileClick) {
                                Text("Mi panel", color = OrangeDark, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }

            // Categorías
            item {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        text = "Categorías",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BrownDark,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(sampleCategories) { category ->
                            CategoryChip(
                                category = category,
                                selected = selectedCategory == category.name,
                                onClick = { selectedCategory = category.name }
                            )
                        }
                    }
                }
            }

            // Título resultados
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isEmpty() && selectedCategory == "Todas") "Destacados 🔥"
                               else "${filteredHuariques.size} resultados",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = BrownDark
                    )
                    TextButton(onClick = {}) {
                        Text("Ver todos", color = OrangePrimary, fontSize = 13.sp)
                    }
                }
            }

            // Lista de huariques
            if (filteredHuariques.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🔍", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("No encontramos huariques", fontWeight = FontWeight.SemiBold, color = TextPrimary)
                            Text("Intenta con otra búsqueda", color = TextSecondary, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(filteredHuariques) { huarique ->
                    HuariqueCard(
                        huarique = huarique,
                        onClick = { onHuariqueClick(huarique.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: Category,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) OrangePrimary else SurfaceColor,
        shadowElevation = if (selected) 4.dp else 1.dp,
        border = if (!selected) androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)) else null
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(category.icon, fontSize = 16.sp)
            Text(
                text = category.name,
                fontSize = 13.sp,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (selected) SurfaceColor else TextPrimary
            )
        }
    }
}

@Composable
fun HuariqueCard(
    huarique: Huarique,
    onClick: () -> Unit
) {
    var isFav by remember { mutableStateOf(huarique.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            // Imagen placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(
                        Brush.horizontalGradient(colors = listOf(OrangeLight, YellowGreen.copy(alpha = 0.5f)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🍽️", fontSize = 56.sp)
                // Badge categoría
                Surface(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(10.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = OrangePrimary
                ) {
                    Text(
                        huarique.category,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        color = SurfaceColor,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                // Botón favorito
                IconButton(
                    onClick = { isFav = !isFav },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null,
                        tint = if (isFav) ErrorRed else SurfaceColor
                    )
                }
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = huarique.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = BrownDark,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, null, tint = StarYellow, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "%.1f".format(huarique.rating),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = BrownDark
                        )
                        Text(" (${huarique.reviewCount})", fontSize = 12.sp, color = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = OrangePrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${huarique.district} · ${huarique.address}",
                        fontSize = 13.sp,
                        color = TextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null, tint = OrangePrimary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(huarique.hours, fontSize = 13.sp, color = TextSecondary)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text("Ver detalles", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
