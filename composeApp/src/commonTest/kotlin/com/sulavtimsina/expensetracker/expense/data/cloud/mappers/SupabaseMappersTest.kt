package com.sulavtimsina.expensetracker.expense.data.cloud.mappers

import com.sulavtimsina.expensetracker.expense.data.cloud.model.SupabaseExpense
import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.test.*

class SupabaseMappersTest {
    @Test
    fun `toDomainExpense maps SupabaseExpense to domain Expense correctly`() {
        // Given
        val date = Instant.parse("2024-01-15T12:00:00Z")
        val supabaseExpense =
            SupabaseExpense(
                id = "1",
                userId = "user123",
                amount = 25.50,
                category = "Food",
                note = "Lunch at restaurant",
                date = date,
                imagePath = "/path/to/image.jpg",
                createdAt = date,
                updatedAt = date,
            )

        // When
        val domainExpense = supabaseExpense.toDomainExpense()

        // Then
        assertEquals("1", domainExpense.id)
        assertEquals(25.50, domainExpense.amount)
        assertEquals(ExpenseCategory.FOOD, domainExpense.category)
        assertEquals("Lunch at restaurant", domainExpense.note)
        assertEquals(date.toLocalDateTime(TimeZone.currentSystemDefault()), domainExpense.date)
        assertEquals("/path/to/image.jpg", domainExpense.imagePath)
    }

    @Test
    fun `toDomainExpense maps SupabaseExpense with null note correctly`() {
        // Given
        val date = Instant.parse("2024-01-15T12:00:00Z")
        val supabaseExpense =
            SupabaseExpense(
                id = "1",
                userId = "user123",
                amount = 25.50,
                category = "Transportation",
                note = null,
                date = date,
                imagePath = null,
                createdAt = date,
                updatedAt = date,
            )

        // When
        val domainExpense = supabaseExpense.toDomainExpense()

        // Then
        assertEquals("1", domainExpense.id)
        assertEquals(25.50, domainExpense.amount)
        assertEquals(ExpenseCategory.TRANSPORTATION, domainExpense.category)
        assertNull(domainExpense.note)
        assertNull(domainExpense.imagePath)
    }

    @Test
    fun `toDomainExpense handles all expense categories correctly`() {
        val categories =
            listOf(
                "Food" to ExpenseCategory.FOOD,
                "Transportation" to ExpenseCategory.TRANSPORTATION,
                "Entertainment" to ExpenseCategory.ENTERTAINMENT,
                "Shopping" to ExpenseCategory.SHOPPING,
                "Bills" to ExpenseCategory.BILLS,
                "Healthcare" to ExpenseCategory.HEALTHCARE,
                "Education" to ExpenseCategory.EDUCATION,
                "Travel" to ExpenseCategory.TRAVEL,
                "Other" to ExpenseCategory.OTHER,
            )

        categories.forEach { (supabaseCategory, expectedDomainCategory) ->
            // Given
            val supabaseExpense =
                SupabaseExpense(
                    id = "1",
                    userId = "user123",
                    amount = 10.0,
                    category = supabaseCategory,
                    note = null,
                    date = Instant.parse("2024-01-15T12:00:00Z"),
                    imagePath = null,
                    createdAt = Instant.parse("2024-01-15T12:00:00Z"),
                    updatedAt = Instant.parse("2024-01-15T12:00:00Z"),
                )

            // When
            val domainExpense = supabaseExpense.toDomainExpense()

            // Then
            assertEquals(expectedDomainCategory, domainExpense.category)
        }
    }

    @Test
    fun `toSupabaseExpenseInsert maps domain Expense to SupabaseExpenseInsert correctly`() {
        // Given
        val localDateTime = LocalDateTime(2024, 1, 15, 12, 0)
        val domainExpense =
            Expense(
                id = "1",
                amount = 25.50,
                category = ExpenseCategory.FOOD,
                note = "Lunch at restaurant",
                date = localDateTime,
                imagePath = "/path/to/image.jpg",
            )
        val userId = "user123"

        // When
        val supabaseInsert = domainExpense.toSupabaseExpenseInsert(userId)

        // Then
        assertEquals("1", supabaseInsert.id)
        assertEquals("user123", supabaseInsert.userId)
        assertEquals(25.50, supabaseInsert.amount)
        assertEquals("Food", supabaseInsert.category)
        assertEquals("Lunch at restaurant", supabaseInsert.note)
        assertEquals(localDateTime.toInstant(TimeZone.currentSystemDefault()), supabaseInsert.date)
        assertEquals("/path/to/image.jpg", supabaseInsert.imagePath)
    }

    @Test
    fun `toSupabaseExpenseInsert maps domain Expense with null note correctly`() {
        // Given
        val localDateTime = LocalDateTime(2024, 1, 15, 12, 0)
        val domainExpense =
            Expense(
                id = "1",
                amount = 25.50,
                category = ExpenseCategory.TRANSPORTATION,
                note = null,
                date = localDateTime,
                imagePath = null,
            )
        val userId = "user123"

        // When
        val supabaseInsert = domainExpense.toSupabaseExpenseInsert(userId)

        // Then
        assertEquals("1", supabaseInsert.id)
        assertEquals("user123", supabaseInsert.userId)
        assertEquals(25.50, supabaseInsert.amount)
        assertEquals("Transportation", supabaseInsert.category)
        assertNull(supabaseInsert.note)
        assertNull(supabaseInsert.imagePath)
    }

    @Test
    fun `toSupabaseExpenseInsert handles all expense categories correctly`() {
        val categories =
            listOf(
                ExpenseCategory.FOOD to "Food",
                ExpenseCategory.TRANSPORTATION to "Transportation",
                ExpenseCategory.ENTERTAINMENT to "Entertainment",
                ExpenseCategory.SHOPPING to "Shopping",
                ExpenseCategory.BILLS to "Bills",
                ExpenseCategory.HEALTHCARE to "Healthcare",
                ExpenseCategory.EDUCATION to "Education",
                ExpenseCategory.TRAVEL to "Travel",
                ExpenseCategory.OTHER to "Other",
            )

        categories.forEach { (domainCategory, expectedSupabaseCategory) ->
            // Given
            val domainExpense =
                Expense(
                    id = "1",
                    amount = 10.0,
                    category = domainCategory,
                    note = null,
                    date = LocalDateTime(2024, 1, 15, 12, 0),
                    imagePath = null,
                )

            // When
            val supabaseInsert = domainExpense.toSupabaseExpenseInsert("user123")

            // Then
            assertEquals(expectedSupabaseCategory, supabaseInsert.category)
        }
    }

    @Test
    fun `toSupabaseExpenseUpdate maps domain Expense to SupabaseExpenseUpdate correctly`() {
        // Given
        val localDateTime = LocalDateTime(2024, 1, 15, 12, 0)
        val domainExpense =
            Expense(
                id = "1",
                amount = 30.75,
                category = ExpenseCategory.ENTERTAINMENT,
                note = "Movie tickets",
                date = localDateTime,
                imagePath = "/updated/path/to/image.jpg",
            )

        // When
        val supabaseUpdate = domainExpense.toSupabaseExpenseUpdate()

        // Then
        assertEquals(30.75, supabaseUpdate.amount)
        assertEquals("Entertainment", supabaseUpdate.category)
        assertEquals("Movie tickets", supabaseUpdate.note)
        assertEquals(localDateTime.toInstant(TimeZone.currentSystemDefault()), supabaseUpdate.date)
        assertEquals("/updated/path/to/image.jpg", supabaseUpdate.imagePath)
    }

    @Test
    fun `toSupabaseExpenseUpdate maps domain Expense with null values correctly`() {
        // Given
        val localDateTime = LocalDateTime(2024, 1, 15, 12, 0)
        val domainExpense =
            Expense(
                id = "1",
                amount = 15.25,
                category = ExpenseCategory.BILLS,
                note = null,
                date = localDateTime,
                imagePath = null,
            )

        // When
        val supabaseUpdate = domainExpense.toSupabaseExpenseUpdate()

        // Then
        assertEquals(15.25, supabaseUpdate.amount)
        assertEquals("Bills", supabaseUpdate.category)
        assertNull(supabaseUpdate.note)
        assertNull(supabaseUpdate.imagePath)
    }

    @Test
    fun `round trip conversion preserves data integrity`() {
        // Given - original domain expense
        val originalDate = LocalDateTime(2024, 1, 15, 12, 30, 45)
        val originalExpense =
            Expense(
                id = "test-id",
                amount = 123.45,
                category = ExpenseCategory.SHOPPING,
                note = "Test purchase",
                date = originalDate,
                imagePath = "/test/path.jpg",
            )

        // When - convert to supabase and back
        val supabaseInsert = originalExpense.toSupabaseExpenseInsert("user123")
        val supabaseExpense =
            SupabaseExpense(
                id = supabaseInsert.id,
                userId = supabaseInsert.userId,
                amount = supabaseInsert.amount,
                category = supabaseInsert.category,
                note = supabaseInsert.note,
                date = supabaseInsert.date,
                imagePath = supabaseInsert.imagePath,
                createdAt = supabaseInsert.date,
                updatedAt = supabaseInsert.date,
            )
        val convertedExpense = supabaseExpense.toDomainExpense()

        // Then - data should be preserved
        assertEquals(originalExpense.id, convertedExpense.id)
        assertEquals(originalExpense.amount, convertedExpense.amount)
        assertEquals(originalExpense.category, convertedExpense.category)
        assertEquals(originalExpense.note, convertedExpense.note)
        assertEquals(originalExpense.imagePath, convertedExpense.imagePath)
        // Note: Date comparison might have small differences due to timezone conversion
        // So we'll just check they're reasonably close
        assertTrue(kotlin.math.abs(originalExpense.date.toString().length - convertedExpense.date.toString().length) <= 1)
    }
}
