package com.govtprep.data.remote

import com.govtprep.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClientProvider {
    private var cachedClient: SupabaseClient? = null

    fun clientOrNull(): SupabaseClient? = runCatching { clientOrThrow() }.getOrNull()

    fun clientOrThrow(): SupabaseClient {
        cachedClient?.let { return it }

        val url = BuildConfig.SUPABASE_URL.trim()
        val key = BuildConfig.SUPABASE_ANON_KEY.trim()

        val looksConfigured =
            url.isNotBlank() &&
                key.isNotBlank() &&
                url.startsWith("https://", ignoreCase = true) &&
                !url.contains("your-project-ref", ignoreCase = true) &&
                !key.contains("your-anon-key", ignoreCase = true)

        if (!looksConfigured) {
            error("Supabase config invalid. Check SUPABASE_URL and SUPABASE_ANON_KEY in BuildConfig.")
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
}
