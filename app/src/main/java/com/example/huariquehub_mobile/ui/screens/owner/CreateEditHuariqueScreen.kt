package com.example.huariquehub_mobile.ui.screens.owner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huariquehub_mobile.ui.components.HuariqueImage
import com.example.huariquehub_mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEditHuariqueScreen(
    huariqueId: Int? = null,
    onBack: () -> Unit,
    onSave: () -> Unit,
    viewModel: CreateEditHuariqueViewModel = viewModel()
) {
    val isEditing = huariqueId != null
    LaunchedEffect(huariqueId) { viewModel.init(huariqueId) }
    val existing = viewModel.existing

    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var openAt by remember { mutableStateOf("") }
    var closeAt by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var delivery by remember { mutableStateOf(false) }
    var takeaway by remember { mutableStateOf(false) }
    var dineIn by remember { mutableStateOf(true) }
    var validationError by remember { mutableStateOf("") }
    val isSaving = viewModel.isSaving
    val errorMessage = validationError.ifBlank { viewModel.error.orEmpty() }

    // Prefill al cargar el huarique existente (modo edición).
    LaunchedEffect(existing) {
        existing?.let {
            name = it.name
            category = it.category
            district = it.district
            address = it.address
            phone = it.phone
            price = if (it.price > 0) "%.0f".format(it.price) else ""
            description = it.description
            imageUrl = it.imageUrl
            delivery = it.deliveryAvailable
            takeaway = it.takeawayAvailable
            dineIn = it.dineInAvailable
        }
    }

    val categories = viewModel.categoryNames
    var expandedCategory by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar huarique" else "Nuevo huarique",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = SurfaceColor
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = SurfaceColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BrownDark)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmWhite)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Sección: Información básica
            SectionLabel("Información básica")

            FormField(label = "Nombre del local *") {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it; validationError = "" },
                    placeholder = { Text("Ej. El Brasero de Don Lucho") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }

            // Dropdown categoría
            FormField(label = "Categoría *") {
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = it }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        placeholder = { Text("Selecciona una categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                        modifier = Modifier.fillMaxWidth().menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                    ExposedDropdownMenu(expanded = expandedCategory, onDismissRequest = { expandedCategory = false }) {
                        categories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat) },
                                onClick = { category = cat; expandedCategory = false }
                            )
                        }
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FormField(label = "Distrito *", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = district,
                        onValueChange = { district = it },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                }
                FormField(label = "Precio prom. (S/)", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                }
            }

            // Sección: Contacto y ubicación
            SectionLabel("Contacto y ubicación")

            FormField(label = "Dirección") {
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    placeholder = { Text("Ej. Jr. Ucayali 342") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }

            FormField(label = "Teléfono") {
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    placeholder = { Text("+51 9XX XXX XXX") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }

            // Sección: Horarios
            SectionLabel("Horarios")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FormField(label = "Apertura", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = openAt,
                        onValueChange = { openAt = it },
                        placeholder = { Text("09:00") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                }
                FormField(label = "Cierre", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = closeAt,
                        onValueChange = { closeAt = it },
                        placeholder = { Text("21:00") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                }
            }

            // Sección: Descripción
            SectionLabel("Descripción")

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Cuéntanos sobre tu huarique...") },
                minLines = 3,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors()
            )

            // Sección: Foto del local
            SectionLabel("Foto del local")

            FormField(label = "URL de la imagen") {
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    placeholder = { Text("https://...") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }
            // Previsualización de la imagen que verán los exploradores.
            HuariqueImage(
                name = name.ifBlank { null },
                url = imageUrl.ifBlank { null },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp)),
                emojiSize = 44.sp
            )

            // Sección: Servicios
            SectionLabel("Servicios disponibles")

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceColor),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(4.dp)) {
                    ServiceToggle("Delivery 🛵", delivery) { delivery = it }
                    HorizontalDivider(color = DividerWarm.copy(alpha = 0.5f))
                    ServiceToggle("Para llevar 🥡", takeaway) { takeaway = it }
                    HorizontalDivider(color = DividerWarm.copy(alpha = 0.5f))
                    ServiceToggle("Comer en local 🪑", dineIn) { dineIn = it }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = ErrorRed, fontSize = 13.sp)
            }

            // Botón guardar
            Button(
                onClick = {
                    when {
                        name.isBlank() -> validationError = "El nombre es obligatorio."
                        category.isBlank() -> validationError = "Selecciona una categoría."
                        district.isBlank() -> validationError = "El distrito es obligatorio."
                        else -> {
                            validationError = ""
                            viewModel.save(
                                id = huariqueId,
                                name = name,
                                category = category,
                                district = district,
                                address = address,
                                priceText = price,
                                description = description,
                                imageUrl = imageUrl
                            ) { onSave() }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = SurfaceColor, modifier = Modifier.size(20.dp))
                } else {
                    Text(
                        if (isEditing) "Guardar cambios" else "Publicar huarique",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.SemiBold,
        color = TextTertiary,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun FormField(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = TextSecondary)
        content()
    }
}

@Composable
private fun ServiceToggle(label: String, checked: Boolean, onToggle: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 14.sp, color = TextPrimary)
        Switch(
            checked = checked,
            onCheckedChange = onToggle,
            colors = SwitchDefaults.colors(
                checkedThumbColor = SurfaceColor,
                checkedTrackColor = OrangePrimary,
                uncheckedThumbColor = TextTertiary,
                uncheckedTrackColor = BackgroundSoft
            )
        )
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = OrangePrimary,
    unfocusedBorderColor = DividerWarm,
    focusedLabelColor = OrangePrimary,
    cursorColor = OrangePrimary,
    focusedContainerColor = SurfaceColor,
    unfocusedContainerColor = SurfaceColor
)
