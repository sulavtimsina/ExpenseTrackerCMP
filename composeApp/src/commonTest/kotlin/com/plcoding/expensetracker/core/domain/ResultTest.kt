package com.plcoding.expensetracker.core.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ResultTest {

    @Test
    fun `map transforms success value`() {
        val result: Result<Int, TestError> = Result.Success(5)
        val mapped = result.map { it * 2 }
        
        assertTrue(mapped is Result.Success)
        assertEquals(10, mapped.data)
    }

    @Test
    fun `map preserves error`() {
        val result: Result<Int, TestError> = Result.Error(TestError.UNKNOWN)
        val mapped = result.map { it * 2 }
        
        assertTrue(mapped is Result.Error)
        assertEquals(TestError.UNKNOWN, mapped.error)
    }

    @Test
    fun `onSuccess executes action for success`() {
        var executed = false
        val result: Result<Int, TestError> = Result.Success(5)
        
        result.onSuccess { executed = true }
        
        assertTrue(executed)
    }

    @Test
    fun `onSuccess does not execute action for error`() {
        var executed = false
        val result: Result<Int, TestError> = Result.Error(TestError.UNKNOWN)
        
        result.onSuccess { executed = true }
        
        assertTrue(!executed)
    }

    @Test
    fun `onError executes action for error`() {
        var executed = false
        val result: Result<Int, TestError> = Result.Error(TestError.UNKNOWN)
        
        result.onError { executed = true }
        
        assertTrue(executed)
    }

    @Test
    fun `onError does not execute action for success`() {
        var executed = false
        val result: Result<Int, TestError> = Result.Success(5)
        
        result.onError { executed = true }
        
        assertTrue(!executed)
    }

    @Test
    fun `asEmptyDataResult converts to EmptyResult`() {
        val result: Result<String, TestError> = Result.Success("test")
        val emptyResult = result.asEmptyDataResult()
        
        assertTrue(emptyResult is Result.Success)
        assertEquals(Unit, emptyResult.data)
    }

    private enum class TestError : Error {
        UNKNOWN
    }
}
