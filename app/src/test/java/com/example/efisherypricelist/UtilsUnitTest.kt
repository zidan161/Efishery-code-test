package com.example.efisherypricelist

import org.junit.Test
import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UtilsUnitTest {
    @Test
    fun toRupiah() {
        assertEquals("Rp. 10,000", (10000).toRupiah())
    }
}