package io.robusta.nikotor

import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AsyncTest {

    @Before
    fun init() {
    }


    @Test
    fun testNow() {
        val result = futureNow(12);
        assertEquals(12, result.get())
    }

    @Test
    fun testUnit() {
        val result = futureUnit
        assertEquals(Unit, result.get())
    }


}