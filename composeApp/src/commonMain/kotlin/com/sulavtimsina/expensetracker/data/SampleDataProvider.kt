package com.sulavtimsina.expensetracker.data

import com.sulavtimsina.expensetracker.expense.domain.Expense
import com.sulavtimsina.expensetracker.expense.domain.ExpenseCategory
import com.sulavtimsina.expensetracker.expense.domain.ExpenseRepository
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlin.random.Random


class SampleDataProvider(
    private val expenseRepository: ExpenseRepository
) {
    
    suspend fun insertSampleData() {
        // Check if sample data already exists by trying to get a known sample ID
        val existingSample = expenseRepository.getExpenseById("sample_expense_0")
        if (existingSample is com.sulavtimsina.expensetracker.core.domain.Result.Success && existingSample.data != null) {
            return // Don't insert if sample data already exists
        }
        
        val sampleExpenses = createSampleExpenses()
        sampleExpenses.forEach { expense ->
            expenseRepository.insertExpense(expense)
        }
    }
    
    private fun createSampleExpenses(): List<Expense> {
        val expenses = mutableListOf<Expense>()
        val currentDate = LocalDateTime.parse("2024-12-01T12:00:00")
        
        // Create expenses over the last 3 months with variety
        val expenseData = listOf(
            // Food expenses
            Triple(ExpenseCategory.FOOD, 15.50, "Lunch at cafe"),
            Triple(ExpenseCategory.FOOD, 67.80, "Weekly groceries"),
            Triple(ExpenseCategory.FOOD, 23.45, "Pizza delivery"),
            Triple(ExpenseCategory.FOOD, 8.90, "Coffee and pastry"),
            Triple(ExpenseCategory.FOOD, 45.20, "Restaurant dinner"),
            Triple(ExpenseCategory.FOOD, 12.75, "Sandwich and drink"),
            Triple(ExpenseCategory.FOOD, 89.30, "Grocery shopping"),
            Triple(ExpenseCategory.FOOD, 31.60, "Takeout Thai food"),
            Triple(ExpenseCategory.FOOD, 18.25, "Breakfast at diner"),
            
            // Transportation expenses
            Triple(ExpenseCategory.TRANSPORTATION, 2.50, "Bus fare"),
            Triple(ExpenseCategory.TRANSPORTATION, 45.00, "Gas for car"),
            Triple(ExpenseCategory.TRANSPORTATION, 12.00, "Subway day pass"),
            Triple(ExpenseCategory.TRANSPORTATION, 67.50, "Uber rides"),
            Triple(ExpenseCategory.TRANSPORTATION, 25.80, "Parking fee"),
            Triple(ExpenseCategory.TRANSPORTATION, 180.00, "Monthly bus pass"),
            Triple(ExpenseCategory.TRANSPORTATION, 35.40, "Taxi to airport"),
            
            // Entertainment expenses
            Triple(ExpenseCategory.ENTERTAINMENT, 15.00, "Movie ticket"),
            Triple(ExpenseCategory.ENTERTAINMENT, 45.75, "Concert tickets"),
            Triple(ExpenseCategory.ENTERTAINMENT, 25.50, "Bowling night"),
            Triple(ExpenseCategory.ENTERTAINMENT, 12.99, "Netflix subscription"),
            Triple(ExpenseCategory.ENTERTAINMENT, 8.50, "Book purchase"),
            Triple(ExpenseCategory.ENTERTAINMENT, 78.20, "Weekend activities"),
            Triple(ExpenseCategory.ENTERTAINMENT, 32.40, "Video games"),
            
            // Shopping expenses
            Triple(ExpenseCategory.SHOPPING, 89.99, "New jacket"),
            Triple(ExpenseCategory.SHOPPING, 156.75, "Shoes and accessories"),
            Triple(ExpenseCategory.SHOPPING, 34.50, "Home supplies"),
            Triple(ExpenseCategory.SHOPPING, 67.80, "Electronics"),
            Triple(ExpenseCategory.SHOPPING, 23.45, "Personal care items"),
            Triple(ExpenseCategory.SHOPPING, 145.20, "Clothing shopping"),
            Triple(ExpenseCategory.SHOPPING, 78.60, "Kitchen utensils"),
            
            // Bills expenses
            Triple(ExpenseCategory.BILLS, 120.00, "Electricity bill"),
            Triple(ExpenseCategory.BILLS, 45.60, "Internet service"),
            Triple(ExpenseCategory.BILLS, 89.50, "Phone bill"),
            Triple(ExpenseCategory.BILLS, 95.75, "Water and gas"),
            Triple(ExpenseCategory.BILLS, 56.40, "Insurance payment"),
            
            // Healthcare expenses
            Triple(ExpenseCategory.HEALTHCARE, 25.00, "Pharmacy visit"),
            Triple(ExpenseCategory.HEALTHCARE, 150.00, "Doctor appointment"),
            Triple(ExpenseCategory.HEALTHCARE, 45.80, "Dental cleaning"),
            Triple(ExpenseCategory.HEALTHCARE, 67.50, "Prescription medicine"),
            
            // Education expenses
            Triple(ExpenseCategory.EDUCATION, 89.99, "Online course"),
            Triple(ExpenseCategory.EDUCATION, 156.50, "Textbooks"),
            Triple(ExpenseCategory.EDUCATION, 45.00, "Workshop attendance"),
            
            // Travel expenses
            Triple(ExpenseCategory.TRAVEL, 450.00, "Flight tickets"),
            Triple(ExpenseCategory.TRAVEL, 125.60, "Hotel accommodation"),
            Triple(ExpenseCategory.TRAVEL, 67.80, "Travel insurance"),
            
            // Other expenses
            Triple(ExpenseCategory.OTHER, 34.75, "Gift for friend"),
            Triple(ExpenseCategory.OTHER, 78.90, "Miscellaneous items"),
            Triple(ExpenseCategory.OTHER, 23.45, "Pet supplies")
        )
        
        // Generate expenses with random dates over the last 90 days
        expenseData.forEachIndexed { index, (category, amount, note) ->
            val randomDaysAgo = Random.nextInt(0, 90)
            val expenseDate = currentDate.minusDays(randomDaysAgo.toLong())
            
            expenses.add(
                Expense(
                    id = "sample_expense_$index",
                    amount = amount,
                    category = category,
                    note = note,
                    date = expenseDate,
                    imagePath = null
                )
            )
        }
        
        return expenses.shuffled() // Randomize the order
    }
    
    private fun LocalDateTime.minusDays(days: Long): LocalDateTime {
        // Simple implementation for demo purposes
        val newDay = this.dayOfMonth - days.toInt()
        return if (newDay > 0) {
            LocalDateTime(year, month, newDay, hour, minute, second, nanosecond)
        } else {
            // For simplicity, just subtract from current date
            // In production, you'd want proper date arithmetic
            val monthNumber = month.ordinal + 1
            val newMonth = if (monthNumber > 1) monthNumber - 1 else 12
            val newYear = if (monthNumber > 1) year else year - 1
            // Use kotlinx.datetime.Month to create the month
            val monthEnum = Month.entries[newMonth - 1]
            LocalDateTime(newYear, monthEnum, 15, hour, minute, second, nanosecond)
        }
    }
}
