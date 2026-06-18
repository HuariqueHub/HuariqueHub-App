package com.example.huariquehub_mobile.data.remote

import com.example.huariquehub_mobile.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Definición de los endpoints del backend de PuntoSabor desplegado en Railway.
 * Las rutas coinciden con las documentadas en Swagger.
 */
interface ApiService {

    // ── Auth & Users ────────────────────────────────────────────────────────
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): AuthResponseDto

    @POST("users")
    suspend fun createUser(@Body body: CreateUserRequest): UserDto

    // ── Huariques ───────────────────────────────────────────────────────────
    @GET("huariques")
    suspend fun getHuariques(
        @Query("q") q: String? = null,
        @Query("near") near: Boolean? = null,
        @Query("ownerId") ownerId: Int? = null
    ): List<HuariqueDto>

    @GET("huariques/{id}")
    suspend fun getHuarique(@Path("id") id: Int): HuariqueDto

    @POST("huariques")
    suspend fun createHuarique(@Body body: CreateHuariqueRequest): HuariqueDto

    @PATCH("huariques/{id}")
    suspend fun patchHuarique(
        @Path("id") id: Int,
        @Body patch: Map<String, @JvmSuppressWildcards Any?>
    ): HuariqueDto

    @DELETE("huariques/{id}")
    suspend fun deleteHuarique(@Path("id") id: Int): Response<Unit>

    // ── Categories ──────────────────────────────────────────────────────────
    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>

    // ── Reviews ─────────────────────────────────────────────────────────────
    @GET("reviews")
    suspend fun getReviews(@Query("huariqueId") huariqueId: Int): List<ReviewDto>

    @POST("reviews")
    suspend fun createReview(@Body body: CreateReviewRequest): ReviewDto

    // ── Plans ───────────────────────────────────────────────────────────────
    @GET("plans")
    suspend fun getPlans(): List<PlanDto>

    // ── Promos ──────────────────────────────────────────────────────────────
    @GET("promos")
    suspend fun getPromos(
        @Query("ownerId") ownerId: Int? = null,
        @Query("huariqueId") huariqueId: Int? = null
    ): List<PromoDto>

    @GET("promos/{id}")
    suspend fun getPromo(@Path("id") id: Int): PromoDto

    @POST("promos")
    suspend fun createPromo(@Body body: CreatePromoRequest): PromoDto

    @PATCH("promos/{id}")
    suspend fun patchPromo(
        @Path("id") id: Int,
        @Body patch: Map<String, @JvmSuppressWildcards Any?>
    ): PromoDto

    @DELETE("promos/{id}")
    suspend fun deletePromo(@Path("id") id: Int): Response<Unit>

    // ── Subscriptions ───────────────────────────────────────────────────────
    @GET("subscriptions/active")
    suspend fun getActiveSubscription(@Query("userId") userId: Int): Response<SubscriptionDto>

    @POST("subscriptions")
    suspend fun subscribe(@Body body: CreateSubscriptionRequest): SubscriptionDto
}
