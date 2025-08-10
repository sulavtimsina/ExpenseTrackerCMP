package com.sulavtimsina.expensetracker.core.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

// Platform-specific URL configuration
expect fun getSupabaseUrl(): String

object SupabaseClient {
    val client =
        createSupabaseClient(
            supabaseUrl = getSupabaseUrl(), // Platform-specific URL
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZS1kZW1vIiwicm9sZSI6ImFub24iLCJleHAiOjE5ODM4MTI5OTZ9.CRXP1A7WOeoJeXxjNni43kdQwgnWNReilDMblYTn_I0", // Local anon key
        ) {
            install(Postgrest)
            install(Auth)
            install(Realtime)
        }
}
