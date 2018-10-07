package com.chairbender.bounce.musicTheory

import org.junit.Assert.*
import org.junit.Test

class IntervalTest {

    @Test
    fun intervalUp() {
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

    @Test
    fun intervalDown() {
        assertEquals(Note("A4"), Interval("m3").downFrom(Note("C4")))
        assertEquals(Note("A4"), Interval("M2").downFrom(Note("B4")))
        assertEquals(Note("A3"), Interval("P8").downFrom(Note("A4")))
        assertEquals(Note("A3"), Interval("P1").downFrom(Note("A3")))
        assertEquals(Note("A3"), Interval("M10").downFrom(Note("C#4")))
        assertEquals(Note("E#4"), Interval("dd2").downFrom(Note("Fb4")))
        assertEquals(Note("G3"), Interval("M2").downFrom(Note("A4")))
        assertEquals(Note("C#4"), Interval("d8").downFrom(Note("C5")))
        assertEquals(Note("C4"), Interval("A8").downFrom(Note("C#5")))
    }
}