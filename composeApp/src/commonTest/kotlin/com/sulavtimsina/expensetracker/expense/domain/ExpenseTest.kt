package com.sulavtimsina.expensetracker.expense.domain

import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ExpenseTest {

    @Test
    fun `expense creation with all fields`() {
        val expense = Expense(
            id = "1",
            amount = 25.50,
            category = ExpenseCategory.FOOD,
            note = "Lunch at restaurant",
            date = LocalDateTime(2024, 1, 15, 12, 30),
            imagePath = "/path/to/receipt.jpg"
        )

        assertEquals("1", expense.id)
        assertEquals(25.50, expense.amount)
        assertEquals(ExpenseCategory.FOOD, expense.category)
        assertEquals("Lunch at restaurant", expense.note)
        assertEquals(LocalDateTime(2024, 1, 15, 12, 30), expense.date)
        assertEquals("/path/to/receipt.jpg", expense.imagePath)
    }

    @Test
    fun `expense creation without optional fields`() {
        val expense = Expense(
            id = "2",
            amount = 100.0,
            category = ExpenseCategory.BILLS,
            note = null,
            date = LocalDateTime(2024, 1, 15, 12, 30),
            imagePath = null
        )

        assertEquals("2", expense.id)
        assertEquals(100.0, expense.amount)
        assertEquals(ExpenseCategory.BILLS, expense.category)
        assertNull(expense.note)
        assertNull(expense.imagePath)
        assertNotNull(expense.date)
    }
}
