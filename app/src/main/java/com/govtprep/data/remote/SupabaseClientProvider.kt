package com.govtprep.data.remote

import com.govtprep.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    private const val DEFAULT_URL = "https://your-project-ref.supabase.co"
    private const val DEFAULT_ANON_KEY = "your-anon-key"

    private var cachedClient: SupabaseClient? = null

    fun clientOrNull(): SupabaseClient? = runCatching { clientOrThrow() }.getOrNull()

    fun clientOrThrow(): SupabaseClient {
        cachedClient?.let { return it }

        val rawUrl = BuildConfig.SUPABASE_URL.trim().removeSurrounding("\"")
        val rawKey = BuildConfig.SUPABASE_ANON_KEY.trim().removeSurrounding("\"")
        val url = normalizeUrl(rawUrl)
        val key = rawKey

        val looksConfigured =
            url.isNotBlank() &&
                key.isNotBlank() &&
                url != DEFAULT_URL &&
                key != DEFAULT_ANON_KEY

        if (!looksConfigured) {
            error(
                "Supabase config invalid. Check SUPABASE_URL and SUPABASE_ANON_KEY in BuildConfig. " +
                    "Current URL: '$url'"
            )
        }

        val created = runCatching {
            createSupabaseClient(
                supabaseUrl = url,
                supabaseKey = key
            ) {
                install(Auth)
                install(Postgrest)
            }
        }.getOrElse { throwable ->
            error(
                "Supabase client initialization failed. Verify URL/key, internet connectivity, and project status. Cause: ${throwable.message}"
            )
        }

        cachedClient = created
        return created
    }

    private fun normalizeUrl(url: String): String =
        if (url.startsWith("http://", ignoreCase = true) || url.startsWith("https://", ignoreCase = true)) url else "https://$url"
}

