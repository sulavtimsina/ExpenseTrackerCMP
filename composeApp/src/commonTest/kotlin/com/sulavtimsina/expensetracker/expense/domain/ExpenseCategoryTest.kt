package com.sulavtimsina.expensetracker.expense.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ExpenseCategoryTest {

    @Test
    fun `fromDisplayName returns correct category`() {
        assertEquals(ExpenseCategory.FOOD, ExpenseCategory.fromDisplayName("Food"))
        assertEquals(ExpenseCategory.TRANSPORTATION, ExpenseCategory.fromDisplayName("Transportation"))
        assertEquals(ExpenseCategory.ENTERTAINMENT, ExpenseCategory.fromDisplayName("Entertainment"))
    }

    @Test
    fun `fromDisplayName returns OTHER for unknown category`() {
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromDisplayName("Unknown"))
        assertEquals(ExpenseCategory.OTHER, ExpenseCategory.fromDisplayName(""))
    }

    @Test
    fun `all categories have valid display names`() {
        ExpenseCategory.values().forEach { category ->
            assertNotNull(category.displayName)
            assert(category.displayName.isNotBlank())
        }
    }
}
