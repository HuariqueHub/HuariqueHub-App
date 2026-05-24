package com.example.huariquehub_mobile.ui.screens.subscription

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.huariquehub_mobile.data.model.Plan
import com.example.huariquehub_mobile.data.model.samplePlans
import com.example.huariquehub_mobile.data.model.sampleSubscriptions
import com.example.huariquehub_mobile.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("UNUSED_VALUE")
@Composable
fun SubscriptionScreen(
    userId: Int,
    onBack: () -> Unit
) {
    val activeSub = remember(userId) {
        sampleSubscriptions.firstOrNull { it.userId == userId && it.isActive }
    }
    var selectedPlanId by remember { mutableStateOf(activeSub?.planId ?: "basic") }
    var showConfirmDialog by remember { mutableStateOf<Plan?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Mi Suscripción",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = SurfaceColor
                        )
                        Text(
                            activeSub?.let { "Plan ${it.planName ?: it.planId} activo" }
                                ?: "Sin plan activo",
                            fontSize = 12.sp,
                            color = SurfaceColor.copy(alpha = 0.8f)
                        )
                    }
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(WarmWhite),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Plan activo banner
            activeSub?.let { sub ->
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(listOf(BrownDark, BrownMedium))
                            )
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "Plan actual",
                                    fontSize = 11.sp,
                                    color = SurfaceColor.copy(alpha = 0.7f),
                                    letterSpacing = 0.8.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    sub.planName ?: sub.planId,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SurfaceColor
                                )
                                sub.planPrice?.let { price ->
                                    Text(
                                        if (price == 0f) "Gratis" else "S/ %.0f / mes".format(price),
                                        fontSize = 13.sp,
                                        color = OrangeDark
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = OrangePrimary.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    "Activo",
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                    fontSize = 12.sp,
                                    color = OrangeDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }

            // Sección título
            item {
                Text(
                    text = if (activeSub != null) "Cambiar de plan" else "Elige tu plan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BrownDark,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
                )
            }

            // Plan cards
            items(samplePlans) { plan ->
                PlanCard(
                    plan = plan,
                    isCurrentPlan = activeSub?.planId == plan.id,
                    isSelected = selectedPlanId == plan.id,
                    onSelect = { selectedPlanId = plan.id }
                )
            }

            // CTA button
            item {
                Spacer(modifier = Modifier.height(8.dp))
                val target = samplePlans.first { it.id == selectedPlanId }
                val isSamePlan = activeSub?.planId == selectedPlanId

                Button(
                    onClick = { if (!isSamePlan) showConfirmDialog = target },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSamePlan) DividerWarm else OrangePrimary,
                        contentColor = if (isSamePlan) TextSecondary else SurfaceColor
                    ),
                    enabled = !isSamePlan
                ) {
                    Text(
                        if (isSamePlan) "Este es tu plan actual"
                        else if (target.price == 0f) "Suscribirme gratis"
                        else "Suscribirme — S/ %.0f/mes".format(target.price),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Cancel link
                if (activeSub != null && activeSub.planId != "basic") {
                    Spacer(modifier = Modifier.height(12.dp))
                    TextButton(
                        onClick = { /* TODO(backend): POST /subscriptions/{id}/cancel */ },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Cancelar suscripción", color = ErrorRed, fontSize = 13.sp)
                    }
                }
            }
        }
    }

    // Confirm subscribe dialog
    showConfirmDialog?.let { plan ->
        AlertDialog(
            onDismissRequest = { showConfirmDialog = null },
            title = { Text("Confirmar suscripción") },
            text = {
                Text(
                    if (plan.price == 0f)
                        "¿Cambiar al plan ${plan.name} (gratis)?"
                    else
                        "¿Suscribirte al plan ${plan.name} por S/ %.0f/mes?".format(plan.price)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    // TODO(backend): POST /subscriptions
                    showConfirmDialog = null
                }) {
                    Text("Confirmar", color = OrangePrimary, fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = null }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }
}

@Composable
private fun PlanCard(
    plan: Plan,
    isCurrentPlan: Boolean,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor = when {
        isCurrentPlan -> OrangePrimary
        isSelected    -> BrownMedium
        else          -> DividerWarm
    }
    val isPopular = plan.id == "premium"

    Card(
        onClick = onSelect,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(
                width = if (isSelected || isCurrentPlan) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) OrangeLight else SurfaceColor
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        plan.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = BrownDark
                    )
                    if (isPopular) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = OrangePrimary
                        ) {
                            Text(
                                "Popular",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                color = SurfaceColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    if (isCurrentPlan) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = BrownDark
                        ) {
                            Text(
                                "Actual",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                fontSize = 10.sp,
                                color = SurfaceColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Text(
                    if (plan.price == 0f) "Gratis" else "S/ %.0f".format(plan.price),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = if (isSelected) OrangePrimary else BrownDark
                )
            }

            if (plan.price > 0f) {
                Text("por mes", fontSize = 11.sp, color = TextTertiary)
            }

            Spacer(modifier = Modifier.height(10.dp))

            plan.features.forEach { feature ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = OrangePrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(feature, fontSize = 13.sp, color = TextSecondary)
                }
            }
        }
    }
}
