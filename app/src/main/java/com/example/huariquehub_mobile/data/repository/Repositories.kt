package com.example.huariquehub_mobile.data.repository

import com.example.huariquehub_mobile.data.model.Category
import com.example.huariquehub_mobile.data.model.Huarique
import com.example.huariquehub_mobile.data.model.Plan
import com.example.huariquehub_mobile.data.model.Promo
import com.example.huariquehub_mobile.data.model.Review
import com.example.huariquehub_mobile.data.model.Subscription
import com.example.huariquehub_mobile.data.model.UserRole
import com.example.huariquehub_mobile.data.model.UserSession
import com.example.huariquehub_mobile.data.remote.ApiClient
import com.example.huariquehub_mobile.data.remote.dto.AuthResponseDto
import com.example.huariquehub_mobile.data.remote.dto.CreateHuariqueRequest
import com.example.huariquehub_mobile.data.remote.dto.CreatePromoRequest
import com.example.huariquehub_mobile.data.remote.dto.CreateReviewRequest
import com.example.huariquehub_mobile.data.remote.dto.CreateSubscriptionRequest
import com.example.huariquehub_mobile.data.remote.dto.CreateUserRequest
import com.example.huariquehub_mobile.data.remote.dto.LoginRequest
import com.example.huariquehub_mobile.data.remote.toModel

private val api get() = ApiClient.api

class AuthRepository {

    suspend fun login(email: String, password: String): UserSession =
        api.login(LoginRequest(email.trim(), password)).toSession()

    suspend fun register(
        name: String,
        email: String,
        password: String,
        role: UserRole
    ): UserSession {
        val roleStr = if (role == UserRole.OWNER) "owner" else "consumer"
        api.createUser(CreateUserRequest(name.trim(), email.trim(), password, roleStr))
        // POST /users no devuelve token: iniciamos sesión para obtenerlo.
        return login(email, password)
    }

    private fun AuthResponseDto.toSession() = UserSession(
        id = id,
        name = name,
        email = email,
        role = if (role.equals("owner", ignoreCase = true)) UserRole.OWNER else UserRole.CONSUMER,
        token = token.orEmpty()
    )
}

class HuariqueRepository {

    suspend fun getHuariques(): List<Huarique> =
        api.getHuariques().map { it.toModel() }

    suspend fun getHuariquesByOwner(ownerId: Int): List<Huarique> =
        api.getHuariques(ownerId = ownerId).map { it.toModel() }

    suspend fun getHuarique(id: Int): Huarique =
        api.getHuarique(id).toModel()

    suspend fun getCategories(): List<Category> =
        api.getCategories().map { it.toModel() }

    suspend fun createHuarique(
        name: String,
        category: String,
        categoryId: Int,
        price: Double,
        district: String,
        address: String?,
        description: String?,
        ownerId: Int?
    ): Huarique = api.createHuarique(
        CreateHuariqueRequest(
            name = name,
            category = category,
            categoryId = categoryId,
            price = price,
            district = district,
            address = address,
            description = description,
            ownerId = ownerId
        )
    ).toModel()

    suspend fun updateHuarique(id: Int, patch: Map<String, Any?>): Huarique =
        api.patchHuarique(id, patch).toModel()

    suspend fun deleteHuarique(id: Int) {
        api.deleteHuarique(id)
    }

    suspend fun getReviews(huariqueId: Int): List<Review> =
        api.getReviews(huariqueId).map { it.toModel() }

    suspend fun addReview(huariqueId: Int, userId: Int, rating: Int, comment: String): Review =
        api.createReview(CreateReviewRequest(huariqueId, userId, rating, comment)).toModel()
}

class MembershipRepository {

    suspend fun getPlans(): List<Plan> =
        api.getPlans().map { it.toModel() }

    suspend fun getActiveSubscription(userId: Int): Subscription? {
        val response = api.getActiveSubscription(userId)
        return if (response.isSuccessful) response.body()?.toModel() else null
    }

    suspend fun subscribe(userId: Int, planId: String): Subscription =
        api.subscribe(CreateSubscriptionRequest(userId, planId)).toModel()

    suspend fun getPromosByOwner(ownerId: Int): List<Promo> =
        api.getPromos(ownerId = ownerId).map { it.toModel() }

    suspend fun getPromo(id: Int): Promo =
        api.getPromo(id).toModel()

    suspend fun createPromo(
        title: String,
        note: String,
        huariqueId: Int?,
        type: String,
        discount: Int
    ): Promo = api.createPromo(
        CreatePromoRequest(
            title = title,
            note = note,
            huariqueId = huariqueId,
            type = type,
            discount = discount
        )
    ).toModel()

    suspend fun updatePromo(id: Int, patch: Map<String, Any?>): Promo =
        api.patchPromo(id, patch).toModel()

    suspend fun deletePromo(id: Int) {
        api.deletePromo(id)
    }
}
