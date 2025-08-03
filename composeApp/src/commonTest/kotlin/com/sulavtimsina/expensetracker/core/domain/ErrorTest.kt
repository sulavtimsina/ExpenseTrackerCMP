package com.sulavtimsina.expensetracker.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ErrorTest {

    private class TestError(override val name: String) : Error

    @Test
    fun `Error interface exposes name property`() {
        val testName = "Test Error Name"
        val error: Error = TestError(testName)
        
        assertEquals(testName, error.name)
    }

    @Test
    fun `multiple Error implementations can have different names`() {
        val error1: Error = TestError("Error 1")
        val error2: Error = TestError("Error 2")
        
        assertEquals("Error 1", error1.name)
        assertEquals("Error 2", error2.name)
    }
}