package com.chairbender.bounce.musicTheory

import org.junit.Assert.*
import org.junit.Test

class IntervalTest {

    @Test
    fun interval() {
        assertEquals(Note("C4"), Interval("m3").upFrom(Note("A4")))
        assertEquals(Note("B4"), Interval("M2").upFrom(Note("A4")))
        assertEquals(Note("A4"), Interval("P8").upFrom(Note("A3")))
        assertEquals(Note("A3"), Interval("P1").upFrom(Note("A3")))
        assertEquals(Note("C#4"), Interval("M10").upFrom(Note("A3")))
        assertEquals(Note("Fb4"), Interval("dd2").upFrom(Note("E#4")))
        assertEquals(Note("A4"), Interval("M2").upFrom(Note("G3")))
        assertEquals(Note("C5"), Interval("d8").upFrom(Note("C#4")))
        assertEquals(Note("C#5"), Interval("A8").upFrom(Note("C4")))
    }
}