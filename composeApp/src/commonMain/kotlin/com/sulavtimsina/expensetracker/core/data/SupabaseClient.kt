package com.sulavtimsina.expensetracker.core.data

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.gotrue.Auth

object SupabaseClient {
    val client = createSupabaseClient(
//        supabaseUrl = "http://localhost:54321", // Will be replaced with actual URL
        supabaseUrl = "https://sgbpdfudbbataqzbsuzm.supabase.co", // Will be replaced with actual URL
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InNnYnBkZnVkYmJhdGFxemJzdXptIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NTAxMDE1MDYsImV4cCI6MjA2NTY3NzUwNn0.UyZ0DR4ECM0-ztHrEvd08nZwjnQ_EHJrcCzn3C8PZDQ" // Local anon key
//        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZS1kZW1vIiwicm9sZSI6ImFub24iLCJleHAiOjE5ODM4MTI5OTZ9.CRXP1A7WOeoJeXxjNni43kdQwgnWNReilDMblYTn_I0" // Local anon key
    ) {
        install(Postgrest)
        install(Auth)
        install(Realtime)
    }
}
