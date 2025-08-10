package com.sulavtimsina.expensetracker.expense.domain

enum class ExpenseCategory(val displayName: String) {
    FOOD("Food"),
    TRANSPORTATION("Transportation"),
    ENTERTAINMENT("Entertainment"),
    SHOPPING("Shopping"),
    BILLS("Bills"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    TRAVEL("Travel"),
    OTHER("Other"),
    ;

    companion object {
        fun fromDisplayName(displayName: String): ExpenseCategory {
            return entries.find { it.displayName == displayName } ?: OTHER
        }
    }
}
