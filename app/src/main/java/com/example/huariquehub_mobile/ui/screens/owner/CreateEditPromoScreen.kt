@file:Suppress("UNUSED_VALUE")
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huariquehub_mobile.data.model.sampleHuariques
import com.example.huariquehub_mobile.data.model.samplePromos
import com.example.huariquehub_mobile.ui.theme.*

// ── Shared helpers (mirrored from CreateEditHuariqueScreen — file-private scope) ──

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
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = OrangePrimary,
    unfocusedBorderColor = DividerWarm,
    focusedLabelColor = OrangePrimary,
    cursorColor = OrangePrimary,
    focusedContainerColor = SurfaceColor,
    unfocusedContainerColor = SurfaceColor
)

// ────────────────────────────────────────────────────────────────────────────────

private data class PromoType(val key: String, val label: String, val emoji: String)

private val promoTypes = listOf(
    PromoType("2x1",        "2×1",          "🍗"),
    PromoType("descuento",  "Descuento %",  "%"),
    PromoType("menu",       "Menú especial","🍽️"),
    PromoType("happy-hour", "Happy hour",   "🍹"),
    PromoType("otro",       "Otro",         "🎉")
)

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNUSED_VALUE")
@Composable
fun CreateEditPromoScreen(
    promoId: Int? = null,
    ownerId: Int = 0,
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val isEditing = promoId != null
    val existing  = remember(promoId) { promoId?.let { id -> samplePromos.find { it.id == id } } }

    var title    by remember { mutableStateOf(existing?.title ?: "") }
    var note     by remember { mutableStateOf(existing?.note ?: "") }
    var typeKey  by remember { mutableStateOf(existing?.type ?: "otro") }
    var discount by remember { mutableStateOf(if ((existing?.discount ?: 0) > 0) existing!!.discount.toString() else "") }
    var code     by remember { mutableStateOf(existing?.code ?: "") }
    var startDate by remember { mutableStateOf(existing?.startDate ?: "") }
    var endDate  by remember { mutableStateOf(existing?.endDate ?: "") }
    var maxUses  by remember { mutableStateOf(existing?.maxUses?.toString() ?: "") }

    var selectedHuariqueId by remember { mutableStateOf(existing?.huariqueId) }

    var expandedType by remember { mutableStateOf(false) }
    var expandedHuarique by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }

    val ownerHuariques = remember(ownerId) {
        sampleHuariques.filter { it.ownerId == ownerId }.ifEmpty { sampleHuariques.take(2) }
    }
    val selectedType = promoTypes.first { it.key == typeKey }
    val showDiscount = typeKey == "descuento" || typeKey == "2x1"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (isEditing) "Editar promo" else "Nueva promo",
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
            // ── Información básica ──────────────────────────────────────────
            SectionLabel("Información básica")

            FormField(label = "Título de la promo *") {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it; errorMessage = "" },
                    placeholder = { Text("Ej. 2×1 Pollo Hoy") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }

            FormField(label = "Tipo de promo *") {
                ExposedDropdownMenuBox(
                    expanded = expandedType,
                    onExpandedChange = { expandedType = it }
                ) {
                    OutlinedTextField(
                        value = "${selectedType.emoji}  ${selectedType.label}",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedType,
                        onDismissRequest = { expandedType = false }
                    ) {
                        promoTypes.forEach { pt ->
                            DropdownMenuItem(
                                text = { Text("${pt.emoji}  ${pt.label}") },
                                onClick = {
                                    typeKey = pt.key
                                    if (!showDiscount) discount = ""
                                    expandedType = false
                                }
                            )
                        }
                    }
                }
            }

            FormField(label = "Descripción / nota *") {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it; errorMessage = "" },
                    placeholder = { Text("Ej. Solo Lun–Vie de 12:00 a 16:00") },
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }

            // ── Descuento ───────────────────────────────────────────────────
            if (showDiscount) {
                SectionLabel("Descuento")

                FormField(label = "Porcentaje de descuento") {
                    OutlinedTextField(
                        value = discount,
                        onValueChange = { discount = it },
                        placeholder = { Text("Ej. 20") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        trailingIcon = {
                            Text("%", fontSize = 14.sp, color = TextSecondary,
                                modifier = Modifier.padding(end = 12.dp))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                }
            }

            // ── Código y límite de uso ──────────────────────────────────────
            SectionLabel("Código y límites")

            FormField(label = "Código de canje (opcional)") {
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    placeholder = { Text("Ej. PROMO20") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }

            FormField(label = "Máximo de usos (opcional)") {
                OutlinedTextField(
                    value = maxUses,
                    onValueChange = { maxUses = it },
                    placeholder = { Text("Ej. 100 — deja vacío para ilimitado") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = fieldColors()
                )
            }

            // ── Vigencia ────────────────────────────────────────────────────
            SectionLabel("Vigencia")

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FormField(label = "Fecha inicio", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = startDate,
                        onValueChange = { startDate = it },
                        placeholder = { Text("AAAA-MM-DD") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                }
                FormField(label = "Fecha fin", modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = endDate,
                        onValueChange = { endDate = it },
                        placeholder = { Text("AAAA-MM-DD") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                }
            }

            // ── Local asociado ──────────────────────────────────────────────
            SectionLabel("Local asociado")

            FormField(label = "Huarique (opcional)") {
                ExposedDropdownMenuBox(
                    expanded = expandedHuarique,
                    onExpandedChange = { expandedHuarique = it }
                ) {
                    OutlinedTextField(
                        value = ownerHuariques.find { it.id == selectedHuariqueId }?.name
                            ?: "Sin asignar",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedHuarique)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true),
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedHuarique,
                        onDismissRequest = { expandedHuarique = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Sin asignar") },
                            onClick = { selectedHuariqueId = null; expandedHuarique = false }
                        )
                        ownerHuariques.forEach { h ->
                            DropdownMenuItem(
                                text = { Text(h.name) },
                                onClick = { selectedHuariqueId = h.id; expandedHuarique = false }
                            )
                        }
                    }
                }
            }

            // ── Error ───────────────────────────────────────────────────────
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = ErrorRed, fontSize = 13.sp)
            }

            // ── Guardar ─────────────────────────────────────────────────────
            Button(
                onClick = {
                    when {
                        title.isBlank() -> errorMessage = "El título es obligatorio."
                        note.isBlank()  -> errorMessage = "La descripción es obligatoria."
                        else -> {
                            isSaving = true
                            // TODO(backend): POST /promos con los campos mapeados
                            onSave()
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        color = SurfaceColor,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        if (isEditing) "Guardar cambios" else "Publicar promo",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
