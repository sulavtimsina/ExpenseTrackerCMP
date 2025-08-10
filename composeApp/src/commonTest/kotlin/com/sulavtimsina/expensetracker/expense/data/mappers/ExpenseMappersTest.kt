package com.sulavtimsina.expensetracker.expense.data.mappers

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import com.sulavtimsina.expensetracker.database.Expense as DatabaseExpense

class ExpenseMappersTest {
    @Test
    fun `toDomainExpense maps all fields correctly`() {
        val databaseExpense =
            DatabaseExpense(
                id = "1",
                amount = 25.50,
                category = "Food",
                note = "Lunch",
                date = "2024-01-15T12:30:00",
                imagePath = "/path/to/image.jpg",
            )

        val domainExpense = databaseExpense.toDomainExpense()

        assertEquals("1", domainExpense.id)
        assertEquals(25.50, domainExpense.amount)
        assertEquals(ExpenseCategory.FOOD, domainExpense.category)
        assertEquals("Lunch", domainExpense.note)
        assertEquals(LocalDateTime(2024, 1, 15, 12, 30), domainExpense.date)
        assertEquals("/path/to/image.jpg", domainExpense.imagePath)
    }

    @Test
    fun `toDomainExpense handles null fields`() {
        val databaseExpense =
            DatabaseExpense(
                id = "2",
                amount = 100.0,
                category = "Bills",
                note = null,
                date = "2024-01-15T12:30:00",
                imagePath = null,
            )

        val domainExpense = databaseExpense.toDomainExpense()

        assertEquals("2", domainExpense.id)
        assertEquals(100.0, domainExpense.amount)
        assertEquals(ExpenseCategory.BILLS, domainExpense.category)
        assertNull(domainExpense.note)
        assertNull(domainExpense.imagePath)
    }

    @Test
    fun `toDatabaseExpense maps all fields correctly`() {
        val domainExpense =
            Expense(
                id = "3",
                amount = 50.0,
                category = ExpenseCategory.TRANSPORTATION,
                note = "Bus ticket",
                date = LocalDateTime(2024, 1, 15, 12, 30),
                imagePath = "/path/to/ticket.jpg",
            )

        val databaseExpense = domainExpense.toDatabaseExpense()

        assertEquals("3", databaseExpense.id)
        assertEquals(50.0, databaseExpense.amount)
        assertEquals("Transportation", databaseExpense.category)
        assertEquals("Bus ticket", databaseExpense.note)
        assertEquals("2024-01-15T12:30", databaseExpense.date)
        assertEquals("/path/to/ticket.jpg", databaseExpense.imagePath)
    }

    @Test
    fun `toDatabaseExpense handles null fields`() {
        val domainExpense =
            Expense(
                id = "4",
                amount = 75.0,
                category = ExpenseCategory.OTHER,
                note = null,
                date = LocalDateTime(2024, 1, 15, 12, 30),
                imagePath = null,
            )

        val databaseExpense = domainExpense.toDatabaseExpense()

        assertEquals("4", databaseExpense.id)
        assertEquals(75.0, databaseExpense.amount)
        assertEquals("Other", databaseExpense.category)
        assertNull(databaseExpense.note)
        assertNull(databaseExpense.imagePath)
    }

    @Test
    fun `roundtrip conversion preserves data`() {
        val originalExpense =
            Expense(
                id = "5",
                amount = 42.99,
                category = ExpenseCategory.ENTERTAINMENT,
                note = "Movie ticket",
                date = LocalDateTime(2024, 1, 15, 19, 45),
                imagePath = "/path/to/receipt.jpg",
            )

        val roundtripExpense = originalExpense.toDatabaseExpense().toDomainExpense()

        assertEquals(originalExpense.id, roundtripExpense.id)
        assertEquals(originalExpense.amount, roundtripExpense.amount)
        assertEquals(originalExpense.category, roundtripExpense.category)
        assertEquals(originalExpense.note, roundtripExpense.note)
        assertEquals(originalExpense.date, roundtripExpense.date)
        assertEquals(originalExpense.imagePath, roundtripExpense.imagePath)
    }
}
